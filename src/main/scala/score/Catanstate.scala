package score

import ScalaResourceSet._
import game.{CatanBoard, CatanPlayer}

case class CatanState(gameBoard: CatanBoard,
                      resourceBank: ResourceSet,
                      players: List[CatanPlayer],
                      rolls: Map[Roll, Int],
                      diceStack: List[Roll],
                      devCards: Int,
                      log: CatanLog)


object CatanState {

  /**
    * roll the dice and the players collect the resources
    * @param diceRoll the roll of the dice. integer from 2 to 12
    * @see roll7 if dice roll is a 7
    */
  def rollDice(state: CatanState, diceRoll: Roll): CatanState = {
    val currentPlayer = state.players.head
    val rollDiceLog = RollDiceLog(currentPlayer, diceRoll)

    val rollCount = state.rolls(diceRoll)
    val updatedRolls = (state.rolls - diceRoll) + (diceRoll -> (rollCount + 1))

    val diceStack = diceRoll :: state.diceStack

    val actualResForPlayers: Map[CatanPlayer, ResourceSet] = {
      val resForPlayers: Map[CatanPlayer, ResourceSet] = state.gameBoard.getResourcesForRoll(diceRoll)
      val totalResourcesCollected: ResourceSet = {
        resForPlayers.values.foldLeft(ScalaResourceSet.empty)(_+_)
      }
      if (!state.resourceBank.contains(totalResourcesCollected)) {
        val overflowTypes = {
          val overflow = totalResourcesCollected - state.resourceBank
          Resource.resourceList.filter(res => overflow.contains(res))
        }
        resForPlayers.map { case (player, resourceSet) =>
          val resourcesToLose: Map[Resource, Int] = {
            overflowTypes.map { res =>
              res -> resourceSet.getAmount(res)
            }.toMap
          }
          val subtractResourceSet = ScalaResourceSet(resourcesToLose)
          (player -> (resourceSet - subtractResourceSet))
        }
      } else resForPlayers
    }

    val players = state.players.map { player =>
      player.copy(
        currentKnownResources = player.currentKnownResources + actualResForPlayers(player),
        resGainedRolls = player.resGainedRolls + actualResForPlayers(player)
      )
    }
    val updatedBank = {
      val actualTotalResources = {
        actualResForPlayers.values.foldLeft(ScalaResourceSet.empty)(_+_)
      }
      state.resourceBank - actualTotalResources
    }

    val collectedResourcesLogs = state.players.filter { player =>
      actualResForPlayers(player).getTotal > 0
    }.map { player =>
      CollectedResourcesLog(player, actualResForPlayers(player))
    }

    val log = collectedResourcesLogs ::: rollDiceLog :: Nil

    state.copy(
      resourceBank = updatedBank,
      rolls = updatedRolls,
      diceStack = diceStack,
      players = players,
      log = state.log.append(log)
    )
  }

  /**
    * when a 7 is rolled move the robber to a new location.
    * Steal a card from a player on the hex of the new robber location.
    * If players have more than 7 cards indicate which cards they discarding
    * @param robberLocation the new robber location
    * @param playerStole the player being stolen from
    * @param cardsLost which players lost which cards
    */
  def roll7(state: CatanState,
            cardsLost: Map[CatanPlayer, ResourceSet],
            robberLocation: Int,
            stolenResource: Option[Steal]): CatanState = {
    val currentPlayer = state.players.head
    val rollDiceLog: LogEvent = RollDiceLog(currentPlayer, Seven)

    val rollCount = state.rolls(Seven)
    val updatedRolls = (state.rolls - Seven) + (Seven -> (rollCount + 1))

    val diceStack = Seven :: state.diceStack

    val playersLostCards = state.players.map { player =>
      val resourceSet = cardsLost(player)
      player.copy(
        currentKnownResources = player.currentKnownResources - resourceSet,
        resLost7 = player.resLost7 + resourceSet
      )
    }
    val lostCardLogs: List[LogEvent] = {
      state.players
        .filter(player => cardsLost(player).getTotal > 0)
        .map { player =>
          val resourceSet = cardsLost(player)
          LostFromRobberLog(player, resourceSet)
        }
    }
    val updatedBank = {
      val totalResourcesLost: ResourceSet = {
        state.players.map(p => cardsLost(p)).foldLeft(ScalaResourceSet.empty)(_+_)
      }
      state.resourceBank - totalResourcesLost
    }

    val updatedBoard = state.gameBoard.moveRobber(robberLocation)
    val robberLocationLog: LogEvent = MovedRobberLog(state.gameBoard.robberLocation, robberLocation)

    val players = stealFunc(stolenResource, state.players)

    val stealingLogs: Option[LogEvent] = stolenResource.map { steal =>
      PlayerStoleLog(currentPlayer, steal.playerStole, steal.resourceStolen)
    }

    val log = {
      rollDiceLog :: lostCardLogs :::
        (Some(robberLocationLog) :: stealingLogs :: Some(rollDiceLog) :: Nil).flatten
    }

    state.copy(
      //gameBoard = updatedBoard,
      resourceBank = updatedBank,
      rolls = updatedRolls,
      diceStack = diceStack,
      players = players,
      log = state.log.append(log)
    )
  }

