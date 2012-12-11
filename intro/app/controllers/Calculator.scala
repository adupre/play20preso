package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws.WS

object Calculator extends Controller {
  val convertorURI = "http://www.google.com/ig/calculator?hl=ENG"

  def convert(r: String) = Action { implicit request =>
  	Async { // Async result
      WS.url(convertorURI).withQueryString(
        "q" -> r
      ).get().map { response =>
        Ok(Json.toJson(Map(
          "results" ->  (cleanJSON(response) \ "rhs").as[String]
			)))
	    }
	  }
  }

  /* Stupid API doesn't return proper JSON... */
  private def cleanJSON(response: play.api.libs.ws.Response): JsValue = 
	Json.parse(response.body
		.replace("lhs:", "\"lhs\":")
		.replace("rhs:", "\"rhs\":")
		.replace("error:", "\"error\":")
		.replace("icc:", "\"icc\":")
  	)
}
