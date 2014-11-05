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


object Settings extends Controller with Secured {
  def load() = IsAuthenticated { username =>
    implicit request => {
    	Ok(views.html.settings(settingsForm))
    }
  }
  
  val settingsForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )
  
  def save = Action { implicit request =>
    settingsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.DoneList.index).withSession("email" -> user._1)
    )
  }

}
