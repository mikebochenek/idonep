package controllers

import models.Done
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Session
import views.html
import play.api.libs.functional.syntax._
import java.util.Date
import models.User
import models.Tag

object DoneList extends Controller with Secured {

  def index = Action { implicit request =>
    Ok(html.donelist());
  }

  def getByDay(id: Long) = IsAuthenticated { username =>
    implicit request => {
      val all = Done.findByDoneDay(username, id)
      Ok(Json.toJson(all.map(a => Json.toJson(a))))
    }
  } 

  def about = Action { implicit request =>
    Ok(html.about());
  }

  def contact = Action { implicit request =>
    Ok(html.contact());
  }

  def create() = IsAuthenticated { username =>
    implicit request => {
      val txt = (request.body.asJson.get \ "donetext")
      val doneday = (request.body.asJson.get \ "doneday")
      println("yup, we are in the create now with " + txt.as[String] + " on " + doneday.as[String].toInt)
      
      val id = User.findByEmail(username).id
      val tags = Tag.findAll(id)

      val newtags = extractHashes(txt.as[String])

      //TODO we should probably replace all newtags within txt...  
      val newDone = Done.create(new Done(1, id, txt.as[String], new Date(), new Date(), false, 1, doneday.as[String].toInt))
      
      for (tag <- newtags) {
        if (tags.contains(tag)) {
          // if existing, than assign
        } else {
          // compare to existing tags, create if needed
          val newlycreatetag = Tag.create(new Tag(0, id, tag.substring(1), null, false))
          println (newlycreatetag.id + " " + newlycreatetag) //TODO this doesn't return the new ID.. wtf!
        }
      }

      Ok(Json.toJson(newDone))
    }
  }
  
  def extractHashes(s: String): Array[String] = {
    s.split("\\ ").filter(a => a.startsWith("#"))
  }

  def update(id: Long) = IsAuthenticated { username =>
    implicit request => {
      Ok(html.contact()) // return the created celebrity in a JSON
    }
  }

  def delete(id: Long) = IsAuthenticated { username =>
    implicit request => {
      Ok(html.contact()) // return the created celebrity in a JSON
    }
  }
}
