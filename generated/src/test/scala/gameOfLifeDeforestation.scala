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
        val mode: Int = args(0).toInt
        val totalTurns: Int = 200
        var role: String = "Standalone"
        var port: Int = 25251

        mode match {
            case 1 => {
                // timeseries
                List((10, 100), (50, 100), (100, 100), (100, 500), (100, 1000)).foreach(p => {
                    val agents = generated.example.gameOfLife.base.InitData(p._1, p._2, 1, 1)
                    API.OptimizationConfig.logControllerEnabled = true
                    API.OptimizationConfig.timeseriesSchema = FullTimeseries
                    // default to time series
                    val snapshot1 = Simulate(agents, totalTurns)
                    // API.Simulate.timeseries.foreach(x => println(x.map(i => i.asInstanceOf[generated.example.gameOfLife.base.Cell].alive)))
                })
            }

            case 2 => {
                // Q1 snapshot
                List((10, 100), (50, 100), (100, 100), (100, 500), (100, 1000)).foreach(p => {
                    val agents = generated.example.gameOfLife.base.InitData(p._1, p._2, 1, 1)
                    API.OptimizationConfig.logControllerEnabled = true
                    API.OptimizationConfig.timeseriesSchema = GoLQ1Timeseries
                    API.Simulate(agents, totalTurns)
                    // API.Simulate.timeseries.foreach(x => println(x))
                })
            }

            case 3 => {
                // Q2
                List((10, 100), (50, 100), (100, 100), (100, 500), (100, 1000)).foreach(p => {
                    val agents = generated.example.gameOfLife.base.InitData(p._1, p._2, 1, 1)
                    API.OptimizationConfig.logControllerEnabled = true
                    API.OptimizationConfig.timeseriesSchema = GoLQ2Timeseries
                    API.Simulate(agents, totalTurns)
                    // API.Simulate.timeseries.foreach(x => println(x))
                })
            }
        }

    }
}