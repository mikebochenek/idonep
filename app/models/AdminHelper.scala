package models

import play.api.Logger
import scala.sys.process._

object AdminHelper {

  def generateStats(): String = {
    val usercount = User.countAll

    val donecount = Done.countAll

    val maillogcount = MailLog.countAll

    val teamcount = Team.countAll

    (system_date + system_uptime + "<br>" 
        + "usercount:" + usercount + "<br>" + "donecount:" + donecount + "<br>" 
        + "maillogcount:" + maillogcount + "<br>" + "teamcount:" + teamcount + "<br><br>"
        + system_df + "<br><br>" + system_top + "<br><br>").replaceAll("\n", "<br>")
  }

  //http://stackoverflow.com/questions/16162483/execute-external-command
  def system_df(): String = { "df".!! }

  def system_top(): String = { "top -b -n1".!! }
  
  def system_date(): String = { "date".!! }

  def system_uptime(): String = { "uptime".!! }

}