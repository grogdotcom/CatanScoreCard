package score

import game.CatanPlayer

case class CatanLog(log: List[LogEvent] = Nil) {
  def append(appendLog: List[LogEvent]): CatanLog = {
    copy(log = appendLog ::: log)
  }
}

abstract class LogEvent{
  def logString: String
}
case class RollDiceLog(player: CatanPlayer, diceRoll: Roll) extends LogEvent {
  override def logString: String = s"${player.name} rolled a ${diceRoll.number}."
}
case class CollectedResourcesLog(player: CatanPlayer, resourceSet: ResourceSet) extends LogEvent {
  override def logString: String = s"${player.name} collected $resourceSet."
}
case class LostFromRobberLog(player: CatanPlayer, resourceSet: ResourceSet) extends LogEvent {
  override def logString: String = s"${player.name} was robbed of $resourceSet."
}
case class MovedRobberLog(oldLocation: Int, newLocation: Int) extends LogEvent {
  override def logString: String = s"Robber was moved from $oldLocation to $newLocation."
}
case class PlayerStoleLog(player: CatanPlayer, steal: Option[Steal]) extends LogEvent {
  override def logString: String = {
    ""
  }
}
