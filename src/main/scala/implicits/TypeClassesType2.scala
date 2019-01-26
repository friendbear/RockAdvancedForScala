package implicits


/** A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - TypeClasses, Part 2
  *   - TYPE CLASS
  *
  *   {{{
  *     trait Equal[T] {
  *       def apply(a: T, b: T): Boolean
  *     }
  *     implicit object Equal {
  *       def apply[T](a: T, b: T)(implicit equal: Equal[T]): Boolean =
  *         equal.apply(a, b)
  *     }
  *   }}}
  *
  *   // AD-HOC polymorphism
  *   Equal(bobInstance, jonInstance) => false
  */
object TypeClassesType2 extends App {

  case class User(name: String, age: Int, email: String)


  // TYPE CLASS 1
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>$value</div"
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href=${user.email}/></div"
  }

  val testCode1 = {
    println(HTMLSerializer.serialize(42)(IntSerializer))
    println(HTMLSerializer[Int].serialize(42))

    val john = User("John", 32, "john@rockthejvm.com")
    println(HTMLSerializer.serialize(john))

    // access to the entire type class interface
    println(HTMLSerializer[User].serialize(john)) // HTMLSerializer[T].apply.serialize

    /*
    <div style: color=blue>42</div
    <div style: color=blue>42</div
    <div>John (32 yo) <a href=john@rockthejvm.com/></div
    <div>John (32 yo) <a href=john@rockthejvm.com/></div
     */
  }


  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }
  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

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

  val testCode2 = {
    val ken = User("Ken", 32, " ken@rockthejvm.com")
    val bob = User("Bob", 42, " bob@rockthejvm.com")

    Equal.apply(bob, ken)(NameEquality)
    Equal(bob, ken)(FullEquality)

    // AD-HOC polymorphism
    println(Equal(bob, ken)) // implicit object NameEquality
  }
}
