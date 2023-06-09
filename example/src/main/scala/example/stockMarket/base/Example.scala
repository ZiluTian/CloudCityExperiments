package example
package stockMarket
package base

import scala.collection.mutable.ListBuffer

object Example extends App {
    val liftedMain = meta.classLifting.liteLift {
        def apply(totalMarkets: Int, tradersPerMarket: Int, cfreq: Int, cinterval: Int): IndexedSeq[Actor] = {
            val initialWealth: Double = 1000
            val interestRate: Double = 0.001

            Range(0, totalMarkets).flatMap(i => {
                val traders = (1 to tradersPerMarket).map(x => new Trader(initialWealth, interestRate, cinterval))
                val market = new Market(cinterval, cfreq)
                market.connectedAgentIds = traders.map(i => i.id.toInt).map(_.toLong)
                market +: traders
            })
        }
    }
    
    val cls1: ClassWithObject[Market] = Market.reflect(IR)
    val cls2: ClassWithObject[Trader] = Trader.reflect(IR)

    compileSims(List(cls1, cls2), Some(liftedMain))
}