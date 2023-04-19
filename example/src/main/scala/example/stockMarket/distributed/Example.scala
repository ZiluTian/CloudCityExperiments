package example
package stockMarket
package v4

import scala.collection.mutable.ListBuffer

object Example extends App {
    
    val liftedMain = meta.classLifting.liteLift {
        // Assume 1 market agent
        def apply(tradersPerMachine: Int, machineId: Int, totalMachines: Int): IndexedSeq[Actor] = {
            val initialWealth: Double = 1000
            val interestRate: Double = 0.001

            // Assume 1 market for now
            if (machineId == 0) {
                // market has id 0
                val market = new Market()
                market.id = 0
                meta.runtime.Actor.lastAgentId = 0
                val traders = (1 to tradersPerMachine).map(x => new Trader(initialWealth, interestRate))
                market.connectedAgentIds = Range(1, tradersPerMachine*totalMachines+1).map(_.toLong)
                market +: traders
            } else {
                meta.runtime.Actor.lastAgentId = machineId * tradersPerMachine - 1
                (1 to tradersPerMachine).map(x => new Trader(initialWealth, interestRate))
            }
        }
    }
    
    val cls1: ClassWithObject[Market] = Market.reflect(IR)
    val cls2: ClassWithObject[Trader] = Trader.reflect(IR)

    compileSims(List(cls1, cls2), Some(liftedMain))
}