  /**
    *
    * @param toBuild
    */
  def build(state: CatanState, toBuild: CatanBuilding): CatanState = {
    val currentPlayer = state.players.head

    val updatedPlayer = currentPlayer.build(toBuild)
    val updatedBank = state.resourceBank + toBuild.cost

    val updatedState = state.copy(
      resourceBank = updatedBank,
      players = updatedPlayer :: state.players.tail
    )

    toBuild match {
      case Road(roadLength) =>
      case Settlement(hexes) =>
      case City(hexes) =>
      case DCard =>
    }


  }

  def playDevelopmentCard(state: CatanState, dCard: DevelopmentCard): CatanState = {
    val currentPlayer = state.players.head

    dCard match {
      case Knight(robberLocation, playerStole) =>

        val updatedBoard = state.gameBoard.moveRobber(robberLocation)
        val robberLocationLog: LogEvent = MovedRobberLog(state.gameBoard.robberLocation, robberLocation)

        val playersLA = {
          val numKnightsCurPlayer = currentPlayer.numKnights + 1
          val currentLAOpt = state.players.filter(_.numKnights >= 3).sortWith(_.numKnights > _.numKnights).headOption

          val playerLostArmy: Option[CatanPlayer] = currentLAOpt match {
            case Some(player) if player == currentPlayer => None
            case Some(player) if numKnightsCurPlayer >= player.numKnights => Some(player)
            case None => None
          }
          val updatedCurrentPlayer = (playerLostArmy match {
            case None if currentLAOpt.isDefined => currentPlayer
            case None if numKnightsCurPlayer < 3 => currentPlayer
            case _ =>
              currentPlayer.copy(
                armyPoints = 2
              )
          }).copy(
            numKnights = numKnightsCurPlayer
          )
          val updatedTail = state.players.tail.map { player =>
            playerLostArmy match {
              case Some(`player`) =>
                player.copy(
                  armyPoints = 0
                )
              case _ => player
            }
          }
          updatedCurrentPlayer :: updatedTail
        }
        val players = stealFunc(playerStole, playersLA)
        state.copy(
          players = players
        )
      case RoadBuilder(roadLength) => state
      case YearOfPlenty(res1, res2) =>
        val resourceSet = List(res1, res2)
        val curPlayer = currentPlayer.copy (
          currentKnownResources = currentPlayer.currentKnownResources + resourceSet,
          resGainedDev = currentPlayer.resGainedDev + resourceSet
        )
        state.copy(players = curPlayer :: state.players.tail)
      case Monopoly(resource, numLost) =>
        val resourceGained = Map(resource -> numLost.values.sum)
        val updatedCurrentPlayer = currentPlayer.copy(
          currentKnownResources = currentPlayer.currentKnownResources + resourceGained,
          resGainedDev = currentPlayer.resGainedDev + resourceGained
        )
        val updatedPlayerTail = state.players.tail.map { player =>
          val resourceSet = Map(resource -> numLost(player))
          player.copy(
            currentKnownResources = player.currentKnownResources - resourceSet,
            resLostDev = player.resLostDev + resourceSet
          )
        }
        state.copy(players = updatedCurrentPlayer :: updatedPlayerTail)
      case Point => state
    }
  }

  def trade(state: CatanState, trade: Trade): CatanState = {
    val currentPlayer: CatanPlayer = state.players.head

    def cardsExchange(player: CatanPlayer, cardsGained: ResourceSet, cardsSent: ResourceSet): CatanPlayer = {
      player.copy(
        currentKnownResources = player.currentKnownResources - cardsSent + cardsGained,
        numTrades = player.numTrades + 1,
        resReceivedTrade = player.resReceivedTrade + cardsGained,
        resTradedInTrade = player.resTradedInTrade + cardsSent
      )
    }

    val players = state.players.map { player =>
      if (player == currentPlayer) {
        cardsExchange(player, trade.cardsReceived, trade.cardsSent)
      } else if (player == trade.tradePartner) {
        cardsExchange(player, trade.cardsSent, trade.cardsReceived)
      } else player
    }

    state.copy(players = players)

  }

  def portTrade(state: CatanState, tradedIn: ResourceSet, recieved: ResourceSet): CatanState = {
    val currentPlayer = state.players.head
    currentPlayer.copy(
      currentKnownResources = currentPlayer.currentKnownResources - tradedIn + recieved,
      numPortTrades = currentPlayer.numPortTrades + 1,
      resTradedInPort = currentPlayer.resTradedInTrade + tradedIn,
      resReceivedPort = currentPlayer.resReceivedPort + recieved
    )
    state.copy (
      players = currentPlayer :: state.players.tail
    )
  }

  def initialSettlement(player: Player, settlement: Settlement): Unit

  def endTurn(state: CatanState): CatanState = {
    val currentPlayer = state.players.head
    state.copy(
      players = state.players.tail ::: List(currentPlayer)
    )
  }

  private def stealFunc(playerStole: Option[Steal], players: List[CatanPlayer]): List[CatanPlayer] = {
    val currentPlayer = players.head
    playerStole.map { steal =>
      players.map { player =>
        val stolenSet = List(steal.resourceStolen)
        if (player == steal.playerStole) {
          player.copy(
            currentKnownResources = player.currentKnownResources - stolenSet,
            resLostSteal = player.resLostSteal + stolenSet
          )
        } else if (player == currentPlayer) {
          player.copy(
            currentKnownResources = player.currentKnownResources + stolenSet,
            resGainedSteal = player.currentKnownResources + stolenSet
          )
        }
        else player
      }
    }.getOrElse(players)
  }
}
