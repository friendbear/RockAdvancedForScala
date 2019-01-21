package concurrency

import java.util.concurrent.Executors

/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Intro to Parallel Programming on the JVM
  */
object Intro extends App {


  /*
   * interface Runnable {
   *   public void run()
   * }
   */
  {
    // JVM threads
    val runnable = new Thread(new Runnable {
      override def run(): Unit = println("Running parallel")
    })
    val aThread = new Thread(runnable)


    aThread.start() // gives the signal to the JVM to start a JVM thread
    // create a JVM thread => OS thread
    new Thread(() => println("Running parallel")).start()

    //
    // run
    runnable.run() // does't do anything in parallel!

    aThread.join() // blocks until aThread finishes running


    val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
    val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
    threadHello.start()
    threadGoodbye.start()
    // different runs produce different results!
  }

  // Executors
  {
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

    { // main thread
      // throws an exception in the calling thread
      pool.execute(() => println("should not appear"))
      pool.shutdown()
      //pool.shutdownNow()
      println(pool.isShutdown)
    }
  }
  //

}
