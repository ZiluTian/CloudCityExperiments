package simulation.akka
package test

object gameOfLifeStandalone {
    def main(args: Array[String]): Unit = {
        val edgeFilePath: String = args(0)
        val mode: Int = args(1).toInt
        val cfreq: Int = args(2).toInt
        val cinterval: Int = args(3).toInt
        val totalTurns: Int = 200
        var role: String = "Standalone"
        var port: Int = 25251

        mode match {
            case 1 => {
                // base
                val agents = generated.example.gameOfLife.base.InitData(edgeFilePath, cfreq, cinterval)
                API.OptimizationConfig.mergedWorker()
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 2 => {
                // callAndForget
                val agents = generated.example.gameOfLifeRPCOneSide.InitData(100, 100)
                API.OptimizationConfig.mergedWorker()
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }

            case 3 => {
                // Direct method call, double buffer
                val agents = generated.example.gameOfLifeRPCOneSideDoubleBuffer.InitData(100, 100)
                API.OptimizationConfig.directMethodCall()
                val snapshot1 = API.Simulate(agents, 2*totalTurns, role, port)
            }

            case 4 => {
                // Direct method call, multi-version
                val agents = generated.example.gameOfLifeRPCOneSideMultiversion.InitData(100, 100)
                API.OptimizationConfig.directMethodCall()
                val snapshot1 = API.Simulate(agents, totalTurns, role, port)
            }
        }
    }
}
