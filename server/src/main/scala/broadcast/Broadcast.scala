package broadcast

import message.Message
import zio.*
import zio.stm.*
import zio.stream.{ZSink, ZStream}

trait Broadcast {
  def subscribe(topic: String): Task[TDequeue[Message]]

  def unsubscribe(topic: String): UIO[Unit]
}