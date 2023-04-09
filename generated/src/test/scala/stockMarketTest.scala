package simulation.akka
package test

object stockMarketStandalone {
    def main(args: Array[String]): Unit = {
        val mode: Int = args(0).toInt

        val totalMarkets = 1
        val totalTurns: Int = 200
        var role: String = "Standalone"
        var port: Int = 25251

        mode match {
            case 1 => {
                // scale-up test
                List(999, 4999, 9999, 49999, 99999).foreach(traders => {
                    val agents = generated.example.stockMarket.base.InitData(1, traders, 1, 1)
                    API.OptimizationConfig.mergedWorker()
                    API.Simulate(agents, totalTurns, role, port)
                })
            }
 
            case 2 => {
                // communication frequency
                List(10, 20, 30).foreach(cfreq => {
                    val agents = generated.example.stockMarket.base.InitData(1, 9999, cfreq, 1)
                    API.OptimizationConfig.mergedWorker()
                    API.Simulate(agents, totalTurns, role, port)
                })
            }
            
            case 3 => {
                // computation interval
                List(5, 10, 20).foreach(compInterval => {
                    val agents = generated.example.stockMarket.base.InitData(1, 9999, 1, compInterval)
                    API.OptimizationConfig.mergedWorker()
                    API.Simulate(agents, totalTurns, role, port)
                })
            }

            case 4 => {
                // generalized double-buffering
                val agents = generated.example.stockMarket.v6.InitData(1, 9999)
                API.OptimizationConfig.mergedWorker()
                API.Simulate(agents, totalTurns, role, port)

                // same semantics as generalized double-buffering, but do not allow dma
                val agents2 = generated.example.stockMarket.v7.InitData(1, 9999)
                API.OptimizationConfig.mergedWorker()
                API.Simulate(agents2, totalTurns, role, port)
            }
        }
    }
}

object stockMarketDistributed {
    def main(args: Array[String]): Unit = {
        val totalMarkets = 1
        val tradersPerMarket: Int = args(0).toInt
        val role: String = args(1)
        val totalMachines = args(2).toInt

        val totalTurns: Int = 200
        val port: Int = if (role == "Driver") 25251 else  0
            
        if (role == "Driver") {
            API.Simulate.driver(totalTurns)
        } else if (role.startsWith("Machine-")){
            val mid = role.stripPrefix("Machine-").toInt
            val agents = generated.example.stockMarket.v4.InitData(tradersPerMarket, mid, totalMachines)
            API.OptimizationConfig.mergedWorker()
            API.Simulate.machine(mid, agents, totalTurns) 
        }
    }
}
