package controllers

import java.io.File

import play.Play
import play.api.mvc.Action
import play.api.mvc.Session
import play.api.mvc.Controller
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import views._

object Memory extends Controller with Secured {
  def index() = IsAuthenticated { username =>
    implicit request => {
      val id = User.findByEmail(username).id
      Ok(views.html.memory())
    }
  }

  def get(id: Long) = IsAuthenticated { username =>
    implicit request => {
      val id = User.findByEmail(username).id
      if (username.contains("bochenek")) {
        val stats = AdminHelper.generateStats()
        Ok(views.html.iadmin(stats))
      } else {
        Ok(views.html.index())
      }
    }
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      picture.ref.moveTo(new File(s"/tmp/idone/$filename"))
      Ok("File uploaded")
    }.getOrElse {
      Redirect(routes.DoneList.about).flashing(
        "error" -> "Missing file")
    }
  }
}
