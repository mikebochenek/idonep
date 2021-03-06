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

object Application extends Controller {
  /** serve the index page app/views/index.scala.html */
  def index(any: String) = Action { implicit request =>
    Ok(views.html.index())
  }

  /** resolve "any" into the corresponding HTML page URI */
  def getURI(any: String): String = any match {
    case "main" => "/public/html/main.html"
    case "detail" => "/public/html/detail.html"
    case _ => "error"
  }

  /** load an HTML page from public/html */
  def loadPublicHTML(any: String) = Action {
    val projectRoot = Play.application().path()
    val file = new File(projectRoot + getURI(any))
    if (file.exists())
      Ok(scala.io.Source.fromFile(file.getCanonicalPath()).mkString).as("text/html");
    else
      NotFound
  }

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text) verifying ("invalidlogin", result => result match {
        case (email, password) => User.authenticate(email, password).isDefined
      }))

  //TODO obviously this will have to include 2nd password and a call to create
  // and error handling (email already in use...)
  val createUserForm = Form(
    tuple(
      "email" -> text,
      "password" -> text,
      "password2" -> text) verifying ("createuserfailed", result => result match {
        case (email, password, password2) => User.create(email, password, password2).isDefined
      }))

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.DoneList.index).withSession("email" -> user._1))
  }

  def createuser = Action { implicit request =>
    Ok(html.createuser(createUserForm))
  }

  def submitcreateuser = Action { implicit request =>
    createUserForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.createuser(formWithErrors)),
      user => Redirect(routes.DoneList.index).withSession("email" -> user._1))
  }

  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
      "success" -> "You've been logged out")
  }

  // -- Javascript routing

  /*
  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        Projects.add, Projects.delete, Projects.rename,
        Projects.addGroup, Projects.deleteGroup, Projects.renameGroup,
        Projects.addUser, Projects.removeUser, Tasks.addFolder, 
        Tasks.renameFolder, Tasks.deleteFolder, Tasks.index,
        Tasks.add, Tasks.update, Tasks.delete
      )
    ).as("text/javascript") 
  }
  
  */

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

/**
 * Provide security features
 */
trait Secured {

  /** Retrieve the connected user email.*/
  def username(request: RequestHeader) = request.session.get("email")

  /** Redirect to login if the user in not authorized. */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login)
  
  /** Action for authenticated users. */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) { user =>
    Action(request => f(user)(request))
  }
}
