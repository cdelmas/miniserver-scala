package io.github.cdelmas.miniserver

import cats.effect.{ExitCode, IO, IOApp}
import com.github.gvolpe.fs2rabbit.config.Fs2RabbitConfig
import com.github.gvolpe.fs2rabbit.interpreter.Fs2Rabbit
import com.github.gvolpe.fs2rabbit.resiliency.ResilientStream
import io.github.cdelmas.miniserver.interpreter.KamonMetrics
import kamon.Kamon
import kamon.statsd.StatsDReporter

import scala.Function.const

object Run extends IOApp {

  Kamon.addReporter(new StatsDReporter)

  private val metrics = new KamonMetrics[IO](
    Map(
      "janedoe.miniserversc.message.nok" -> Kamon.counter("janedoe.miniserversc.message.nok"),
      "janedoe.miniserversc.message.ok" -> Kamon.counter("janedoe.miniserversc.message.ok")
    )
  )

  private val config: Fs2RabbitConfig = Fs2RabbitConfig(virtualHost = "/",
    host = sys.env("RABBITMQ_HOST"),
    username = Some("guest"),
    password = Some("guest"),
    port = 5672,
    ssl = false,
    sslContext = None,
    connectionTimeout = 3,
    requeueOnNack = false)

  implicit val fs2rabbit: Fs2Rabbit[IO] = Fs2Rabbit[IO](config)

  override def run(args: List[String]): IO[ExitCode] = ResilientStream.run(
    new KovfefeConsumer[IO](RabbitMqBinding("myExchange", "myKey", "myQueue"), metrics).program
  ).map(const(ExitCode.Success))

}
