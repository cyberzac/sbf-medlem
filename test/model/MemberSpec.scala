package model

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._
import anorm.Pk


/**
 *
 */
class MemberSpec extends Specification {

    val member: Member = Member("email", "name", "address", "zip", "city", "comment")

  "have a create method" in running(FakeApplication()) {
     val expected = Member.create(member)
    expected.email must_== "email"
  }



}
