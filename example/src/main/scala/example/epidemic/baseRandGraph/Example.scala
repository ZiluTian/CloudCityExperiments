package example
package epidemic.baseRandGraph

import scala.util.Random

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(verticesPerBlock: Int, edgeProb: Double, blocks: Int, cfreq: Int, interval: Int): IndexedSeq[Actor] = {
            Range(0, blocks).flatMap(block => {
                val nodes = Range(block * verticesPerBlock, verticesPerBlock * (block+1))
                nodes.map(i => {
                    val person = new Person(Random.nextInt(90) + 10, cfreq, interval)
                    person.id = i
                    person.connectedAgentIds = nodes.filter(n => {
                        (n!=i) && edgeProb > Random.nextDouble() 
                    }).map(_.toLong)
                    person
                })
            })
        }
    }
}

object Example extends App {

  val cls1: ClassWithObject[Person] = Person.reflect(IR)

  val mainClass = MainInit.liftedMain
    
  compileSims(List(cls1), Some(mainClass))
}