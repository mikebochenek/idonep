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

import com.thoughtworks.xstream._

import models._
import views._

object Settings extends Controller with Secured {
  def load() = IsAuthenticated { username =>
    implicit request => {
      settingsForm.fill("test@eme.com", "finish")
      Ok(views.html.settings(settingsForm))
    }
  }

  val settingsForm = Form(
    tuple(
      "email" -> text,
      "language" -> text))

  /*def save = Action { implicit request =>
    settingsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.DoneList.index).withSession("email" -> user._1)
    )
  }*/

  def save = Action { implicit request =>
    val (email, language) = settingsForm.bindFromRequest.get
    println(email)
    println(language)
    Ok("Hi %s %s".format(email, language))
  }

  private val xstream = new XStream

  def toXML[T](obj: T): String = {
    xstream.toXML(obj)
  }

  def generateJSON = Action { implicit request =>
    Ok("done json")
  }
  def generateCSV = Action { implicit request =>
    Ok("done csv")
  }
  def generateXML = Action { implicit request =>
    Ok("done xml")
  }
}
