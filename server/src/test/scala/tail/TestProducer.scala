package tail

import com.dimafeng.testcontainers.KafkaContainer
import zio.*
import zio.kafka.producer.{Producer, ProducerSettings}
import tailconfig.*

object TestProducer {

  val live: RLayer[Scope & TailConfig, Producer] =
    ZLayer {
      for {
        bootstrapServers <- ZIO.serviceWith[TailConfig](_.bootstrapServers)
        producer <- Producer.make(ProducerSettings(bootstrapServers))
      } yield producer
    }
}
