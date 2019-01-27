  package implicits

import java.util.Date
/**
  * A Taste of Advanced Scala
  * AdvancedFunctional Programing
  *
  * - A Type Class End-to-end example: JSON Serialization
  */
object JSONSerialization extends App {

  /*
    Users, posts, feeds
    Serialize to JSON
   */
  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
    1 - intermediate data types: Int, String, List, Date
    2 - type classes for conversion to intermediate data types
    3 - serialize to JSON
   */
  sealed trait JSONValue { // intermediate data type
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    def stringify: String = "\"" + value + "\""
  }
  final case class JSONNumber(value: Int) extends JSONValue {
    def stringify: String = value.toString
  }
  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
     {
        name: "John"
        age: 22
        friends: [ ... ]
        latestPost: {
          content: "Scala Rocks"
          date: ...
        }
      }
     */
    def stringify: String = values.map {
      case (key, value) => "\"" + key + "\":" + value.stringify
    }.mkString("{", ",", "}")
  }

  val test1 = {
    val data = JSONArray(
      List(
      JSONObject(
          Map(
            "user" -> JSONString("Daniel"),
            "posts" -> JSONArray(List(
              JSONString("Scala Rocks!"),
              JSONNumber(453)
            ))
          )
        ),
        JSONObject(
          Map(
            "user" -> JSONString("Daniel"),
            "posts" -> JSONArray(List(
              JSONString("Scala Rocks!"),
              JSONNumber(453)
            ))
          )
        )
      )
    )
    println(data.stringify)
  }

  // type class
  /*
    1 - type class
    2 - type class instances (implicit)
    3 - pimp library to use type class instances
   */
  // call stringify on result
  // 2.1
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }
  // 2.2
  implicit object StringConverter extends JSONConverter[String] {
    def convert(value: String): JSONValue = JSONString(value)
  }
  // 2.3 conversion
  implicit class JSONOpts[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(value: Int): JSONValue = JSONNumber(value)
  }
  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))

  }
  implicit object PostConverter extends JSONConverter[Post] {
    def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "createdAt:" -> JSONString(post.createdAt.toString)
    ))

  }
  implicit object FeedConverter extends JSONConverter[Feed] {
    //def convert(feed: Feed): JSONValue = JSONObject(Map(
    //  "user" -> UserConverter.convert(feed.user),   // TODO
    //  "posts" -> JSONArray(feed.posts.map(PostConverter.convert(_))   // TODO
    //)))
    def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> feed.user.toJSON,
      "posts" -> JSONArray(feed.posts.map(_.toJSON)
      )))
  }

  val test2 = {
    val now = new Date(System.currentTimeMillis())
    val john = User("John", 34, "john@rockthejvm.com")
    val feed = Feed(john, List(
      Post("hello", now),
      Post("look at this cute puppy", now)
    ))
    println(feed.toJSON.stringify)
  }
}
