package consumer

import zio.*
import zio.kafka.consumer.Subscription.Topics
import zio.kafka.consumer.{Consumer, ConsumerSettings}
import zio.kafka.serde.Serde
import zio.stream.*
import message.*
import tailconfig.*
import consumer.*

final case class TailConsumerImpl(topics: Set[String], consumer: Consumer) extends TailConsumer {
  override val consume: Stream[Throwable, Message] =
    consumer
      .plainStream[Any, Array[Byte], Array[Byte]](
        Topics(topics),
        Serde.byteArray,
        Serde.byteArray
      ).map(record =>
      Message(
        record.offset.topicPartition.topic(),
        record.partition,
        record.offset.offset,
        record.key,
        record.value
      )
    )
}

object TailConsumerImpl {
  val live: RLayer[TailConfig, TailConsumer] =
    ZLayer.scoped {
      for {
        config <- ZIO.service[TailConfig]
        topics = config.topics.toSet
        consumer <- Consumer.make(
          ConsumerSettings(config.bootstrapServers)
            .withGroupId(config.groupId)
        )
      } yield TailConsumerImpl(topics, consumer)
    }
}
