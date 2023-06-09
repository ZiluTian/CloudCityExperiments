package example
package stockMarket
package base

import squid.quasi.lift
import meta.classLifting.SpecialInstructions._
import squid.lib.transparencyPropagating
import scala.collection.mutable.{Map => MutMap}
import meta.runtime.{Message, DoubleArrayMessage}

@lift 
class Market(val interval: Int, val cfreq: Int) extends Actor {

    val stock: Stock = new Stock(0.01)
    private var marketState: List[Int] = null
    // Initial price
    var stockPrice: Double = 100
    var dividendPerShare: Double = 0
    var buyOrders: Int = 0
    var sellOrders: Int = 0

    def main(): Unit = {
        stock.priceAdjustmentFactor = 0.1 / connectedAgentIds.size
        while (true) {
            buyOrders = 0
            sellOrders = 0
            var m = receiveMessage()
            while (m.isDefined){
                var ans = m.get.value
                if (ans == 1) {
                    buyOrders = buyOrders + 1
                } 
                if (ans == 2) {
                    sellOrders = sellOrders + 1
                }
                m = receiveMessage()
            }
            stockPrice = stock.priceAdjustment(buyOrders, sellOrders)
            dividendPerShare = stock.getDividend()
            // println(buyOrders + ", " + sellOrders + ", " + dividendPerShare + ", " + stockPrice)
            marketState = stock.updateMarketInfo(stockPrice, dividendPerShare)
            
            val msg = new DoubleArrayMessage(6)
            msg.doubleArrayValue(0) = id.toDouble
            msg.doubleArrayValue(1) = stockPrice
            msg.doubleArrayValue(2) = dividendPerShare
            msg.doubleArrayValue(3) = marketState(0).toDouble
            msg.doubleArrayValue(4) = marketState(1).toDouble
            msg.doubleArrayValue(5) = marketState(2).toDouble 

            connectedAgentIds.foreach(i => {
                Range(0, cfreq).foreach(j => {
                    sendMessage(i, msg)
                })
            })
            waitRounds(interval)
        }
    }
}

