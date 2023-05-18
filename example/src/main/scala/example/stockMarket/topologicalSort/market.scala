package example
package stockMarket
package topologicalSort

import squid.quasi.lift
import meta.classLifting.SpecialInstructions._
import squid.lib.transparencyPropagating
import scala.collection.mutable.{Map => MutMap, ListBuffer}

@lift 
class Market(val traders: List[Trader]) extends Actor {

    val stock: Stock = new Stock(0.01)
    private var futures: List[Future[Int]] = null
    private var marketState: List[Int] = null
    // Initial price
    var stockPrice: Double = 100
    var dividendPerShare: Double = 0
    val bufferedActions: ListBuffer[Int] = ListBuffer[Int]()
    var buyOrders: Int = 0
    var sellOrders: Int = 0

    @transparencyPropagating
    def traderAction(action: Int): Unit = {
        if (action == 1) {
            buyOrders = buyOrders + 1
        } else if (action == 2) {
            sellOrders = sellOrders + 1
        }
    }

    def main(): Unit = {
        markAllowDirectAccess("traderAction")
        stock.priceAdjustmentFactor = 0.1 / traders.size
        while (true) {
            marketState = stock.updateMarketInfo(stockPrice, dividendPerShare)
            traders.foreach(x => callAndForget(x.inform(stockPrice, dividendPerShare, marketState), 1))
            // wait for traders to 
            stockPrice = stock.priceAdjustment(buyOrders, sellOrders)
            dividendPerShare = stock.getDividend()
            waitAndReply(1)
        }
    }
}

