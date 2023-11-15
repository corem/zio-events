package consumer

import zio.*
import zio.stream.*
import message.*

trait TailConsumer {
  val consume: Stream[Throwable, Message]
}

object TailConsumer {
  val consume: URIO[TailConsumer, Stream[Throwable, Message]] =
    ZIO.serviceWith[TailConsumer](_.consume)
}
