package example
package gameOfLifeRPCOneSideMultiversion

import scala.collection.mutable.{Map => MutMap}

object MainInit {
    val liftedMain = meta.classLifting.liteLift {
        def apply(width: Int, height: Int): List[Actor] = {
            val totalPoints: Int = width * height
            // 2D space
            val neighborRadius: Int = 1

            val points = (1 to totalPoints).map(x => {
                if (Random.nextBoolean()){
                    new Cell(1)
                } else {
                    new Cell(0)
                }
            }).toList
            val offset: Int = points(0).id.toInt
            val graph = cloudcity.lib.Graph.GenerateGraph.Torus2DGraph(width, height, offset)
            points.foreach(p => {
                p.connectedAgents = graph(p.id).map(i => points(i.toInt - offset))
            })
            points
        }
    }
}

object Example extends App {

  val cls1: ClassWithObject[Cell] = Cell.reflect(IR)

  val mainClass = MainInit.liftedMain

  compileSims(List(cls1), Some(mainClass))
}