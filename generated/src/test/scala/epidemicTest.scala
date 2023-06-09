package simulation.akka
package test

object epidemicsStandalone {
    def main(args: Array[String]): Unit = {
        val edgeFile: String = args(0)
        val mode: Int = args(1).toInt
        val cfreq: Int = args(2).toInt
        val cinterval: Int = args(3).toInt

        val totalTurns: Int = 50
        var role: String = "Standalone"
        var port: Int = 25251

        mode match {
            case 1 => {
                val agents = generated.example.epidemic.base.InitData(edgeFile, cfreq, cinterval)
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 2=> {
                // Generalized double-buffering, with delayed processing 
                val agents = generated.example.epidemic.dma.InitData(edgeFile)
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 3 => {
                val agents = generated.example.epidemic.nodma.InitData(edgeFile)
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }
        }
    }
}

object epidemicsStandaloneDynamic {
    def main(args: Array[String]): Unit = {
        val mode: Int = args(0).toInt
        val edgeProb: Double = 0.01
        val totalTurns: Int = 50
        var role: String = "Standalone"
        var port: Int = 25251

        mode match {
            case 1 => { // scaleup, ERM
                List(1000, 5000, 10000, 50000, 100000).foreach(i => {
                    val agents = generated.example.epidemic.baseRandGraph.InitData(i, edgeProb, 1, 1, 1)
                    API.Simulate(agents, totalTurns, role, port)
                })
            }
            case 2 => { // scaleup, SBM
                List(1000, 5000, 10000, 50000, 100000).foreach(i => {
                    val agents = generated.example.epidemic.baseRandGraph.InitData((i / 5).toInt, edgeProb, 5, 1, 1)
                    API.Simulate(agents, totalTurns, role, port)
                })
            }
        }
    }
}

object epidemicsDistributed {
        def main(args: Array[String]): Unit = {
        val population: Int = args(0).toInt
        val isSBM: Boolean = (args(1).toInt == 1)
        val totalMachines: Int = args(2).toInt
        var role: String = args(3)
        var port: Int = args(4).toInt
        val totalTurns: Int = 50

        // v4, partial materialized workers
        if (role == "Driver") {
            API.Simulate.driver(totalTurns)
        } else if (role.startsWith("Machine-")){
            val mid = role.stripPrefix("Machine-").toInt
            // Set the agent counter to agents that belong to this machine
            meta.runtime.Actor.lastAgentId = mid * population
            val agents = generated.example.epidemic.v4.InitData(population, 0.01, isSBM, totalMachines)
            API.OptimizationConfig.mergedWorker()
            API.Simulate.machine(mid, agents, totalTurns)
        }
    }
}