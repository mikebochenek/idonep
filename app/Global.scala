import akka.actor.{ Actor, Props }
import play.api.libs.concurrent.Akka
import play.api.GlobalSettings
import play.api.templates.Html
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import models.User
import actors.EmailJobActor
import java.util.Calendar

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

    Akka.system.scheduler.schedule(
      5.seconds, 3.seconds, actor, "send")
  }

}
