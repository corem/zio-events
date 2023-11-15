package tail

import sttp.client3.*
import sttp.client3.httpclient.zio.SttpClient
import sttp.model.StatusCode
import zio.*
import tailconfig.*

object Tail {

  val live: RLayer[TailConfig & SttpClient, Unit] =
    ZLayer.scoped {
      ZIO
        .acquireRelease(
          for {
            config <- ZIO.service[TailConfig]
            sttp <- ZIO.service[SttpClient]
            program <-
              Main.program(config = ZLayer.succeed(config)).forkScoped.interruptible
            _ <-
              sttp
                .send(
                  basicRequest
                    .get(uri"http://localhost:${config.port}/tail/health")
                    .response(asString)
                )
                .catchAll(_ => ZIO.succeed(ServiceUnavailable))
                .repeatUntil(_.code.isSuccess)
            _ <- ZIO.logInfo(s"Tail started on port ${config.port}")
          } yield program
        )(program => program.interrupt *> ZIO.logInfo("Tail stopped"))
        .unit
    }

  private val ServiceUnavailable: Response[Either[String, String]] =
    Response(Left("Tail unavailable"), StatusCode.ServiceUnavailable)
}
