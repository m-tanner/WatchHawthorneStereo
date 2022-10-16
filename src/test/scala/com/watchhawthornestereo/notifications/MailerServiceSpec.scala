package com.watchhawthornestereo.notifications

import com.watchhawthornestereo.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class MailerServiceSpec extends PlaySpec with GuiceOneAppPerSuite {

  "MailerService" must {
    "mail!" in {
      val mailerService: MailerService = app.injector.instanceOf[MailerService]
      mailerService.sendEmail(
        "myintegrationtests@gmail.com",
        "{\"listings\":[{\"link\":\"https://www.hawthornestereo.com/rega-dac-used.html\",\"pubDate\":\"Sun, 09 Oct 2022 19:25:10 +0000\",\"title\":\"Rega DAC USED\",\"img\":\"https://cdn.shoplightspeed.com/shops/635168/files/49175063/75x75x2/image.jpg\",\"price\":\"$539.00\",\"description\":\"A Hawthorne favorite! Great sounding DAC. Capable of hi-res playback on the coax and optical inputs, and 48/24 on the USB input. 8/10 from ~2013.\"},{\"link\":\"https://www.hawthornestereo.com/cambridge-audio-axr100-receiver-used.html\",\"pubDate\":\"Sun, 09 Oct 2022 19:23:07 +0000\",\"title\":\"Cambridge Audio AXR100 Receiver USED\",\"img\":\"https://cdn.shoplightspeed.com/shops/635168/files/49175037/75x75x2/image.jpg\",\"price\":\"$559.00\",\"description\":\"Lightly used 100-watt receiver with DAC, phono input, and remote control. 9/10 from 2021.\"},{\"link\":\"https://www.hawthornestereo.com/b-w-cm9-floorstanding-speakers-used.html\",\"pubDate\":\"Sun, 09 Oct 2022 19:22:09 +0000\",\"title\":\"B&W CM9 Floorstanding Speakers USED\",\"img\":\"https://cdn.shoplightspeed.com/shops/635168/files/49247843/75x75x2/image.jpg\",\"price\":\"$1,289.00\",\"description\":\"Gloss black towers from renowned UK speaker manufacturer Bowers & Wilkins. 8/10 from 2010. Store pickup only.\"},{\"link\":\"https://www.hawthornestereo.com/sumiko-s5-subwoofer-black-used.html\",\"pubDate\":\"Sun, 09 Oct 2022 19:20:07 +0000\",\"title\":\"Sumiko S.5 Subwoofer Black USED\",\"img\":\"https://cdn.shoplightspeed.com/shops/635168/files/49175002/75x75x2/image.jpg\",\"price\":\"$389.00\",\"description\":\"Quality subwoofer with 8\\\" downfiring woofer and 150-watt amplifier. Includes 10-meter Neutrik Speakon cable. 8/10 from 2018.\"}]}",
      )
    }
  }

}
