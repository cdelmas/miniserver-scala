package io.github.cdelmas.miniserver.interpreter

import cats.Applicative
import github.gphat.censorinus.StatsDClient
import io.github.cdelmas.miniserver.algebra.Metrics

class CensorinusMetrics[F[_] : Applicative](statsDClient: StatsDClient)(implicit F: Applicative[F]) extends Metrics[F] {
  override def incrementCounter(counter: String): F[Unit] = F.pure(statsDClient.increment(counter))
}
