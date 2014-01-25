package controllers

import play.api.Play.current
import play.api.mvc._
import model.{Member, MemberForm}
import play.api.db._
import anorm._
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

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

  def newMember = Action {
    Ok {
      views.html.register(form)
    }
  }

  def save = Action {
    implicit request =>
      val memberForm: MemberForm = form.bindFromRequest.get

      if (!memberForm.approved)
        NotAcceptable(views.html.error("Du måste acceptera stadgarna"))
      else {
        try {
        val member = Member.create(memberForm)
        Ok(views.html.registered("Du är anmäld", member))
        } catch {
          case e:MySQLIntegrityConstraintViolationException =>   NotAcceptable(views.html.error("Email finns redan"))
        }
      }
  }

  def list = Action {
    Ok(views.html.list(Member.findAll))
  }

  def search(what:String) = Action {
    Ok(views.html.list(Member.searchAll(what)))
  }

}