package buffer

import message.Message
import zio.{Queue, UIO, ULayer, ZIO, ZLayer}

final case class BufferImpl(queue: Queue[Message]) extends Buffer {
  override def offer(message: Message): UIO[Unit] =
    queue.offer(message).unit *> ZIO.logInfo(s"Buffer message ${message.id}")

  override def poll(): UIO[Option[Message]] = queue.poll
}

object BufferImpl {
  val live: ULayer[Buffer] =
    ZLayer(Queue.unbounded[Message].map(BufferImpl(_)))
}