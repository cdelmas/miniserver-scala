package io.github.cdelmas.miniserver

import cats.{Applicative, Monad}
import cats.effect.Concurrent
import com.github.gvolpe.fs2rabbit.config.declaration.{DeclarationExchangeConfig, DeclarationQueueConfig}
import com.github.gvolpe.fs2rabbit.interpreter.Fs2Rabbit
import com.github.gvolpe.fs2rabbit.model.AckResult.Ack
import com.github.gvolpe.fs2rabbit.model.ExchangeType.Direct
import com.github.gvolpe.fs2rabbit.model._
import com.github.gvolpe.fs2rabbit.util.StreamEval
import fs2.{Pipe, Stream}
import io.github.cdelmas.miniserver.algebra.Metrics

case class RabbitMqBinding(exchangeName: String, routingKey: String, queueName: String)

class KovfefeConsumer[F[_] : Concurrent : Applicative](binding: RabbitMqBinding, metrics: Metrics[F])(implicit F: Fs2Rabbit[F], A: Applicative[F], SE: StreamEval[F]) {

  private val exchangeName = ExchangeName(binding.exchangeName)
  private val queueName = QueueName(binding.queueName)
  private val routingKey = RoutingKey(binding.routingKey)

  def logPipe: Pipe[F, AmqpEnvelope, AckResult] = { streamMsg =>
    for {
      amqpMsg <- streamMsg
      _ <- SE.evalF[Unit](println(s"Consumed: $amqpMsg"))
      _ <- Stream.eval(A.pure(
        if (amqpMsg.payload contains "kovfefe")
          metrics.incrementCounter("janedoe.miniserversc.message.nok")
        else
          metrics.incrementCounter("janedoe.miniserversc.message.ok")
      )
      )
    } yield Ack(amqpMsg.deliveryTag)
  }

  val program: Stream[F, Unit] = F.createConnectionChannel flatMap { implicit channel =>
    for {
      _ <- F.declareExchange(DeclarationExchangeConfig.default(exchangeName, Direct))
      _ <- F.declareQueue(DeclarationQueueConfig.default(queueName))
      _ <- F.bindQueue(queueName, exchangeName, routingKey)
      ackerAndConsumer <- F.createAckerConsumer(queueName)
      (acker, consumer) = ackerAndConsumer
      result <- consumer through logPipe to acker
    } yield result
  }
}
