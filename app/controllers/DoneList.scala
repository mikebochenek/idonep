package controllers

import models.Done
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import views.html


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
