package model

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._

/**
 *
 */
class MemberSpec extends Specification with WithTestDatabase {

  val kalle: Member = Member("kalle@email.com", "Kalle Eriksson", "Prinsgatan 11", "110 10", "Stockholm", "Yepp")
  val palle: Member = Member("palle@email.com", "Palle Berg", "Storgatan 11", "130 10", "Stockholm", "Sinnes")
  val nina: Member = Member("nina@gmail.com", "Nina Grusberg", "Pl 11333", "930 10", "Grusträsk", "Hej")

  "have a create method" in running(FakeApplication()) {
    val expected = Member.create(kalle)
    expected must_== kalle
  }

  "have a findByEmail method" in running(FakeApplication()) {
    setupSearch
    val expected: Option[Member] = Member.findByEmail("kalle@email.com")
    expected must_== Some(kalle)
  }

  "have a findAll method" in running(FakeApplication()) {
    setupSearch
    val expected: List[Member] = Member.findAll
    expected must_== List(kalle, nina, palle)
  }

  "have a searchByEmail method" in running(FakeApplication()) {
    setupSearch
    Member.searchByEmail("kalle").size must_== 1
    Member.searchByEmail("email.com").size must_== 2
    Member.searchByEmail("nomail").size must_== 0
  }

  def setupSearch = {
    Member.create(kalle)
    Member.create(palle)
    Member.create(nina)
  }
}
