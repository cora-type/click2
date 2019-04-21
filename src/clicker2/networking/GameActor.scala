package clicker2.networking

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef}
import akka.io.{IO, Tcp}
import akka.io.Tcp.Write
import akka.util.ByteString
import clicker2.Game

case object Update

case object ClickGold

case object Save

case object Setup

case class BuyEquipment(equipmentID: String)

class GameActor(username: String) extends Actor {
  import akka.io.Tcp._
  import context.system
  IO(Tcp) ! Bind(self, new InetSocketAddress("localhost", 8000))
  var clients: Set[ActorRef] = Set()
  val game: Game = new Game(username)
  override def receive: Receive = {
    case Setup =>

      if (Database.playerExists(username)){
      Database.loadGame(username, game)
    } else {
        Database.createPlayer(username)
      }

    case Update =>

      game.update(game.lastUpdateTime)
      this.clients.foreach((client: ActorRef) => client ! Write(ByteString(game.toJSON())))

    case Save =>
      Database.saveGame(username, game.gold, game.equipment("shovels").numberOwned, game.equipment("excavators").numberOwned, game.equipment("mines").numberOwned, game.lastUpdateTime)

    case buy: BuyEquipment =>
      game.buyEquipment(eq)

    case ClickGold =>
      game.clickGold()
  }
}
