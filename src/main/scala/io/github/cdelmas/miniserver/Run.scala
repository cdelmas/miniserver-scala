package io.github.cdelmas.miniserver

import akka.actor.{ActorSystem, Props}
import com.rabbitmq.client.{Address, ConnectionFactory}
import com.spingo.op_rabbit._
import github.gphat.censorinus.StatsDClient

import scala.concurrent._

object Run extends App {
  println("hello")

  implicit val executionContext: ExecutionContext = ExecutionContext.Implicits.global
  implicit val actorSystem: ActorSystem = ActorSystem("miniserver-actor")
  implicit val recoveryStrategy: RecoveryStrategy = RecoveryStrategy.limitedRedeliver()

  val rabbitmqHost: String = System.getenv("RABBITMQ_HOST")

  val connectionParams = ConnectionParams(
    hosts = List(new Address(rabbitmqHost, ConnectionFactory.DEFAULT_AMQP_PORT)),
    username = "guest",
    password = "guest"
  )
  val rabbitControl = actorSystem.actorOf(Props { new RabbitControl(connectionParams) })

  val statsdClient = new StatsDClient("localhost", 9125)

  val subscriptionRef = Subscription.run(rabbitControl) {
    import com.spingo.op_rabbit.Directives._
    channel(qos = 3) {
      val e = Exchange.direct("myExchange")
      val q = Queue("myQueue")
      val b = Binding.direct(q, e, List("myKey"))
      consume(b) {
        (body(as[String]) & routingKey) { (msg, key) =>
          if (msg contains "kovfefe") statsdClient.increment("janedoe.miniserversc.message.nok") else statsdClient.increment("janedoe.miniserversc.message.ok")
          /* do work; this body is executed in a separate thread, as
             provided by the implicit execution context */
          println(s"""received $msg over '$key'.""")
          ack
        }
      }
    }
  }
}
