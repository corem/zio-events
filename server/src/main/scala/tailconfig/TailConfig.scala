package tailconfig

import zio.*
import zio.config.*
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.*

case class TailConfig(port: Int, bootstrapServers: List[String], groupId: String, topics: List[String])

object TailConfig {
  val live: Layer[Config.Error, TailConfig] =
    ZLayer.fromZIO(
      ZIO.config[TailConfig](config).tap { config =>
        ZIO.logInfo(
          s"""
             |tail server configuration:
             |port: ${config.port}
             |bootstrap-servers: ${config.bootstrapServers.mkString(",")}
             |group-id: ${config.groupId}
             |topics: ${config.topics.mkString(",")}
             |""".stripMargin)
      }
    )
  private val config: Config[TailConfig] = deriveConfig[TailConfig].nested("tail")
}