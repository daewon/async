import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Failure, Random, Success, Try}
implicit val ec = ExecutionContext.Implicits.global
val p = Promise[Int]()
val f = p.future
f.onComplete {
  case Success(n) => println(n)
  case Failure(ex) => println(ex.getMessage)
}
if (true || Random.nextBoolean()) p.complete(Try(1))
else p.complete(Try(throw new RuntimeException("Exception")))
// return 10 or failure
Await.result(f, Duration("10 seconds"))
def error(ex: Throwable): Unit = ex
def success(n: Int): Unit = n
val f1 = Future { 1 }
val f2 = Future { 2 }
val f3 = Future { 3 }
f1 onComplete {
  case Success(s1) => f2 onComplete {
    case Success(s2) => f3 onComplete {
      case Success(s3) => success(s1 + s2 + s3)
      case Failure(ex) => error(ex)
    }
    case Failure(ex) => error(ex)
  }
  case Failure(ex) => error(ex)
}

val fr2 = f1 flatMap { s1 =>
  f2 flatMap { s2 =>
    f3 map { s3 =>
      s1 + s2 + s3
    }
  }
}

val fr = for {
  s1 <- f1
  s2 <- f2
  s3 <- f3
} yield s1 + s2 + s3

fr onComplete {
  case Success(s) => success(s)
  case Failure(ex) => error(ex)
}


