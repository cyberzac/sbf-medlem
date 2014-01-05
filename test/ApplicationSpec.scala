import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }

    "render the registration page" in new WithApplication{
      val home = route(FakeRequest(GET, "/register")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Medlemsanmälan")
    }

    "Return Ok  when registering a new member" in new WithApplication {

      val Some(result) = route(
        FakeRequest(POST, "/submit").withFormUrlEncodedBody(
          ("replyto", "zac@cyberzac.se"),
          ("namn", "Martin Zachrison"),
          ("adress", "Prinsessvägen 3A"),
          ("postnummer", "131 46"),
          ("postort", "Nacka"),
          ("jag-godkanner-foreningens-stadgar", "true"),
          ("comments", "comments")
        ))

      status(result) must equalTo(OK)
    }

  }
}
