package tail

import com.dimafeng.testcontainers.KafkaContainer
import zio.*
import tailconfig.*

object TestTailConfig {

  val Topic1: String = "test-topic-1"
  val Topic2: String = "test-topic-2"
  val Topic3: String = "test-topic-3"

  val live: URLayer[KafkaContainer, TailConfig] =
    ZLayer {
      for {
        bootstrapServers <-
          ZIO.serviceWith[KafkaContainer](_.bootstrapServers.split(',').toList)
        random <- ZIO.random
        port <-
          random.nextIntBetween(9000, 9999)
        config = TailConfig(
          port = port,
          bootstrapServers = bootstrapServers,
          groupId = "tail-server-test",
          topics = List(Topic1, Topic2, Topic3)
        )
      } yield config
    }
}
