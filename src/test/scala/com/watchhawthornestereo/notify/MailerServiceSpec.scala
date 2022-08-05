package com.watchhawthornestereo.notify

import com.watchhawthornestereo.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class MailerServiceSpec extends PlaySpec with GuiceOneAppPerSuite {

  "MailerService" must {
    "mail!" in {
      val mailerService: MailerService = app.injector.instanceOf[MailerService]
      mailerService.sendEmail("hi")
    }
  }
}
