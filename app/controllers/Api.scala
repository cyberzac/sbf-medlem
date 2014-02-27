package controllers

import play.api.mvc.{Action, Controller}
import model.Member
import anorm.{Id, Pk}

/**
 *
 */
object Api extends Controller {

  import play.api.libs.json.Json

  def list() = Action {
    Ok(Json.toJson(Member.findAll))
  }
  
  def verify(id:Int) = toJson(id, {Member.verify})

  def get(id:Int) = toJson(id, (m:Member) => m)
  
  private def toJson(id:Int, f :(Member) => Member) =  Action {
    val member = Member.findById(Id(id))
    if (member.isDefined)
      Ok(Json.toJson(f(member.get)))
    else
      NotFound
  }

}
