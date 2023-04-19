package example
package gameOfLifeRPCOneSideDoubleBuffer

import meta.classLifting.SpecialInstructions._
import squid.quasi.lift
import squid.lib.transparencyPropagating
import scala.collection.mutable.{Map => MutMap}

@lift
class Cell(var alive: Int) extends Actor {    

    var aliveNeighbors: Array[Int] = new Array[Int](2)

    @transparencyPropagating
    def tell(key: Int, state: Int): Int = {
      aliveNeighbors(key) = aliveNeighbors(key) + state
      0
    }
    
    def main(): Unit = {
      markAllowDirectAccess("tell")
        while(true) {
          connectedAgents.map(x => 
            x.asInstanceOf[Cell]).foreach(v => callAndForget(v.tell((time.toInt+1) % 2, alive), 1)
          )
          waitRounds(1)
          aliveNeighbors((time.toInt+1)%2) = 0
          handleRPC()          
          if (alive==1 && (aliveNeighbors(time.toInt % 2) > 3 || aliveNeighbors(time.toInt % 2) < 2)) {
            alive = 0
          } else if (alive==0 && aliveNeighbors(time.toInt % 2) ==3) {
            alive = 1
          }
          waitRounds(1)
        }
    }
}