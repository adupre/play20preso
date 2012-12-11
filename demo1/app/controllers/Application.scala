package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent._

object Application extends Controller {
  val fooPromise = Promise[String]()

  def foo = Action { implicit request =>
		Async {
			fooPromise.map { str =>
				Ok(str)			
			}
		}
  }

	def index = Action {
		Ok(views.html.index())
	}
  
	// curl -X POST http://localhost:9000/foo/A
	def setFoo(foo: String) = Action {
		fooPromise.redeem(foo)
		Ok
	}
}