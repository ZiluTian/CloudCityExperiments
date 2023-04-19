// package example
// package gameOfLife.dist

// import meta.classLifting.SpecialInstructions._
// import squid.quasi.lift
// import meta.runtime.Message

// /**
//   * Conway's game of life
//   * https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
//   * Vary communication frequency and comp. interval for microbenchmark only
//   *
//   * @param alive
//   */
// @lift
// class Cell(var alive: Int, val cfreq: Int, val cInterval: Int) extends Actor {
  
//   var aliveNeighbors: Int = 0
//     def main(): Unit = {
//         while(true) {
//             val msg = new Message()
//             msg.value = alive.toDouble
//             connectedAgentIds.foreach(i => {
//               sendMessages.getOrElseUpdate(i, new ListBuffer[Message]()).appendAll(Range(0, cfreq).map(i => {
//                   msg
//               }))
//             })
//             waitRounds(cInterval)

//             var m: Option[Message] = receiveMessage()
//             aliveNeighbors = 0
            
//             while (m.isDefined){
//               aliveNeighbors = aliveNeighbors + m.get.value.toInt
//               m = receiveMessage()
//             }

//             if (alive==1 && (aliveNeighbors > 3 || aliveNeighbors < 2)) {
//               alive = 0
//             } else if (alive==0 && aliveNeighbors==3) {
//               alive = 1
//             }
//         }
//     }
// }
