package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee._

object IterateeC extends Controller {
	
	// curl -i -X PUT -T "/Users/anthony/Downloads/ubuntu-12.10-desktop-i386.7z" http://localhost:9000/iter/
	
	val bodyparser = BodyParser { rh =>
		Iteratee.fold[Array[Byte], Int](0) { case (a, e) => a+1 }.map(
			Right(_)
		)
	}
	
	def index(file: String) = Action(bodyparser) { request =>
		Ok("Body parts %d\r\n".format(request.body))
	}
}