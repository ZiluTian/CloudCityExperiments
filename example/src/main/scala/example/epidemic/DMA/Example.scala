package example
package epidemic.dma

import scala.util.Random
import cloudcity.lib.Graph.LoadGraph

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(edgeFilePath: String): IndexedSeq[Actor] = {
            val edges: Map[Long, Iterable[Long]] = LoadGraph(edgeFilePath)

            val people: IndexedSeq[Actor] = edges.map(i => {
                val person = new Person(Random.nextInt(90) + 10)
                person.id = i._1
                person
            }).toVector

            people.map(p => {
                p.connectedAgents = edges(p.id).map(i => people(i.toInt))
                p
            })
        }
    }
}

object Example extends App {

  val cls1: ClassWithObject[Person] = Person.reflect(IR)

  val mainClass = MainInit.liftedMain
    
  compileSims(List(cls1), Some(mainClass))
}