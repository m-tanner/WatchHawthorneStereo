package com.watchhawthornestereo.notify

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.mailer._

import javax.inject.Inject


class MailerService @Inject()(mailerClient: MailerClient) extends LazyLogging {

  def sendEmail(s: String): String = {
    val cid = "1234"
    val email = Email(
      "Simple email",
      "FROM <watchhawthornestereo@gmail.com>",
      Seq("TO <tannman11@gmail.com>"),
      bodyText = Some(s"$s"),
      bodyHtml = Some(s"""<html><body><p>An <b>html</b> message with cid <img src="cid:$cid"> and message $s</p></body></html>""")
    )
    mailerClient.send(email)
  }
}

object MailerService {
  def apply(mailerClient: MailerClient) = new MailerService(mailerClient)
}
