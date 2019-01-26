package implicits

/** A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - TypeClasses
  *   - trait HTMLSerializer[T] vs trait HTMLWritable
  *   - TYPE CLASS
  *    {{{
  *     trait MyTypeClassTemplate[T] {
  *       def action(value: T): String
  *     }
  *    }}}
  */
object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }
  User("John", 32, "john@rockthejvm.com").toHtml

  /*
    1 - for the type WE write
    2 - ONE implementation out of quite a number
   */
  // option 2 - pattern matching
  object HTMLSerializerPatternMatching {
    def serializeToHtml(value: Any) = value match {
      case User(n, a, e) => ()
      case _ => ()
    }
  }
  /*
    1 - lost type safety => HTMLSerializerPatternMatching
    2 - need to modify the code every time
    3 - still ONE implementation
   */
  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }
  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href=${user.email}/></div"
  }

  val john = User("John", 32, "john@rockthejvm.com")
  println(UserSerializer.serialize(john))

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

  /**
    * Equality
    */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }
  object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name
  }
  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.email == b.email
  }
  val ken = User("Ken", 32, " ken@rockthejvm.com")
  val bob = User("Bob", 42, " bob@rockthejvm.com")
  println(NameEquality(ken, bob))
  println(FullEquality(ken, bob))
}
