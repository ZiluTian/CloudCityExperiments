package example
package gameOfLifeCommFreq

import scala.collection.mutable.{Map => MutMap}

import lib.Grid.Torus2D

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(width: Int, height: Int, freq: Int): List[Actor] = {
            val totalPoints: Int = width * height
            // 2D space
            val neighborRadius: Int = 1

            val points = (1 to totalPoints).map(x => {
                if (Random.nextBoolean()){
                    new Cell(1, freq)
                } else {
                    new Cell(0, freq)
                }
            })

            val nodesSeq = points.toIndexedSeq
            points.zipWithIndex.foreach(n => {
                n._1.connectedAgentIds = Torus2D.getNeighborCells(width, height)(n._2, neighborRadius)
            })
            points.toList
        }
    }
}

object Example extends App {

  val cls1: ClassWithObject[Cell] = Cell.reflect(IR)

  val mainClass = MainInit.liftedMain

  compileSims(List(cls1), Some(mainClass))
}