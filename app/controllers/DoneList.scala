package controllers

import models.Done
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import views.html
import play.api.libs.functional.syntax._
import java.util.Date
import models.User

object DoneList extends Controller with Secured {

  def index = Action {
    Ok(html.donelist());
  }

  def getByDay(id: Long) = IsAuthenticated { username =>
    implicit request => {
      val all = Done.findByDoneDay(username, id)
      Ok(Json.toJson(all.map(a => Json.toJson(a))))
    }
  }

  def about = Action {
    Ok(html.about());
  }

  def contact = Action {
    Ok(html.contact());
  }

  def create() = IsAuthenticated { username =>
    implicit request => {
      val txt = (request.body.asJson.get \ "donetext")
      //println (request.body.asJson.map(a => Json.fromJson[models.Done](a)))
      println("yup, we are in the create now with " + txt.as[String])
      
      val id = User.findByEmail(username).id
      
      //TODO still need to extract donedate
      Done.create(new Done(1, id, txt.as[String], new Date(), new Date(), false, 1, 2014093))
      Ok
    }
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
