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
import play.api.libs.json.Json

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

  def generateJSON = IsAuthenticated { username =>
    implicit request => {
      val all = Done.findAll(User.findByEmail(username).id)
      Ok(Json.toJson(all.map(a => Json.toJson(a))))
    }
  }

  def generateCSV = IsAuthenticated { username =>
    implicit request => {
      val all = Done.findAll(User.findByEmail(username).id)
      val csvstr = all.mkString("\n")
      val header = "id,owner,donetext,donedate,createdate,deleted,category,doneDay\n"

      Ok(header + csvstr.replaceAll("Done[(]", "").replaceAll("[)]", "")) 
      
      //TODO kinda wrong because commas in the done text, breaks everything
      //TODO simplified, but I want only starting 
      //TODO or better use a real library - i.e. http://code.google.com/p/opencsv/
    }
  }

  def generateXML = IsAuthenticated { username =>
    implicit request => {
      val xstream = new XStream
      val all = Done.findAll(User.findByEmail(username).id)
      Ok(xstream.toXML(all))
    }
  }
}
