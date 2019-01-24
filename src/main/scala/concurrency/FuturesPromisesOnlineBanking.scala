package concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.util.Success

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Futures, Part3(Online Banking) and promise
  *
  */
object FuturesPromisesOnlineBanking extends App {


  //online banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // simulate some processes
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {

      // fetch the user from the DB
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      import scala.concurrent.duration._

      /*
       🔴 Timeout
       Exception in thread "main" java.util.concurrent.TimeoutException: Futures timed out after [1 second]
      	at scala.concurrent.impl.Promise$DefaultPromise.ready(Promise.scala:259)
       */
      Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
    }
  }
  println(BankingApp.purchase("Daniel", "iPhone 12", "rock the jvm store", 3000))

  // promises
  val promise = Promise[Int]() // "controller" over a future
  val future = promise.future

  // thread 1 - "consumer"
  future.onComplete{
    case Success(r) => println("[Consumer] I've received " + r)
  }

  // thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    // "fulfilling" the promise
    promise.success(42) // => Handle future
    println("[producer] done")
  })

  producer.start()

  Thread.sleep(1000)
}
