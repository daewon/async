package io.daewon.async

import io.daewon.util.Monad

import scala.util._

trait Future[+A] extends Monad[A, Future] {
  def isCompleted: Boolean

  def value: Option[Try[A]]

  // addCallback
  def onComplete[U](f: Try[A] => U): Unit

  def onSuccess[U](f: A => U): Unit = onComplete {
    case Success(a) => f(a)
    case _ =>
  }

  def onFailure[U](f: Throwable => U): Unit = onComplete {
    case Failure(ex) => f(ex)
    case _ =>
  }
}
