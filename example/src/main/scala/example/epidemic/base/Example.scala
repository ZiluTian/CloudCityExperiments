package example
package epidemic.base

import scala.util.Random
import scala.io.Source
import cloudcity.lib.Graph.LoadGraph

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(edgeFilePath: String, cfreq: Int, interval: Int): List[Actor] = {
            var edges = LoadGraph(edgeFilePath)
            edges.map(i => {
                val person = new Person(Random.nextInt(90) + 10, cfreq, interval)
                person.id = i._1
                person.connectedAgentIds = i._2
                person
            }).toList
        }
    }
}

object Example extends App {

  val cls1: ClassWithObject[Person] = Person.reflect(IR)

  val mainClass = MainInit.liftedMain
    
  compileSims(List(cls1), Some(mainClass))
}