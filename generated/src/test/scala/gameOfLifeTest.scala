package simulation.akka
package test

object gameOfLifeStandalone {
    def main(args: Array[String]): Unit = {
        val mode: Int = args(0).toInt
        val totalTurns: Int = 200
        var role: String = "Standalone"
        var port: Int = 25251

        mode match {
            case 1 => {
                // scale-up test
                List((10, 100), (50, 100), (100, 100), (100, 500), (100, 1000)).foreach(p => {
                    val agents = generated.example.gameOfLife.base.InitData(p._1, p._2, 1, 1)
                    API.Simulate(agents, totalTurns, role, port)
                })
            }

            case 2 => {
                // comm. freq
                List(10, 20, 30).foreach(cfreq => {
                    val agents = generated.example.gameOfLife.base.InitData(100, 100, cfreq, 1)
                    API.Simulate(agents, totalTurns, role, port)
                })
            }

            case 3 => {
                // comm. freq
                List(5, 10, 20).foreach(interval => {
                    val agents = generated.example.gameOfLife.base.InitData(100, 100, 1, interval)
                    API.Simulate(agents, totalTurns, role, port)
                })
            }

            case 4 => {
                // callAndForget
                val agents = generated.example.gameOfLifeRPCOneSide.InitData(100, 100)
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 5 => {
                // Direct method call, double buffer
                val agents = generated.example.gameOfLifeRPCOneSideDoubleBuffer.InitData(100, 100)
                API.OptimizationConfig.directMethodCall()
                val snapshot1 = API.Simulate(agents, 2*totalTurns, role, port)
            }

            case 6 => {
                // Direct method call, multi-version
                val agents = generated.example.gameOfLifeRPCOneSideMultiversion.InitData(100, 100)
                API.OptimizationConfig.directMethodCall()
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }
        }
    }
}
