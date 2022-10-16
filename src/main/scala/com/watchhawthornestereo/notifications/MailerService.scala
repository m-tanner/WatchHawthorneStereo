package com.watchhawthornestereo.notifications

import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.hawthorne.HawthorneModelUtils
import play.api.libs.mailer._

import javax.inject.Inject

class MailerService @Inject() (mailerClient: MailerClient) extends LazyLogging with HawthorneModelUtils {

  def sendEmail(to: String, listings: String): String = {
    val email = Email(
      subject = "An Update From Hawthorne Stereo",
      from = "watchhawthornestereo@gmail.com",
      to = List(to),
      bodyText = Some(s"$listings"),
      bodyHtml = Some(html.EmailTemplate(asList(listings)).body),
      replyTo = List("tannman11@gmail.com"),
    )

    mailerClient.send(email)
  }

}

object MailerService {
  def apply(mailerClient: MailerClient) = new MailerService(mailerClient)
}
