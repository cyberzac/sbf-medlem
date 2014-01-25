package controllers

import play.api.mvc.{Action, Controller}
import model.Member

/**
 *
 */
object Api extends Controller {

  import play.api.libs.json.Json

  def list() = Action {
    Ok(Json.toJson(Member.findAll))
      //.map { t=>
     // (t.email, t.name)
   //  }
 // toMap))
  }

}
