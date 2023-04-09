package example
package gameOfLife.base

import scala.collection.mutable.{Map => MutMap}
import scala.io.Source
import cloudcity.lib.Graph.LoadGraph

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(edgeFilePath: String, cfreq: Int, interval: Int): List[Actor] = {
            val edges = LoadGraph(edgeFilePath)
            edges.map(i => {
                val cell = if (Random.nextBoolean) {
                    new Cell(1, cfreq, interval)
                } else {
                    new Cell(0, cfreq, interval)
                }
                cell.id = i._1
                cell.connectedAgentIds = i._2
                cell
            }).toList
        }
    }
}

object Example extends App {
  val cls1: ClassWithObject[Cell] = Cell.reflect(IR)
  val mainClass = MainInit.liftedMain
  compileSims(List(cls1), Some(mainClass))
}