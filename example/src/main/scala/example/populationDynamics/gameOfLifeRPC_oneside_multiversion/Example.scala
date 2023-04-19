package example
package gameOfLifeRPCOneSideMultiversion

import scala.collection.mutable.{Map => MutMap}

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(width: Int, height: Int): IndexedSeq[Actor] = {
            val cells = Range(0, width * height).map(x => {
                val cell = if (Random.nextBoolean()){
                    new Cell(1)
                } else {
                    new Cell(0)
                }
                cell.id = x
                cell
            })

            cells.map(cell => {
                cell.connectedAgents = {
                        val x = cell.id % width
                        val y = cell.id / width
                        for {
                            i <- -1 to 1
                            j <- -1 to 1
                            if !(i == 0 && j == 0)
                                dx = (x + i + width) % width
                                dy = (y + j + height) % height
                        } yield dy * width + dx
                    }.map(i => cells(i.toInt))
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