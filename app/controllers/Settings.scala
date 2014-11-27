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
      
      val fullUser = User.getFullUser(username)

      if (fullUser.settings != null) {
        val userSettings = Json.parse(fullUser.settings) \ "language" //.asOpt[UserSettings] //TODO this doesn't parse!!
        Logger.debug("parse ----->" + userSettings)
      }
      
      val confirmedTargets = Team.findMyTargets(me.id, 1)
      val unconfirmedTargets = Team.findMyTargets(me.id, 0)
      val confirmedOwners = Team.findMyOwners(me.id, 1)
      val unconfirmedOwners = Team.findMyOwners(me.id, 0)
      
      Logger.debug("confirmedTargets:" + confirmedTargets.size 
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

  def save = IsAuthenticated { username =>
    implicit request => { 
      val (email, language, password, passwordnew1, passwordnew2, newusertarget) = settingsForm.bindFromRequest.get

      Logger.debug("email:" + email + " language:" + language)

      val fullUser = User.getFullUser(username)

      if (fullUser.settings == null || language != Json.parse(fullUser.settings) \ "language") {
        Logger.debug("yes, we will save the language:" + language)

        val userSettings = new UserSettings(fullUser.id, language, false, null)
        val settingsJson = Json.toJson(userSettings).toString
        Logger.debug(settingsJson)
        User.update(fullUser, email, settingsJson)
      }
      
      //Logger.debug("password:" + password + " passwordnew1:" + passwordnew1 + " passwordnew2:" + passwordnew2)

      if (password != null && password.trim.length > 0 
          && passwordnew1 != null && passwordnew1.trim.length > 0 && passwordnew1.equals(passwordnew2)) {
        Logger.info("changing password for email:" + username)
        if (username.equals(User.authenticate(username, password).get.email)) {
          User.updatepassword(username, passwordnew1)
        }
      } //TODO only handles happy path for now

      Logger.debug("newusertarget:" + newusertarget)
      if (newusertarget != null && newusertarget.trim.length > 0) {
        val targetId = User.findByEmail(newusertarget).id
        Team.create(User.findByEmail(username).id, targetId)
        Logger.info("added new target:" + newusertarget + " targetId:"+ targetId)
      } //TODO only works with happy flow
      //TODO user.findbyemail can fail with [RuntimeException: SqlMappingError(No rows when expecting a single one)]
      
      Redirect(routes.Settings.load)
    }
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
