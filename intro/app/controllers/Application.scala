package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  val convertorURI = "http://www.google.com/ig/calculator?hl=ENG"

  def index = Action {
    Ok(views.html.index())
  }

  def find = Action {
    Ok(views.html.find())
  }

  def javascriptRoutes = Action { implicit request =>
    import routes.javascript._
    Ok(
      Routes.javascriptRouter("routes")(
        Calculator.convert, Locator.locate
      )
    ).as("text/javascript") 
  }
}