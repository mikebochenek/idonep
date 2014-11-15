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
import play.api.Logger

import com.thoughtworks.xstream._

import models._
import views._

object Settings extends Controller with Secured {
  def load() = IsAuthenticated { username =>
    implicit request => {
      val me = User.findByEmail(username)
      val confirmedTargets = Team.findMyTargets(me.id, 1)
      val unconfirmedTargets = Team.findMyTargets(me.id, 0)
      val confirmedOwners = Team.findMyOwners(me.id, 1)
      val unconfirmedOwners = Team.findMyOwners(me.id, 0)
      
      Logger.info("confirmedTargets:" + confirmedTargets.size 
          + " unconfirmedTargets:" + unconfirmedTargets.size
          + " confirmedOwners: " + confirmedOwners.size
          + " unconfirmedOwners: " + unconfirmedOwners.size)
      
      Ok(views.html.settings(settingsForm, me, confirmedTargets, unconfirmedTargets, confirmedOwners, unconfirmedOwners))
    }
  }

  val settingsForm = Form(
    tuple(
      "email" -> text,
      "language" -> text,
      "password" -> text,
      "passwordnew1" -> text,
      "passwordnew2" -> text,
      "newusertarget" -> text))

  def save = Action { implicit request =>
    val (email, language, password, passwordnew1, passwordnew2, newusertarget) = settingsForm.bindFromRequest.get
    
    Logger.info("email:" + email + " language:" + language)
    //Logger.info("password:" + password + " passwordnew1:" + passwordnew1 + " passwordnew2:" + passwordnew2)
    Logger.info("newusertarget:" + newusertarget)
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
