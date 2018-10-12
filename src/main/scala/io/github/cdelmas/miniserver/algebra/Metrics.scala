package io.github.cdelmas.miniserver.algebra

trait Metrics[F[_]] {

  def incrementCounter(counter: String): F[Unit]
}
