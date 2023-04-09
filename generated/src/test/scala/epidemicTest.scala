package simulation.akka
package test

object epidemic {
    def main(args: Array[String]): Unit = {
        val edgeFilePath: String = args(0)
        val totalTurns: Int = args(1).toInt
        val mode: Int = args(2).toInt
        val cfreq: Int = args(3).toInt
        val cinterval: Int = args(4).toInt

        var role: String = "Standalone"
        var port: Int = 25251

        if (args.size > 6) {
            role = args(5)
            port = args(6).toInt
        }

        mode match {
            case 1 => {
                val agents = generated.example.epidemic.base.InitData(edgeFile, cfreq, cinterval)
                API.OptimizationConfig.mergedWorker()
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 2=> {
                // Generalized double-buffering, with delayed processing 
                val agents = generated.example.epidemic.dma.InitData(edgeFile)
                API.OptimizationConfig.mergedWorker()
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 3 => {
                val agents = generated.example.epidemic.nodma.InitData(edgeFile)
                API.OptimizationConfig.mergedWorker()
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 4 => {
                // v4, partial materialized workers
                if (role == "Driver") {
                    API.Simulate.driver(totalTurns)
                } else if (role.startsWith("Machine-")){
                    val mid = role.stripPrefix("Machine-").toInt
                    val totalMachines = blocks
                    meta.runtime.Actor.lastAgentId = mid * population
                    val agents = generated.example.epidemic.v4.InitData(population, p, isSBM, totalMachines)
                    API.OptimizationConfig.mergedWorker()
                    API.Simulate.machine(mid, agents, totalTurns)
                }
            }
        }
    }
}
