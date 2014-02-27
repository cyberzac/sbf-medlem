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
                   verified: Boolean = false
                   ) {

  def createdDateTime: DateTime = new DateTime(createdAt)

}

object Member {
  def verify(member: Member): Member = {
    DB.withConnection {
      implicit connection =>
        SQL(
          """
            update  member set verified = true where id = {id}
          """
        ).on(
            'id -> member.id
          ).executeUpdate()
        val verified: Member = findById(member.id.get).get
        Logger.debug("Verified member {}", verified)
        verified
    }
  }


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
      get[Boolean]("member.verified") map {
      case id ~ email ~ name ~ address ~ zip ~ city ~ comment ~ created ~ verified =>
        new Member(
          Some(id),
          email,
          name,
          address,
          zip,
          city,
          comment,
          created.getMillis,
          verified
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
              {id}, {email}, {name}, {address}, {zip}, {city}, {comment}, {created_date}, {verified}
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
            'verified -> member.verified
          ).executeUpdate()
        Logger.debug("Stored member {}", member)
        findByEmail(member.email).get
    }
  }

  def findById(id: Pk[Long]): Option[Member] = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from member where id = {id} ").on(
          'id -> id
        ).as(Member.simple.singleOpt)
    }
  }

  def update(member: Member) = {
    DB.withConnection {
      implicit connection =>

        SQL(
          """
              update member set
                email = {email},
                name = {name},
                address = {address},
                zip = {zip},
                city = {city},
                comment ={comment},
                created_date = {created_date},
                verified = {verified}
              where id = {id}
          """
        ).on(
            'email -> member.email,
            'comment -> member.comment,
            'id -> member.id,
            'name -> member.name,
            'address -> member.address,
            'zip -> member.zip,
            'city -> member.city,
            'comment -> member.comment,
            'created_date -> member.createdDateTime,
            'verified -> member.verified
          ).executeUpdate()
        Logger.debug("Updated member {}", member)
        findById(member.id.get)
    }
  }

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
      case x => JsUndefined(x.toString)
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
      (__ \ 'verified).write[Boolean]
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
