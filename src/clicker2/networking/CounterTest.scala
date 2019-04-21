package clicker2.networking

import akka.actor.{ActorSystem, Props}

object CounterTest extends App{

  val system = ActorSystem("FirstSystem")

  val actor = system.actorOf(Props(classOf[GameActor], "ye"))
  actor ! Setup

}
