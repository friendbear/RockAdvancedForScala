package concurrency


/**
  * A Taste of Advanced Scala
  * Functional Concurrent Programming
  *
  * - Thread communication Exercise
  */
object ProducerConsumerLevel3Plus extends App {

  /*
    Exercise.
   */

  // 1) think of an example where notifyAll acts in a different way than notify?
  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i]  waiting ...")
        bell.wait()

        println(s"[thread $i] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock'n roll!")
      bell.synchronized {
        bell.notifyAll() // 🔴 All synchronized wait Object notify
      }
    }).start()
  }
  testNotifyAll()

  // 2) create a deadlock
  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rize(this)
        println(s"$this: I am rising to my friend $other")
      }
    }
    def rize(other: Friend) = {
      this.synchronized{
        println(s"$this: I am rising to my friend $other")
      }
    }

    // 3
    var side = "right"
    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }
    def pass(other: Friend): Unit = {
      while (this.side == other.side) {
        println(s"$this: Oh, but please $other, feel free to pass ...")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }
  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

  val deadLock = {
    new Thread(() => sam.bow(pierre)).start() // sam's lock,    |   then pierre's lock
    new Thread(() => pierre.bow(sam)).start() // pierre's lock, |   then sam's lock
  }
  // 3) create a livelock
  val liveLock = {
    new Thread(() => sam.pass(pierre)).start()
    new Thread(() => pierre.pass(sam)).start()
  }

  liveLock

}
