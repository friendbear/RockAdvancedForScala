package implicits

import exercises.EqualityPlayground._

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
  *
  * - TypeClasses, Part 3
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

  // 1 - we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(date: Date): String = s"<div>${date.toString()}</div>"
  }

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  // part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  /*
    - type class itself -- HTMLSerializer[T] { .. }
    - type class instances (some of which are implicit)  --- UserSerializer, IntSerializer
    - conversion with implicit classes
   */
  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body> ${content.toHTML(serializer)} </body></html>"

  def htmlSugar[T : HTMLSerializer](content: T): String = {
     val serializer = implicitly[HTMLSerializer[T]]
     // use serializer
     s"<html><body> ${content.toHTML(serializer)}</body></html>"
   }

  // implicitly
  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions](defaultPermissions)

  val john = User("John", 32, "john@rockthejvm.com")
  val testCode1 = {
    println(HTMLSerializer.serialize(42)(IntSerializer))
    println(HTMLSerializer[Int].serialize(42))

    println(HTMLSerializer.serialize(john))

    // access to the entire type class interface
    println(HTMLSerializer[User].serialize(john)) // HTMLSerializer[T].apply.serialize

  }

  val testCode2 = {
    val ken = User("Ken", 32, " ken@rockthejvm.com")
    val bob = User("Bob", 42, " bob@rockthejvm.com")

    Equal.apply(bob, ken)(NameEquality)
    Equal(bob, ken)(FullEquality)

    // AD-HOC polymorphism
    println(Equal(bob, ken)) // implicit object NameEquality
  }

  val testCode3 = {
    println(john.toHTML(UserSerializer)) // println(new HTMLEnrichment[User](john).toHTML
    println(john.toHTML) // println(new HTMLEnrichment[User](john).toHTML
    // COOL !

    /*
     - extend to new types
     - choose implementation
     - super expressive!
     */
    println(2.toHTML)
    println(john.toHTML(PartialUserSerializer))

    println(htmlBoilerplate(john)(HTMLSerializer[User]))
    println(htmlSugar(john))
  }
}
