package controllers

import play.Play
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.data.Forms._
import views._

object DoneList extends Controller {

  def index = Action {
    Ok(html.donelist());
  }
}