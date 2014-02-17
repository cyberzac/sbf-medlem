package controllers

import play.api.mvc.{Action, Controller}
import model.Member
import anorm.Pk

/**
 *
 */
object Api extends Controller {

  import play.api.libs.json.Json

  def list() = Action {
    Ok(Json.toJson(Member.findAll))
  }

  def verify(id: Pk[Long]) = Action {
    val member = Member.findById(id)
    if (member.isDefined)
      Ok(Json.toJson(Member.verify(member.get)))
    else
      NotFound
  }

}
