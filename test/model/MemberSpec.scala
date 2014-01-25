package model

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._

/**
 *
 */
class MemberSpec extends Specification with WithTestDatabase {

  val member: Member = Member("test@email.com", "Test Testson", "address", "zip", "city", "comment")
  val member2: Member = Member("test2@email.com", "Test2 Test Testson", "address2", "zip2", "city2", "comment2")

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

 "have a findAll method" in running(FakeApplication()) {
    Member.create(member)
    Member.create(member2)
    val expected: List[Member] = Member.findAll
    expected must_== List(member, member2)
  }

 "have a searchForEmail method" in running(FakeApplication()) {
    setupSearch
    Member.searchForEmail("kalle").size must_== 1
    Member.searchForEmail("email.com").size must_== 2
    Member.searchForEmail("nomail").size must_== 0
  }

def setupSearch = {
  Member.create(Member("kalle@email.com", "Kalle Eriksson", "Prinsgatan 11", "110 10", "Stockholm", "Yepp"))
  Member.create(Member("palle@email.com", "Palle Berg", "Storgatan 11", "130 10", "Stockholm", "Sinnes"))
  Member.create(Member("nina@gmail.com", "Nina Grusberg", "Pl 11333", "930 10", "Grusträsk", "Hej"))
}
}
