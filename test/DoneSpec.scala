import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import models.User
import models.Done
import java.util.Date

@RunWith(classOf[JUnitRunner])
class DoneSpec extends Specification {

  "Application" should {

    "fetch a lot of data from done table " in new WithApplication {
      val dones = Done.findAll(1L)
      dones.size must greaterThan(3)
    }

    "create done entries" in new WithApplication {
      val before = Done.findAll(1L).size
      val done = Done.create(new Done(1, 1, "delme" + System.currentTimeMillis(), new Date(), new Date(), false, 1, 101));
      done should be (done)
      val after = Done.findAll(1L).size
      after mustEqual(before+1)
    }
  }
}
