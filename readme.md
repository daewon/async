[future and promise](http://docs.scala-lang.org/overviews/core/futures.html)


“I heard you like callbacks, so I put a callback in your callback!

“I know Futures, and they are completely useless!”

What is future, promise

Futures
A Future is an object holding a value which may become available at some point.
This value is usually the result of some other computation

If the computation has not yet completed, we say that the Future is not completed.
If the computation has completed with a value or with an exception, we say that the Future is completed.

non-blocking parallel execution

how to combinate each other
    - sum(n)
    - max(n)

callback hell.


```
    val f1 = f ..
    val f2 = f ..
    val f3 = f ..

    f1 onComplete {
        case Success(s1) => f2 onComplete {
            case Success(s2) => f3 onComplete {
                case Success(s3) => doSomething(s1, s2, s3)
            }
        }
    }

```

monad to rescue (next section)
    - None.map
    - Failure.map
    - Failure.flatMap
    - Failure.get
    - Monad.scala (higher-kind type)

# introduce option monad
# make future as monad

in Try or Option
    - tr.get error
    - tr.map

You will have noticed that Future[T] is success-biased

error case, res is Future.failure

```
val session = null
val f: Future[List[Friend]] = Future {
  session.getFriends
}

```

Functional Composition and For-Comprehensions
    - flatMap
    - map

statement to value , value to composable, statement is not

# future cache
on complete add callback!
