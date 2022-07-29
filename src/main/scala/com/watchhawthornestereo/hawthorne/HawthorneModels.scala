package com.watchhawthornestereo.hawthorne

final case class RssListing(link: String, pubDate: String, title: String, img: String)

final case class HtmlListing(price: String, description: String)

final case class UnifiedListing(link: String, pubDate: String, title: String, img: String, price: String, description: String)

final case class UnifiedListings(listings: List[UnifiedListing]) {
  def contains(ul: UnifiedListing): Boolean = listings.contains(ul)
}
