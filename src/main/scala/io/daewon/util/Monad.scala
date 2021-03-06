package io.daewon.util

import scala.language.higherKinds

trait Monad[+M, Cont[_]] {
  def unit[A](a: A): Cont[A]

  def flatMap[A](f: M => Cont[A]): Cont[A]
}
