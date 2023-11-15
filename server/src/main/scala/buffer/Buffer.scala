package buffer

import message.*
import zio.*

trait Buffer {
  def offer(message: Message): UIO[Unit]

  def poll(): UIO[Option[Message]]
}

object Buffer {
  def offer(message: Message): URIO[Buffer, Unit] =
    ZIO.serviceWithZIO[Buffer](_.offer(message))

  def poll(): URIO[Buffer, Option[Message]] =
    ZIO.serviceWithZIO[Buffer](_.poll())
}


