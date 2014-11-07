import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import models.User
import models.Done
import java.util.Date
import com.typesafe.plugin._
import play.api.Play.current
import actors.EmailJobActor
import akka.actor.{ Actor, Props }
import play.api.libs.concurrent.Akka
import scala.concurrent.duration._
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext

    
@RunWith(classOf[JUnitRunner])
class EmailSpec extends Specification {

  "Application" should {

    "send one test email" in new WithApplication {
      val mail = use[MailerPlugin].email
      mail.setSubject("mailer test")
      mail.setRecipient("mike.bochenek@gmail.com")
      mail.setFrom("mike.bochenek@gmail.com")
      //mail.sendHtml("<html>html</html>")
    }
    
   "check email job" in new WithApplication {
      val actor = Akka.system.actorOf(Props(new EmailJobActor()))

//      Akka.system.scheduler.schedule(1.seconds, 1.days, actor, "send")
      
      //val myActor = Akka.system.actorOf(Props(classOf[EmailJobActor]), "case2-primary")
      //tester.send(fileActor, CreateFile(1))
    }

  }
}

/** 
import com.typesafe.plugin._
 
val mail = use[MailerPlugin].email
mail.setSubject("mailer")
mail.setRecipient("Peter Hausel Junior <noreply@email.com>","example@foo.com")
//or use a list
mail.setBcc(List("Dummy <example@example.org>", "Dummy2 <example@example.org>"):_*)
mail.setFrom("Peter Hausel <noreply@email.com>")
//adds attachment
mail.addAttachment("attachment.pdf", new File("/some/path/attachment.pdf"))
//adds inline attachment from byte array
val data: Array[Byte] = "data".getBytes
mail.addAttachment("data.txt", data, "text/plain", "A simple file", EmailAttachment.INLINE)
//sends html
mail.sendHtml("<html>html</html>" )
//sends text/text
mail.send( "text" )
//sends both text and html
mail.send( "text", "<html>html</html>")
*/