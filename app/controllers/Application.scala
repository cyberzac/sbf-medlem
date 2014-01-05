package controllers

import play.api._
import play.api.mvc._
import model.Member

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }


  import play.api.data._
  import play.api.data.Forms._

  val form = Form(
    mapping(
      "replyto" -> text,
      "namn" -> text,
      "adress" -> text,
      "postnummer" -> text,
      "postort" -> text,
      "comments" -> text,
      "jag-godkanner-foreneinges-stadgar" -> boolean
    ) (Member.apply)(Member.unapply)
  )

  def register = Action {
    Ok { views.html.register(form)}
  }

  def submit = Action {  implicit request =>
    val member: Member = form.bindFromRequest.get
    Ok(views.html.registered("Du är anmäld ", member))
  }

}