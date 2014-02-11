package model

import org.joda.time.DateTime
import play.api.db.DB
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.Logger
import AnormExtension._

/**
 *
 */
case class Member(
                   id: Option[Pk[Long]],
                   email: String,
                   name: String,
                   address: String,
                   zip: String,
                   city: String,
                   comment: String,
                   createdAt: Long = DateTime.now.millisOfSecond().setCopy(0).getMillis, // Remove the millis
                   approved: Boolean = false
                   ) {

  def createdDateTime: DateTime = new DateTime(createdAt)

}

object Member {

  def apply(f: MemberForm) = new Member(None, f.epost, f.name, f.address, f.zip, f.city, f.comment)

  val simple = {
    get[Pk[Long]]("member.id") ~
      get[String]("member.email") ~
      get[String]("member.name") ~
      get[String]("member.address") ~
      get[String]("member.zip") ~
      get[String]("member.city") ~
      get[String]("member.comment") ~
      get[DateTime]("member.created_date") ~
      get[Boolean]("member.approved") map {
      case id ~ email ~ name ~ address ~ zip ~ city ~ comment ~ created ~ approved =>
        new Member(
          Some(id),
          email,
          name,
          address,
          zip,
          city,
          comment,
          created.getMillis,
          approved
        )
    }
  }

  def create(memberForm: MemberForm): Member = create(Member(memberForm))

  def create(member: Member): Member = {
    DB.withConnection {
      implicit connection =>

        SQL(
          """
            insert into member values (
              {id}, {email}, {name}, {address}, {zip}, {city}, {comment}, {created_date}, {approved}
            )
          """
        ).on(
            'id -> member.id,
            'email -> member.email,
            'name -> member.name,
            'address -> member.address,
            'zip -> member.zip,
            'city -> member.city,
            'comment -> member.comment,
            'created_date -> member.createdDateTime,
            'approved -> member.approved
          ).executeUpdate()
        Logger.debug("Stored member {}", member)
        findByEmail(member.email).get
    }
  }

  //  def findById(id: Pk[Long]): Option[Member] = {
  //    DB.withConnection {
  //      implicit connection =>
  //        SQL("select * from member where id = {id} ").on(
  //          'id -> id
  //        ).as(Member.simple.singleOpt)
  //    }
  //  }

  //  def update(member:Member) = {
  //    DB.withConnection {
  //      implicit connection =>
  //
  //        SQL(
  //          """
  //            update  member set (
  //              {email}, {name}, {address}, {zip}, {city}, {comment}, {created_date}, {approved}
  //            )
  //          """
  //        ).on(
  //            'email -> member.email,
  //            'name -> member.name,
  //            'address -> member.address,
  //            'zip -> member.zip,
  //            'city -> member.city,
  //            'comment -> member.comment,
  //            'created_date -> member.createdDateTime,
  //            'approved -> member.approved
  //          ).executeUpdate()
  //        Logger.debug("Updated member {}", member)
  //        member // Todo read back id.
  //    }
  //  }

  def findByEmail(email: String): Option[Member] = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from member where email = {email} ").on(
          'email -> email
        ).as(Member.simple.singleOpt)
    }
  }

  def findAll: scala.List[Member] = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from member").as(Member.simple *)
    }
  }

  def searchByEmail(email: String): List[Member] = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from member where email like {email} ").on(
          'email -> s"%$email%"
        ).as(Member.simple *)
    }
  }

  def searchAll(what: String): List[Member] = {
    searchByEmail(what)
  }

  import play.api.libs.json._
  import play.api.libs.functional.syntax._

  /**
   * Serializer for Int types.
   */
  implicit object OptionPkLongWrites extends Writes[Option[Pk[Long]]] {
    def writes(o: Option[Pk[Long]]) = o match {
      case Some(Id(v)) => JsNumber(v)
      case None => JsNull
    }
  }


  implicit val memberWrites: Writes[Member] = (
    (__ \ 'id).write[Option[Pk[Long]]] and
      (__ \ 'email).write[String] and
      (__ \ 'name).write[String] and
      (__ \ 'address).write[String] and
      (__ \ 'zip).write[String] and
      (__ \ 'city).write[String] and
      (__ \ 'comments).write[String] and
      (__ \ 'created_date).write[Long] and
      (__ \ 'approved).write[Boolean]
    )(unlift(Member.unapply))

}

case class MemberForm(
                       epost: String,
                       name: String,
                       address: String,
                       zip: String,
                       city: String,
                       comment: String,
                       approved: Boolean
                       )
