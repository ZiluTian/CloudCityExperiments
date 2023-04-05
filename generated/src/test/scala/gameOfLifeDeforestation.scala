package simulation.akka
package test

import simulation.akka.API._
import meta.runtime.Actor

case object GoLQ1Timeseries extends SimulationTimeseries[Int, Int] {
  def mapper(x: Actor): Int = {
      x.asInstanceOf[generated.example.gameOfLife.Cell].alive
  }

  def reducer(x: Iterable[Int]): Int = x.sum

  def reduce(round: Int): Unit = {
    log.update(round, intermediateLog.getOrElse(round, List[Int]()).sum)
  }
}

case object GoLQ2Timeseries extends SimulationTimeseries[Option[Actor], Iterable[Actor]] {
  def mapper(x: Actor): Option[Actor] = {
    if (x.asInstanceOf[generated.example.gameOfLife.Cell].alive==1) {
        Some(x)
    } else {
        None
    }
  }
  def reducer(x: Iterable[Option[Actor]]): Iterable[Actor] = x.filter(i => i.isDefined).map(i => i.get)

  def reduce(round: Int): Unit = {
    log.update(round, intermediateLog.getOrElse(round, List[Iterable[Actor]]()).flatten)
  }
}

object gameOfLifeDeforestation {
    def main(args: Array[String]): Unit = {
        val width = args(0).toInt
        val height: Int = args(1).toInt
        val totalTurns: Int = args(2).toInt
        val mode: Int = args(3).toInt
        var role: String = "Standalone"
        var port: Int = 25251
        if (args.size > 5) {
            role = args(4)
            port = args(5).toInt
        }

        mode match {
            case 1 => {
                // timeseries
                val agents = generated.example.gameOfLife.InitData(width, height)
                API.OptimizationConfig.timeseries=Some(FullTimeseries)
                // default to time series
                val snapshot1 = Simulate(agents, totalTurns, role, port)
                // FullTimeseries.log.foreach(x => println(x._2.map(i => i.asInstanceOf[generated.example.gameOfLife.Cell].alive)))
            }

            case 2 => {
                // Q2 snapshot
                val agents = generated.example.gameOfLife.InitData(width, height)
                simulation.akka.API.OptimizationConfig.timeseries = Some(GoLQ1Timeseries)
                API.Simulate(agents, totalTurns)
                GoLQ1Timeseries.log.foreach(x => println(x._2))
            }

            // For the following experiments, need to change the default value of log variable in 
            // /Akka/src/main/scala/API/Simulate.scala to match the type of Simulate.log, such as GoLQ1Timeseries
            case 3 => {
                // q1
                // (logicalClock, Iterable(local_sims.filter(i => i._2.asInstanceOf.alive==1).size))
                val agents = generated.example.gameOfLife.InitData(width, height)
                simulation.akka.API.OptimizationConfig.timeseries = Some(GoLQ2Timeseries)
                API.Simulate(agents, totalTurns)
                GoLQ2Timeseries.log.foreach(x => println(x._2.map(i => i.asInstanceOf[generated.example.gameOfLife.Cell].alive)))
            }
        }

    }
}