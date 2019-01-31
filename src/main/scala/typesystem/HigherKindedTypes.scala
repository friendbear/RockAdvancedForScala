package typesystem

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * A Taste of Advanced Scala
  * Mastering the Type System
  *
  * - Higher-Kinded Types
  * {{{
  *   trait Monad[F[_], A] { // higher-kinded type class
  *     def flatMap[B](f: A => F[B]): F[B]
  *     def map[B](f: A => B): F[B]
  *   }
  *   implicit class MonadList[T](list: List[T]) extends Monad[List, T] {
  *     override def flatMap[B](f: T => List[B]): List[B] = list.flatMap(f)
  *     override def map[B](f: T => B): List[B] = list.map(f)
  *   }
  *   implicit class MonadOption[T](option: Option[T]) extends Monad[Option, T] {
  *     override def flatMap[B](f: T => Option[B]): Option[B] = option.flatMap(f)
  *     override def map[B](f: T => B): Option[B] = option.map(f)
  *   }
  * }}}
  */
object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]
  {
    trait MyList[T] {
      def flatMap[B](f: T => B): MyList[T]
    }
    trait MyOption[T] {
      def flatMap[B](f: T=> B): MyOption[T]
    }
    trait MyFuture[T] {
      def flatMap[B](f: T => B): MyFuture[T]
    }

    // combine/multiply List(1, 2) x List("a", "b") => List(1a, 1b , 2a, 2b)
    def multiply[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
      for {
        a <- listA
        b <- listB
      } yield (a, b)

    def multiply[A, B](listA: Option[A], listB: Option[B]): Option[(A, B)] =
      for {
        a <- listA
        b <- listB
      } yield (a, b)

    def multiply[A, B](listA: Future[A], listB: Future[B]): Future[(A, B)] =
      for {
        a <- listA
        b <- listB
      } yield (a, b)
  }

  // HKT
  // 🔵 🔴🔵 🔴🔵 🔴🔵 🔴🔵 🔴
  trait Monad[F[_], A] { // higher-kinded type class
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }
  implicit class MonadList[T](list: List[T]) extends Monad[List, T] {
    override def flatMap[B](f: T => List[B]): List[B] = list.flatMap(f)
    override def map[B](f: T => B): List[B] = list.map(f)
  }
  implicit class MonadOption[T](option: Option[T]) extends Monad[Option, T] {
    override def flatMap[B](f: T => Option[B]): Option[B] = option.flatMap(f)
    override def map[B](f: T => B): Option[B] = option.map(f)
  }


  val monadList = new MonadList(List(1, 2, 3))
  monadList.flatMap(x => List(x, x + 1)) // List[Int] Monad[List, Int] => List[Int]
  monadList.map(_ * 2) // List[Int] Monad[List, Int] => List[Int]

  def multiply[F[_], A, B](implicit ma: Monad[F, A], mb: Monad[F, B]): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  /*
    ma.flatMap(a => mb.map(b => (a, b)))
   */
  println(multiply(new MonadList(List(1,2)), new MonadList(List("a", "b"))))
  println(multiply(new MonadOption[Int](Some(1)), new MonadOption[String](Some("scala"))))

  // implicit class  same implementation!!!
  println(multiply(List(1,2), List("a", "b")))
  println(multiply(Some(1), Some("scala")))

}
