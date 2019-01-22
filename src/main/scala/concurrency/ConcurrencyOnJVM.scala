package concurrency

/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Concurrency Problems on the JVM
  *
  * Concurrency problems!
  * account.synchronized {
  *   account.subtract(price)
  * }
  *
  * or @volatile var account: Int = 50000
  */
object ConcurrencyOnJVM extends App {

  {
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
      println(x) // => 0
    }

    for (_ <- 1 to 1000) runInParallel
  }

  // race condition
  {
    class BankAccount(var amount: Int) {
      override def toString: String = "" + amount
    }
    def buy(account: BankAccount, thing: String, price: Int) = {
      account.amount -= price // account.amount = account.amount - price
      println("I' ve bought " + thing)
      println("my account is now " + account)
    }

    for (_ <- 1 to 10) {
      val account = new BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iPhone12", 4000))

      thread1.start()
      thread2.start()
      Thread.sleep(100)
      println(account.amount)
      if (account.amount != 43000) println("AHA: " + account.amount)

    }
    /*
     * thread1 (shoes): 500000
     *   - account1 = 50000 - 3000 = 470000
     * thread2 (iphone): 500000
     *   - account = 50000 - 4000 = 46000 overwrites the memory of account.amount
     */

    // option #1
    // use synchronized()
    def byeSafe(account: BankAccount, thing: String, price: Int) =
      account.synchronized({
        // no two thread can evaluate this at this at the same time
        account.amount -= price
        println("I've bought " + thing)
        println("my account is now " + account)
      })

    // option #2: use @volatile
    class BankAccount2(@volatile var amount: Int)
  }


  /**
    * Exercises
    * 1) Construct 50 "inception" threads
    *     Thread1 -> Thread2 -> Thread3 -> ...
    *     println("hello from thread #3)
    *   in REVERSE ORDER
    */
  {
    def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread $i")
    })

    inceptionThreads(50).start()
  }

  /*
   * 2
   */
  {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())

    /*
     * 1) what is the biggest value possible for x? 100
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
  }

  /*
   * 3 sleep fallacy
   */
  {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })
    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(2000)
    awesomeThread.join() // wait for the awesome thread to join
    println(message)
    /*
    what's the value of message? almost always " Scala is awesome"
    is it guaranteed? NO!
    why? why not?

    (main thread)
      message = "Scala sucks"
      awesomeThread.start()
      sleep() = relieves execution
    (awesome thread)
      sleep() = relieves execution
    (OS gives the CPU to some important thread - takes CPU for more than 2 seconds)
      println("Scala sucks")
    (OS gives the CPU to awesomeThread)
      message = "Scala is awesome"
    */

    // how to we fix this!
    // syncrhonizing  dose Not work
  }

}
