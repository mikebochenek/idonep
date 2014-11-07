package actors

import akka.actor.Actor
import models.User

class EmailJobActor() extends Actor {
  def receive = {
    case "send" => {
      send()
    }
    case _ => play.api.Logger.warn("unsupported message type")
  }

  def send() {
    play.api.Logger.info("executing send() in EmailJobActor..")
    User.findAll.foreach(sendemail)
  }
  
  def sendemail(user: User) {
    println ("processing " + user.email)
  }
}