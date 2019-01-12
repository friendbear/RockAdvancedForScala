package advanced

import java.util.concurrent.Executors

object Intro extends App {


  /*
   * interface Runnable {
   *   public void rrun()
   * }
   */
  // JVM threads
  val runnable = new Thread(new Runnable {
    override def run(): Unit = println("Running parallel")
  })

  val aThread = new Thread(runnable)


  aThread.start() // gives the signal to the JVM to start a  JVM thread
  // create a JVM thread => OS thread
  //
  // run
   runnable.run() // does't do anyting in parallel!

   aThread.join()  // blocks until aThread finishies running


   val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
   val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
   threadHello.start()
   threadGoodbye.start()
  // different runs produce different results!
  //
  // Executores
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })


  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  //pool.execute{() => println("should not appear")) // throws an exception in the calling thread
  println(pool.isShutdown)

  //
  def runInParallel = {
    var x = 0
    val thread1 = new Thread(() => {
      x = 1
    })
    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)
  }

  for (_ <- 1 to 10000) runInParallel

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount
  }
  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    println("I' ve bought " + thing)
    println("my account is now " + account)
  }

  for (_ <- 1 to 1000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buy(account, "shoes", 3000))
    val thread2 = new Thread(() => buy(account, "iPhone12", 4000))

    thread1.start()
    thread2.start()
    if (account.amount != 43000) println("AHA: " + account.amount)

  }

  /*
    * thread1 (shoes): 500000
    *   - account1 = 50000 - 3000 = 470000
    * thread2 (iphone): 500000
    *   - account = 50000 - 4000 = 46000 overwites the momory of acount.amount
    */
  // option #1 use synchronized()
  def byeSafe(account: BankAccount, thing: String, price: Int) =
    account.synchronized {
      account.amount -= price
      println("I've bought " + thing)
      println("my account is now " + account)
    }

  // option #2: use @volatile


  /**
    * Exercises
    * 1) Construct 50 "inception" threads
    *   Thread1 -> Thread2 -> Thread3 -> ...
    *   println("hello from thread #3)
    *   in REVERSE ORDER
    *
    */
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
    if (i < maxThreads) {
      val newThread = inceptionThreads(maxThreads, i + 1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })

  inceptionThreads(50).start()


  /*
    * 2
    */
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(() => x += 1))
  threads.foreach(_.start())

  /**
    * 1) what is thebiggest value possible for x? 100
    * 2) what is the  SMALLEST value possible for x? 1
    *
    * thread1: x = 0
    * thread2: x = 0
    * ....
    * thread100: x = 0
    *
    * for all threads: x = 1 and write it back to x
    */

  println(x)
  threads.foreach(_.join())
  println(x)

  /*
    * 3 sleep fallacy
    */
  var message = ""
  val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
  })
  message = "Scala is aweway"
  awesomeThread.start()
  Thread.sleep(1001)
  awesomeThread.join()


  // how to we fix that!
}
