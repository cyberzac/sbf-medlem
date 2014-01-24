package controllers

import play.api.Play.current
import play.api.mvc._
import model.{Member, MemberForm}
import play.api.db._
import anorm._

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
      "jag-godkanner-foreningens-stadgar" -> boolean
    )(MemberForm.apply)(MemberForm.unapply)
  )

  def register = Action {
    Ok {
      views.html.register(form)
    }
  }

  def submit = Action {
    implicit request =>
      val memberForm: MemberForm = form.bindFromRequest.get

      if (!memberForm.approved)
        NotAcceptable(views.html.error("Du måste acceptera stadgarna"))
      else {
        val member = Member.create(memberForm)
        Ok(views.html.registered("Du är anmäld", member))
      }
  }

}