package concurrency

/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - JVM Thread Communication
  */
object ThreadCommunication extends App {

  /*
    the producer-consumer problem

    producer -> [ ? ] -> consumer
  */
  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int) = value = newValue
    def get = {
      val result = value
      value = 0
      result
    }
  }

  lazy val naive = {
    def naiveProdCons(): Unit = {
      val container = new SimpleContainer

      val consumer = new Thread(() => {
        println("[consumer] waiting...")
        while (container.isEmpty) {
          println("[consumer] actively waiting...")
        }
        println("[consumer] I have consumed " + container.get)
      })

      val producer = new Thread(() => {
        println("[producer] computing...")
        Thread.sleep(500)
        val value = 42
        println("[producer] I have produced, after long work, the value" + value)
        container.set(value) // Change isEmpty => True
      })

      consumer.start
      producer.start
    }
    naiveProdCons()
  }

  // wait and notify
  lazy val smart = {
    /*
      Synchronized
      val someObject = "hello'
      someObject.synchronized {
        // code
      }


      wait() and notify()

      // thread 1
      val someObject = "hello"
      someObject.synchronized {
        // code part 1
        someObject.wait()
        // code part 2
      }

      // thread 2
      someObject.synchronized {
        // code
        someObject.notify()
      }

     */
    def smartProdCons() = {
      val container = new SimpleContainer
      val consumer = new Thread(() => {
        println("[consumer] waiting...")
        container.synchronized{
          container.wait()
        }

        // container must have some value
        println("[consumer] I have consumed " + container.get)
      })

      val producer = new Thread(() => {
        println("[producer] Hard at work...")
        Thread.sleep(2000)
        val value = 42

        container.synchronized {
          println("[producer] I'm producing " + value)
          container.set(value)
          container.notify()
        }
      })
      consumer.start
      producer.start
    }
    smartProdCons()
  }
}
