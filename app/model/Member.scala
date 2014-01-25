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
//                   id: Option[Pk[Long]],
                   email: String,
                   name: String,
                   address: String,
                   zip: String,
                   city: String,
                   comment: String,
                   createdAt: Long = DateTime.now.getMillis,
                   approved: Boolean = false
                   )  {

  def createdDateTime: DateTime = new DateTime(createdAt)

}

object Member {

  def apply(f: MemberForm) = new Member(f.epost, f.name, f.address, f.zip, f.city, f.comment)

  val simple = {
//    get[Pk[Long]]("member.id") ~
      get[String]("member.email") ~
      get[String]("member.name") ~
      get[String]("member.address") ~
      get[String]("member.zip") ~
      get[String]("member.city") ~
      get[String]("member.comment") ~
      get[DateTime]("member.created_date") ~
      get[Boolean]("member.approved") map {
      case email ~ name ~ address ~ zip ~ city ~ comment ~ created ~ approved => {
        new Member(
//          Some(id),
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
  }

  def create(memberForm:MemberForm):Member = create(Member(memberForm))

  def create(member: Member): Member = {
    DB.withConnection {
      implicit connection =>

        SQL(
          """
            insert into member values (
              {email}, {name}, {address}, {zip}, {city}, {comment}, {created_date}, {approved}
            )
          """
        ).on(
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
        member // Todo read back id.
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

  def searchForEmail(email: String): List[Member] = {
    DB.withConnection {
      implicit connection =>
        SQL("select * from member where email like {email} ").on(
          'email ->  s"%${email}%"
        ).as(Member.simple *)
    }
  }

  def searchAll(what: String): List[Member] = {
    searchForEmail(what)
  }



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
