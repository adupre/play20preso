package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.ws.WS
import models.Location

object Locator extends Controller {
  val geocodeAPI = "http://maps.googleapis.com/maps/api/geocode/json"
	val locationAPI = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
	
	val API_KEY = "AIzaSyC5SHKpl4RhQgIo_CGXFUYMnuRjry7BWrs"

  def locate(what: String, near: String) = Action { implicit request =>
    Async {
			WS.url(geocodeAPI).withQueryString(
				"address" -> near,
				"sensor" -> "true"
			).get().map { r1 =>
				val locr = (r1.json \ "results")(0)
				val formatted = (locr \ "formatted_address").as[String]
				val loc = locr \ "geometry" \ "location"
				
				Async {
					WS.url(locationAPI).withQueryString(
						"location" -> "%f,%f".format((loc \ "lat").as[Float], (loc \ "lng").as[Float]),
						"radius" -> "500", // in meters
						"sensor" -> "false",
						"key" -> API_KEY,
						"name" -> what
					).get().map { r2 =>
						Ok(views.html.location(json2loc(r2.json)))
					}
				}
      }
    }
  }


	private def json2loc(loc: JsValue) =
		(loc \ "results").asInstanceOf[JsArray].value.map { item =>
			Location(
				name = (item \ "name").as[String],
				icon = (item \ "icon").as[String],
				open = (item \ "opening_hours" \ "open_now").asOpt[Boolean].getOrElse(false),
				vincinity = (item \ "vicinity").as[String]
			)
		}
}
