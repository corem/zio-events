package tail

import zio.*
import zio.config.*
import zio.config.typesafe.*
import zio.stream.ZSink
import tailconfig.*
import consumer.*
import server.*
import broadcast.*
import buffer.*

object Main extends ZIOAppDefault {

  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.setConfigProvider(ConfigProvider.fromResourcePath())

  override def run: Task[ExitCode] = program(TailConfig.live)

  def program(config: TaskLayer[TailConfig]): Task[ExitCode] =
    (for {
      consume <- TailConsumer.consume
      _ <- consume.mapZIO(Buffer.offer).run(ZSink.drain).forkDaemon
      serve <- Server.serve
    } yield serve).provide(
      config,
      TailConsumerImpl.live,
      BufferImpl.live,
      BroadcastImpl.live,
      ServerImpl.live
    )
}