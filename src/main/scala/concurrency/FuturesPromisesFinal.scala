package concurrency

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Futures, Part 4 + Exercises
  *
  */
object FuturesPromisesFinal extends App {

  /*
    1) fulfill a future IMMEDIATELY with a value
    2) inSequence(fa, fb)
    3) first(fa, fb) => new future with the first value of the two futures
    4) last(fa, fb) => new future with the last value
    5) retryUntil[T](action: () => Future[T], condition: T=> Boolean): Future[T]
   */

  // 1 - fulfill immediately
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // 2 - inSequence
  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] =
    first.flatMap(_ => second)

  // 3 - first out of two futures
  {
    def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
      val promise = Promise[A]
      fa.onComplete {
        case Success(r) => try {
          promise.success(r)
        } catch {
          case _ =>
        }
        case Failure(t) => try {
          promise.failure(t)
        } catch {
          case _ =>
        }
      }
      fb.onComplete {
        case Success(r) => try {
          promise.success(r)
        } catch {
          case _ =>
        }
        case Failure(t) => try {
          promise.failure(t)
        } catch {
          case _ =>
        }
      }
      promise.future
    }

    def firstRefactor[A](fa: Future[A], fb: Future[A]): Future[A] = {
      val promise = Promise[A]

      def tryComplete(promise: Promise[A], result: Try[A]) = result match {
        case Success(r) => try {
          promise.success(r)
        } catch {
          case _ =>
        }
        case Failure(t) => try {
          promise.failure(t)
        } catch {
          case _ =>
        }
      }

      fa.onComplete(tryComplete(promise, _))
      fb.onComplete(tryComplete(promise, _))
      promise.future
    }

    def firstRefactorUsePromiseTryComplete[A](fa: Future[A], fb: Future[A]): Future[A] = {
      val promise = Promise[A]
      fa.onComplete(promise.tryComplete(_))
      fb.onComplete(promise.tryComplete(_))
      promise.future
    }
  }
}
