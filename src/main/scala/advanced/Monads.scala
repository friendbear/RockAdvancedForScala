package advanced

object Monads extends App {

  // our own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
   * left-identity
   *
   * unit.flatMap(f) = f(x)
   * Attempt(x).flatMap(f) = f(x) // Success case!
   * Success(x).flatMap(f) = f(x) // proved.
   *
   * right-identity
   *
   * attempt.flatMap(unit) = attempt
   * Success(x).flatMap(x => Accept(x)) = Accept(x) = Success(x)
   *
   * Fail(e).flatMap(...) = Fail(e)
   *
   * associativity
   *
   * attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))
   * Fail(e).flatMap(f).flatMap(g) = Fail(e)
   *
   * Success(v).flatMap(f).flatMap(g) =
   *   f(v).flatMap(g) OR Fail(e)
   *
   *   Sccess(v).flatMap(x => f(x).flatMap(g)) =
   *   f(v).flatMap(g) OR Fail(e)
   */

  val attempt = Attempt {
    throw new RuntimeException("My own monad, yes!")
  }

  println(attempt)

  /*
   * EXERCISE:
   * 1) impliment a Lazy[T] monad = computation whitch will only be executed when its neede.
   * unit/apply
   * flatMap
   *
   * 2) Monads = unit + flatMap
   *    Monads = unit + map + flattern
   *
   *    Monad[T] {
   *      def flatMap[E](f: T => Monad[B]j): Monad[B] ... (impemented)
   *      def map[B](f: T => B): Monad[B] = ???
   *      def flattern(m: Monad[Monad[T]): Monad[T] = ???
   *
   *      (have List in mind)
   */
}
