package game

import soc.game.SOCResourceSet


case class PlayerState(name: String,
                       position: Int,
                       currentKnownResources: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
                       boardPoints: Int = 0,
                       armyPoints: Int = 0,
                       roadPoints: Int = 0,
                       devPoints: Int = 0,
                       ports: Set[Int] =  Set.empty,
                       settlementNodes: List[Int] = Nil,
                       cityNodes: List[Int] = Nil,
                       roadEdges: List[Int] = Nil,
                       playedDCards: Int,
                       playableDCards: Int,
                       newCards: Int,
                       dots: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
                       roadLength: Int = 0,
                       numKnights: Int = 0
//                       resGainedRolls: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       resGainedSteal: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       resGainedDev: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       resLost7: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       resLostSteal: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       resLostDev: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       numPortTrades: Int = 0,
//                       resTradedInPort: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       resReceivedPort: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       numTrades: Int = 0,
//                       resTradedInTrade: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
//                       resReceivedTrade: SOCResourceSet  = SOCResourceSet.EMPTY_SET,
                      ) extends SOCState {

  def ==(other: PlayerState): Boolean = {
    name == other.name && position == other.position
  }

  def points = boardPoints + devPoints+ armyPoints + roadPoints

  def collectedResourceFromRoll(resourceSet: SOCResourceSet): PlayerState = {
    val c = copy(
      currentKnownResources = new SOCResourceSet(currentKnownResources)
    )
    c.currentKnownResources.add(resourceSet)
    c

  }

  override def getStateArray: List[Int] = {
    List(position)


  }
}

