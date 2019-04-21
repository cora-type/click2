package clicker2.networking

import akka.actor.{Actor, ActorSystem, Props}


case object UpdateGames

case object AutoSave

case class GameState(gameState: String)

class ClickerServer extends Actor {

  override def receive: Receive = {

//    Example of adding an actor with this actor as its supervisor
//    Note that we use the context of this actor and do not create a new actor system
//    val childActor = context.actorOf(Props(classOf[GameActor], username))


    case UpdateGames =>
    case AutoSave =>
    case gs: GameState =>
      val delimiter = "~"
  }

}


object ClickerServer {

  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem()

    import actorSystem.dispatcher

    import scala.concurrent.duration._

    val server = actorSystem.actorOf(Props(classOf[ClickerServer]))

    actorSystem.scheduler.schedule(0 milliseconds, 100 milliseconds, server, UpdateGames)
    actorSystem.scheduler.schedule(0 milliseconds, 5000 milliseconds, server, AutoSave)
  }
}
