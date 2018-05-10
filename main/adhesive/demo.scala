import java.io.{File, PrintStream}
import java.nio.charset.Charset
import java.util
import java.util.SortedMap
import java.util.function.BiConsumer

import scala.sys.process._
import scala.io.Source
//Process.apply("cmd -k").!
//System.setOut(new PrintStream(new File("a.txt")))
//('a' to 'z').foreach(i => {
//  println(i + ":" + i.toInt)
//})
//println(97.asInstanceOf[Char])
Charset.availableCharsets().forEach(new BiConsumer[String, Charset] {
  override def accept(t: String, u: Charset): Unit = {
    try {
      println(t)
      println(new String("：ä¹±ç \u0081å\u0095\u008Aä¹±ç \u0081ä»\u0080ä¹\u0088ä¹±ç \u0081 ".getBytes(t), "UTF-8"))
      println
    }catch{
      case _:Exception => Unit
    }
  }
})

//val file = Source.fromFile("master-basic.iml")
//
//file.foreach(item => {
//  println(item)
//})

//for (line <- file.getLines) {
//  println(line)
//}

//file.close