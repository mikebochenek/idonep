package models

import play.api.Logger

object AdminHelper {

  def generateStats(): String = {

    val usercount = User.countAll
    Logger.info("usercount:" + usercount)

    val donecount = Done.countAll
    Logger.info("donecount:" + donecount)
    
    val maillogcount = MailLog.countAll
    Logger.info("maillogcount:" + maillogcount)
    
    val teamcount = Team.countAll
    Logger.info("teamcount:" + teamcount)
    
    "done"
  }
}