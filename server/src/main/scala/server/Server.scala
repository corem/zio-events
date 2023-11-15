package server

import zio.*

import scala.concurrent.ExecutionContext

trait Server {
  val serve: Task[ExitCode]
}

object Server {
  val serve: ZIO[Server, Throwable, ExitCode] = ZIO.serviceWithZIO[Server](_.serve)
}