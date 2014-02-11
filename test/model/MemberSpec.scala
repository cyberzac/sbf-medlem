package model

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._
import anorm.Id

/**
 *
 */
class MemberSpec extends Specification with WithTestDatabase {

  val kalle: Member = Member(None, "kalle@email.com", "Kalle Eriksson", "Prinsgatan 11", "110 10", "Stockholm", "Yepp")
  val palle: Member = Member(None, "palle@email.com", "Palle Berg", "Storgatan 11", "130 10", "Stockholm", "Sinnes")
  val nina: Member = Member(None, "nina@gmail.com", "Nina Grusberg", "Pl 11333", "930 10", "Grusträsk", "Hej")

  "have a create method" in running(FakeApplication()) {
    val expected = Member.create(kalle)
    expected must_== kalle
  }

  "have a findByEmail method" in running(FakeApplication()) {
    setupMembers
    val expected: Option[Member] = Member.findByEmail("kalle@email.com")
    expected must_== Some(kalle.copy(id=Some(Id(1))))
  }

  "have a findAll method" in running(FakeApplication()) {
    setupMembers
    val expected: List[Member] = Member.findAll
    expected must_== List(kalle.copy(id=Some(Id(1))), palle.copy(id=Some(Id(2))), nina.copy(id=Some(Id(3))))
  }

  "have a searchByEmail method" in running(FakeApplication()) {
    setupMembers
    Member.searchByEmail("kalle").size must_== 1
    Member.searchByEmail("email.com").size must_== 2
    Member.searchByEmail("nomail").size must_== 0
  }
  
//  "have an approve method" in running(FakeApplication()) {
//    setupMembers
//    Member.create(kalle)
//  }

  def setupMembers = {
    Member.create(kalle)
    Member.create(palle)
    Member.create(nina)
  }
}
