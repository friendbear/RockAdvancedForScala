package exercises

import implicits.TypeClassesType2.{User}

object EqualityPlayground {

  /**
    * Equality
    */
  // TYPE CLASS
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }
  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }

  /*
    Exercise - improve the Equal TC with an implicit conversion class
    ===(another value: T)
    !==(another value: T)
   */
  implicit class TypeSafeEqual[T](value :T) {
    def ===(another: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, another)
    def !==(another: T)(implicit equalizer: Equal[T]): Boolean = ! equalizer.apply(value, another)
  }

  val john = User("Jon", 44, "jon@example.com")
  val anotherJohn = User("Jon", 44, "jon@example.com")

  println(john === anotherJohn)
  /*
    john.===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   */
  /*
    TYPE SAFE
   */

}
