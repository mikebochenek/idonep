package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import play.Play
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.data.Forms._
import views._
import play.api.libs.json.Json
import models.Done
import scala.concurrent.impl.Future
import play.api.mvc.SimpleResult
import play.api.mvc.Request


object DoneList extends Controller with Secured {

  def index = Action {
    Ok(html.donelist());
  }

  def getByDay(id: String) = IsAuthenticated { username =>
    implicit request => {
      val all = Done.findByDoneDay(username, 2014093)
      Ok(Json.toJson(all.map(a => Json.toJson(a))))
    }
  }

  /** list all celebrities 
  def index = Action.async {
    val cursor = collection.find(
      BSONDocument(), BSONDocument()).cursor[Celebrity] // get all the fields of all the celebrities
    val futureList = cursor.collect[List]() // convert it to a list of Celebrity
    futureList.map { celebrities => Ok(Json.toJson(celebrities)) } // convert it to a JSON and return it
  }*/

  
  def about = Action {
    Ok(html.about());
  }

    
  def contact = Action {
    Ok(html.contact());
  }


  /*
  def create() = Action.async(parse.json) { 
    Ok(html.contact()) // return the created celebrity in a JSON
  }

  def getByDay(id: String) = Action.async(parse.empty) { 
    Ok(html.contact()) // return the created celebrity in a JSON
  }

  def update(id: String) = Action.async(parse.json) {     
    Ok(html.contact()) // return the created celebrity in a JSON
  }

  def delete(id: String) = Action.async(parse.empty) {     
    Ok(html.contact()) // return the created celebrity in a JSON
  }
  */
}