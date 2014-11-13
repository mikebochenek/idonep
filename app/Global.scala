import akka.actor.{ Actor, Props }
import play.api.libs.concurrent.Akka
import play.api.GlobalSettings
import play.api.templates.Html
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.User
import actors.EmailJobActor
import java.util.Calendar
import java.util.Date

/**
 * Application global object, used here to schedule jobs on application start-up.
 */
object Global extends GlobalSettings {

  override def onStart(application: play.api.Application) {

    play.api.Logger.info("Scheduling jobs...")
    import scala.concurrent.duration._
    import play.api.Play.current


    val actor = Akka.system.actorOf(
      Props(new EmailJobActor()))

    Akka.system.scheduler.schedule(calculateDelayForSchedule.seconds, 1.days, actor, "send")
    //Akka.system.scheduler.schedule(0.seconds, 1.minutes, actor, "send")
  }

  /**
   * so hacky... can't believe that I have to do this
   * http://stackoverflow.com/questions/13700452/scheduling-a-task-at-a-fixed-time-of-the-day-with-akka
   * http://brainstep.blogspot.ch/2013/10/scheduling-jobs-in-play-2.html
   */
  private def calculateDelayForSchedule: Long = {
    var c = Calendar.getInstance();
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 45);
    c.set(Calendar.SECOND, 0);
    var plannedStart = c.getTime();
    val now = new Date();
    var nextRun = c.getTime();
    if (now.after(plannedStart)) {
      c.add(Calendar.DAY_OF_WEEK, 1);
      nextRun = c.getTime();
    }
    val delayInSeconds = (nextRun.getTime() - now.getTime()) / 1000; //To convert milliseconds to seconds.
    delayInSeconds
  }

}
