package io.daewon.util

trait Functor[A, M[_]] {
  def map[B](f: A => B): M[B]
}
