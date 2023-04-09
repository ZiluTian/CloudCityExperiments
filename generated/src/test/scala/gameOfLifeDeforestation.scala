package simulation.akka
package test

import simulation.akka.API._
import meta.runtime.Actor

case class Alive(alive: Int) extends Serializable

case object GoLQ1Timeseries extends SimulationTimeseries {
  override def mapper(x: Serializable): Serializable = {
      Alive(x.asInstanceOf[generated.example.gameOfLife.base.Cell].alive)
  }

  override def reducer(x: Iterable[Iterable[Serializable]]): Iterable[Serializable] = Iterable(Alive(x.flatten.asInstanceOf[Iterable[Alive]].map(d => d.alive).sum))
}

case object GoLQ2Timeseries extends SimulationTimeseries {
  override def mapper(x: Serializable): Serializable = {
    if (x.asInstanceOf[generated.example.gameOfLife.base.Cell].alive==1) {
        Some(x)
    } else {
        None
    }
  }
  override def reducer(x: Iterable[Iterable[Serializable]]): Iterable[Serializable] = x.flatten.map(i => i.asInstanceOf[Option[Actor]]).filter(i => i.isDefined).map(i => i.get)
}

object gameOfLifeDeforestation {
    def main(args: Array[String]): Unit = {
        val edgeFilePath: String = args(0)
        val totalTurns: Int = args(1).toInt
        val mode: Int = args(2).toInt
        var role: String = "Standalone"
        var port: Int = 25251

        mode match {
            case 1 => {
                // timeseries
                val agents = generated.example.gameOfLife.base.InitData(edgeFilePath, 1, 1)
                API.OptimizationConfig.logControllerEnabled = true
                API.OptimizationConfig.timeseriesSchema = FullTimeseries
                // default to time series
                val snapshot1 = Simulate(agents, totalTurns)
                API.Simulate.timeseries.foreach(x => println(x.map(i => i.asInstanceOf[generated.example.gameOfLife.base.Cell].alive)))
            }

            case 2 => {
                // Q2 snapshot
                val agents = generated.example.gameOfLife.base.InitData(edgeFilePath, 1, 1)
                API.OptimizationConfig.logControllerEnabled = true
                API.OptimizationConfig.timeseriesSchema = GoLQ1Timeseries
                API.Simulate(agents, totalTurns)
                API.Simulate.timeseries.foreach(x => println(x))
            }

            case 3 => {
                // q1
                // (logicalClock, Iterable(local_sims.filter(i => i._2.asInstanceOf.alive==1).size))
                val agents = generated.example.gameOfLife.base.InitData(edgeFilePath, 1, 1)
                API.OptimizationConfig.logControllerEnabled = true
                API.OptimizationConfig.timeseriesSchema = GoLQ2Timeseries
                API.Simulate(agents, totalTurns)
                API.Simulate.timeseries.foreach(x => println(x))
            }
        }

    }
}