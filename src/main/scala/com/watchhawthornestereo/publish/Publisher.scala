package com.watchhawthornestereo.publish

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.alpakka.googlecloud.pubsub.scaladsl.GooglePubSub
import akka.stream.alpakka.googlecloud.pubsub.{PubSubConfig, PublishMessage, PublishRequest}
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.scalalogging.LazyLogging
import com.watchhawthornestereo.Settings

import java.util.Base64
import javax.inject.Inject
import scala.concurrent.ExecutionContext.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.postfixOps

class Publisher @Inject()(settings: Settings) extends LazyLogging {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = global

  private val config = PubSubConfig()
  private val topic = settings.topic

  def publish(message: String): Unit = {
    val publishMessage = PublishMessage(new String(Base64.getEncoder.encode(message.getBytes)))
    val publishRequest = PublishRequest(Seq(publishMessage))

    val source: Source[PublishRequest, NotUsed] = Source.single(publishRequest)

    val publishFlow: Flow[PublishRequest, Seq[String], NotUsed] = GooglePubSub.publish(topic, config)

    val publishedMessageIds: Future[Seq[Seq[String]]] = source.via(publishFlow).runWith(Sink.seq)

    Await.result(publishedMessageIds, 10 seconds)
    publishedMessageIds.foreach(id => logger.info(s"Successfully published ${id.mkString} to $topic"))
  }
}

object Publisher {
  def apply(settings: Settings) = new Publisher(settings)
}
