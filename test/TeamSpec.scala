import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import models.User
import models.Done
import java.util.Date
import models.Team

@RunWith(classOf[JUnitRunner])
class TeamSpec extends Specification {

  "Application" should {

    "create team entries" in new WithApplication {
      //Team.create(1, 3);
    }
    
    "check my team targets" in new WithApplication {
      val targets = Team.findMyTargets(1L, 1)
      println(targets.size + "  my targets are: " + targets)
      targets.size must greaterThan(0)
    }
    
    "check my team owners" in new WithApplication {
      val owners = Team.findMyOwners(3L, 1)
      println(owners.size + "  my owners are: " + owners)
      owners.size must greaterThan(0)
    }

    "update team status" in new WithApplication {
      Team.updatestatus(1, 3, 1)
    }
  }
}
