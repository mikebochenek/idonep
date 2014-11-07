package actors

import akka.actor.Actor

class EmailJobActor() extends Actor {
  def receive = {
    case "send" => {
      send()
    }
    case _ => play.api.Logger.warn("unsupported message type")
  }

  def send() {
    play.api.Logger.info("executing send() in EmailJobActor..")
  }
}