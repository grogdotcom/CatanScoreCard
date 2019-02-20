package score
import game.{CatanBoard, CatanPlayer}

sealed abstract class CatanBuilding {
  val cost: ResourceSet
}
case class Road(roadLength: Int) extends CatanBuilding {
  val cost = ScalaResourceSet(wo = 1, br = 1)
}
case class City(hexes: Int*) extends CatanBuilding {
  val cost = ScalaResourceSet(or = 3, wh = 2)
}
case class Settlement(hexes: Int*) extends CatanBuilding {
  val cost = ScalaResourceSet(wo = 1, br = 1, wh = 1, sh = 1)
}
case object DCard extends CatanBuilding{
  val cost = ScalaResourceSet(or = 1, wh = 1, sh = 1)
}

sealed abstract class DevelopmentCard
case class Knight(robberLocation: Int, steal: Option[Steal]) extends DevelopmentCard
case class RoadBuilder(roadLength: Int) extends DevelopmentCard
case class YearOfPlenty(res1: Resource, res2: Resource) extends DevelopmentCard
case class Monopoly(resource: Resource, numLost: Map[CatanPlayer, Int]) extends DevelopmentCard
case object Point extends DevelopmentCard

sealed abstract trait Port
case object ThreeForOne extends Port

sealed abstract trait Resource
case object Wood extends Resource with Port
case object Brick extends Resource with Port
case object Sheep extends Resource with Port
case object Wheat extends Resource with Port
case object Ore extends Resource with Port
case object Unknown extends Resource
object Resource {
  val resourceList = List(Wood, Brick, Sheep, Wheat, Ore)
}

case class Roll(number: Int) {
  val dots: Int = 6 - Math.abs(7 - number)
  val prod: Double = dots / 36.0
}
case object Seven extends Roll(7)
case class CatanHex(resource: Resource, number: Roll)

sealed abstract class LoseOrGain
case object Lose extends LoseOrGain
case object Gain extends LoseOrGain

sealed abstract class CardExchange(loseOrGain: LoseOrGain)
case class GainFromRoll(resourceSet: ResourceSet) extends CardExchange(Gain)
case class LoseFromSeven(resourceSet: ResourceSet) extends CardExchange(Lose)
case class FromSteal(lg: LoseOrGain, resource: Resource) extends CardExchange(lg)
case class LoseByBuilding(building: CatanBuilding, resourceSet: ResourceSet) extends CardExchange(Lose)
case class TradeSent(lg: LoseOrGain, resourceSet: ResourceSet) extends CardExchange(lg)

case class Steal(playerStole: CatanPlayer, resourceStolen: Resource)
case class Trade(tradePartner: CatanPlayer, cardsReceived: ResourceSet, cardsSent: ResourceSet)




