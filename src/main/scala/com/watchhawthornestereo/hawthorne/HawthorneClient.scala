package com.watchhawthornestereo.hawthorne

import com.watchhawthornestereo.{Client, Settings}
import org.json4s.DefaultFormats
import org.json4s.native.{Json => Json4s}
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import play.api.libs.ws.WSClient

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters._
import scala.language.{implicitConversions, postfixOps}

class HawthorneClient @Inject()(ws: WSClient, settings: Settings)(implicit ec: ExecutionContext)
  extends Client(ws, settings)(ec) {

  def findNewListings(): String = {
    val listings = fetchRssListings()

    val hydratedListings = for (l <- listings) yield fetchHtmlListing(l)

    val unifiedListings = for ((rss, html) <- listings.zip(hydratedListings)) yield mergeListings(rss, html)

    Json4s(DefaultFormats).write("listings" -> unifiedListings)
  }

  private def fetchRssListings(): List[RssListing] = {
    val doc = Jsoup.connect(settings.hawthorneRss).get()
    val elements = doc.select("item").asScala
    val listings = for (e <- elements)
      yield RssListing(
        link = e.select("link").text(),
        pubDate = e.select("pubDate").text(),
        title = e.select("title").text(),
        img = scrapeImageSourceFromDescription(e),
      )

    listings.toList
  }

  private def scrapeImageSourceFromDescription(es: Element): String = {
    val description = es.select("description")
    val asText = description.text()
    val asHtml = Jsoup.parse(asText)
    asHtml.select("img").attr("src")
  }

  private def fetchHtmlListing(rss: RssListing): HtmlListing = {
    val doc = Jsoup.connect(rss.link).get()
    val price = doc.select(".price").text()
    val description = doc.select("p").text()
    HtmlListing(price = price, description = description)
  }

  private def mergeListings(rss: RssListing, html: HtmlListing): UnifiedListing = {
    UnifiedListing(
      link = rss.link,
      pubDate = rss.pubDate,
      title = rss.title,
      img = rss.img,
      price = html.price,
      description = html.description
    )
  }
}

object HawthorneClient {
  def apply(ws: WSClient, settings: Settings)(ec: ExecutionContext) = new HawthorneClient(ws, settings)(ec)
}
