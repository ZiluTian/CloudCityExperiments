package example
package epidemic.v4

import scala.util.Random

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(populationPerMachine: Int, p: Double, sbm: Boolean, totalMachines: Int): IndexedSeq[Actor] = {
            val lastId = meta.runtime.Actor.lastAgentId.toInt

            val citizens = (1 to populationPerMachine).map(c => { new Person(Random.nextInt(90) + 10) })

            if (!sbm){
                citizens.foreach(c => {
                    c.connectedAgentIds = Range(1, totalMachines*populationPerMachine).filter(i => {(i!= c.id) &&  Random.nextDouble() < p}).map(_.toLong)
                })
            } else {
                // One block per machine
                citizens.foreach(c => {
                    c.connectedAgentIds = Range(lastId, populationPerMachine + lastId).filter(i => {(i!= c.id) &&  Random.nextDouble() < p}).map(_.toLong)
                })
            }
            
            citizens
        }
    }
}

object Example extends App {

  val cls1: ClassWithObject[Person] = Person.reflect(IR)

  val mainClass = MainInit.liftedMain
    
  compileSims(List(cls1), Some(mainClass))
}