package io.daewon.async

import scala.concurrent.ExecutionContext
import scala.util._

object Promise {
  def apply[A]()(implicit ec: ExecutionContext) = new Promise[A]()(ec)
}

class Promise[A]()(implicit ec: ExecutionContext) {
  private lazy val internalFuture = new FutureImpl[A]

  object status {
    @volatile var result: Try[A] = null
  }

  def isCompleted: Boolean = status.result != null

  def value: Try[A] =
    if (this.isCompleted) status.result
    else throw new IllegalStateException("This promise is not completed yet")

  def complete(_result: Try[A]): this.type =
    if (!tryComplete(_result)) throw new IllegalStateException("Promise already completed")
    else this

  def tryComplete(result: Try[A]): Boolean = {
    if (result == null) throw new IllegalStateException("result can't be null")

    if (isCompleted) false
    else synchronized {
      this.status.result = result
      this.internalFuture.complete(result)
      true
    }
  }

  def success(value: A): this.type = complete(Success(value))

  def failure(ex: Throwable): this.type = complete(Failure(ex))

  def future: Future[A] = internalFuture
}
