package example
package gameOfLife.base

import scala.collection.mutable.{Map => MutMap}
import scala.io.Source
import cloudcity.lib.Graph.LoadGraph

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(width: Int, height: Int, cfreq: Int, interval: Int): IndexedSeq[Actor] = {
            Range(0, width * height).map(i => {
                val cell = if (Random.nextBoolean) {
                    new Cell(1, cfreq, interval)
                } else {
                    new Cell(0, cfreq, interval)
                }
                cell.connectedAgentIds = {
                    val x = cell.id % width
                    val y = cell.id / width

                    for {
                        i <- -1 to 1
                        j <- -1 to 1
                        if !(i == 0 && j == 0)
                            dx = (x + i + width) % width
                            dy = (y + j + height) % height
                    } yield dy * width + dx
                }
                cell
            })
        }
    }
}

object Example extends App {
  val cls1: ClassWithObject[Cell] = Cell.reflect(IR)
  val mainClass = MainInit.liftedMain
  compileSims(List(cls1), Some(mainClass))
}