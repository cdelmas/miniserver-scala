package io.github.cdelmas.miniserver.interpreter

import cats.Applicative
import io.github.cdelmas.miniserver.algebra.Metrics
import kamon.metric.CounterMetric

class KamonMetrics[F[_]](val counters: Map[String, CounterMetric])(implicit F: Applicative[F]) extends Metrics[F] {

  override def incrementCounter(counter: String): F[Unit] = F.pure(counters(counter).increment())
}
