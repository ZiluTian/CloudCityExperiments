// package example
// package gameOfLife.dist

// import scala.collection.mutable.ListBuffer

// object Example extends App {
    
//     val liftedMain = meta.classLifting.liteLift {
//         def apply(width: Int, height: Int, agentsPerMachine: Int, totalMachines: Int): IndexedSeq[Actor] = {
//             val allAgents: ListBuffer[Actor] = new ListBuffer[Actor]

//             // Assume 1 market for now
//             if (machineId == 0) {
//                 val market = new Market()
//                 allAgents.append(market)
//                 val lastAgentId = meta.runtime.Actor.lastAgentId.toInt + 1
//                 val traders = (1 to tradersPerMachine).map(x => new Trader(initialWealth, interestRate))
//                 market.connectedAgentIds = Range(lastAgentId, tradersPerMachine*totalMachines+lastAgentId).map(_.toLong)
//                 allAgents.appendAll(traders)
//             } else {
//                 meta.runtime.Actor.lastAgentId = machineId * tradersPerMachine
//                 val traders = (1 to tradersPerMachine).map(x => new Trader(initialWealth, interestRate))
//                 allAgents.appendAll(traders)
//             }
//             allAgents.toList
//         }
//     }
    
//     val cls1: ClassWithObject[Market] = Market.reflect(IR)
//     val cls2: ClassWithObject[Trader] = Trader.reflect(IR)

//     compileSims(List(cls1, cls2), Some(liftedMain))
// }