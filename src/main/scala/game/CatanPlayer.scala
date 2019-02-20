package game

import score._
import ScalaResourceSet._

case class CatanPlayer(name: String,
                               position: Int,
                               currentKnownResources: ResourceSet  = ScalaResourceSet.empty,
                               boardPoints: Int = 0,
                               armyPoints: Int = 0,
                               roadPoints: Int = 0,
                               devPoints: Int = 0,
                               built: List[CatanBuilding] = Nil,
                               resGainedRolls: ResourceSet = ScalaResourceSet.empty,
                               resGainedSteal: ResourceSet = ScalaResourceSet.empty,
                               resGainedDev: ResourceSet = ScalaResourceSet.empty,
                               resLost7: ResourceSet = ScalaResourceSet.empty,
                               resLostSteal: ResourceSet = ScalaResourceSet.empty,
                               resLostDev: ResourceSet = ScalaResourceSet.empty,
                               ports: List[Port] = Nil,
                               numPortTrades: Int = 0,
                               resTradedInPort: ResourceSet = ScalaResourceSet.empty,
                               resReceivedPort: ResourceSet = ScalaResourceSet.empty,
                               numTrades: Int = 0,
                               resTradedInTrade: ResourceSet = ScalaResourceSet.empty,
                               resReceivedTrade: ResourceSet = ScalaResourceSet.empty,
                               dots: ResourceSet = ScalaResourceSet.empty,
                               roadLength: Int = 0,
                               numKnights: Int = 0
                              ) {

  def ==(other: CatanPlayer): Boolean = {
    name == other.name && position == other.position
  }

  def points = boardPoints + devPoints+ armyPoints + roadPoints

  def collectedResourceFromRoll(resourceSet: ResourceSet): CatanPlayer = {
    val updatedCurrentRes: ResourceSet = currentKnownResources + resourceSet
    val updatedResFromRolls: ResourceSet = resGainedRolls + resourceSet
    copy(
      currentKnownResources = updatedCurrentRes,
      resGainedRolls = updatedResFromRolls
    )

  }

  def build(toBuild: CatanBuilding): CatanPlayer = {
    val updatedPoints = toBuild match {
      case Settlement(_) | City(_) => points + 1
    }
    val updatedBuildings = toBuild :: built
    copy(
      currentKnownResources = currentKnownResources - toBuild.cost
    )
  }

}
