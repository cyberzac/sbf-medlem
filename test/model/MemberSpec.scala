package model

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._

/**
 *
 */
class MemberSpec extends Specification with WithTestDatabase {

  val member: Member = Member("email", "Test Testson", "address", "zip", "city", "comment")
  val member2: Member = Member("email2", "Test Test Testson", "address2", "zip2", "city2", "comment2")

  "have a create method" in running(FakeApplication()) {
    val expected = Member.create(member)
    expected must_== member
  }

  "have a findByEmail method" in running(FakeApplication()) {
    Member.create(member)
    Member.create(member2)
    val expected: Option[Member] = Member.findByEmail("email")
    expected must_== Some(member)
  }


}
