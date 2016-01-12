package io.daewon.async

import scala.concurrent.ExecutionContext
import scala.util._

class FutureImpl[A]()(implicit ec: ExecutionContext) extends Future[A] {
  type FutureCallback = Try[A] => Any

  object status {
    @volatile var value: Try[A] = null
    var callbacks = Vector.empty[FutureCallback]
  }

  def complete(_value: Try[A]): Unit = _value match {
    case null => throw new IllegalStateException("A Future can't be completed with null")
    case _ =>
      if (this.isCompleted) throw new IllegalStateException("Promise already completed.")
      synchronized {
        status.value = _value
        fireCallbacks()
      }
  }

  override def onComplete[U](f: Try[A] => U): Unit = this.status.synchronized {
    this.isCompleted match {
      case true => fireCallback(f)
      case false => status.callbacks = f +: status.callbacks
    }
  }

  private def fireCallback(futureCallback: FutureCallback): Unit = {
    ec.execute(new Runnable {
      override def run(): Unit = futureCallback(status.value)
    })
  }

  private def fireCallbacks(): Unit = {
    status.callbacks.foreach(fireCallback)
    status.callbacks = Vector.empty[FutureCallback]
  }

  override def isCompleted: Boolean = status.value != null

  override def value: Option[Try[A]] = this.isCompleted match {
    case true => Option(status.value)
    case false => None
  }

  override def unit[A](a: A): Future[A] = new FutureImpl[A]()

  override def flatMap[B](f: A => Future[B]): Future[B] = {
    val p = Promise[B]()(ec)
    this onComplete {
      case Success(v) => f(v) onComplete p.complete
      case Failure(ex) => p.failure(ex)
    }
    p.future
  }

  override def map[B](f: A => B): Future[B] = {
    val p = Promise[B]()(ec)
    this onComplete {
      v => p complete (v map f)
    }
    p.future
  }
}
