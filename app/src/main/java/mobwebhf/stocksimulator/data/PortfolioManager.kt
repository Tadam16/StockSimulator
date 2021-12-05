package mobwebhf.stocksimulator.data

import mobwebhf.stocksimulator.network.NetworkManager
import java.text.DecimalFormat
import kotlin.concurrent.thread

class PortfolioManager(val portfolio : PortfolioData, val db : AppDatabase, val listener : Listener? = null) {

    companion object{
        val df = DecimalFormat("#.##")
    }

    fun BuyStock(name : String, quantity : Double, price : Double) {
        thread {
            val stocks = db.stockDao().getStock(portfolio.id!!, name)
            val stock: StockData
            val transvalue = price * quantity
            if (stocks.isEmpty()) {
                stock = StockData(null, portfolio.id!!, name, price, quantity, transvalue)
                stock.id = db.stockDao().addStock(stock)
                listener?.stockCreated(stock)
            } else {
                stock = stocks[0]
                stock.price = price
                stock.spent += transvalue
                stock.quantity += quantity
                db.stockDao().updateStock(stock)
                listener?.stockUpdated(stock)
            }
            portfolio.money -= transvalue
            UpdatePortfolio()
        }
    }

    fun SellStock(name : String, quantity : Double, price : Double) {
        thread {
            val stocks = db.stockDao().getStock(portfolio.id!!, name)
            val transvalue = price * quantity
            if (stocks.isNotEmpty()) {
                val stock = stocks[0]
                stock.price = price
                stock.quantity -= quantity
                stock.spent -= transvalue
                if (stock.quantity > 0.01) {
                    db.stockDao().updateStock(stock)
                    listener?.stockUpdated(stock)
                } else {
                    db.stockDao().removeStock(stock)
                    listener?.stockDestroyed(stock)
                }
                portfolio.money += transvalue
                UpdatePortfolio()
            }
        }
    }

    fun getQuantity(name : String) : Double{
        val stocks = db.stockDao().getStock(portfolio.id!!, name)
        if (!stocks.isEmpty()) {
            return stocks[0].quantity
        }
        return 0.0
    }

    fun getCurrentPrice(name : String) : Double {
        val response = NetworkManager.getCurrentStockPrice(name).execute()
        if(response.isSuccessful){
            return response.body()?.c?.toDouble() ?: 0.0
        }
        else {
            //todo error handling
        }
        return 0.0
    }

    fun UpdateStocks() {
        val names = db.stockDao().getStockNames()
        for(name in names){
            val price = getCurrentPrice(name)
            for(stock in db.stockDao().getStocksWithSameSymbol(name)) {
                stock.price = price
                db.stockDao().updateStock(stock)
                listener?.stockUpdated(stock)
            }
        }
        UpdatePortfolios()
    }

    private fun UpdatePortfolio(portfolio : PortfolioData = this.portfolio){
        var value = portfolio.money
        for(stock in db.stockDao().getStocks(portfolio.id!!)){
            value += stock.value
        }
        portfolio.value = value
        db.portfolioDao().updatePortfolio(portfolio)
    }

    private fun UpdatePortfolios() {
        for(portfolio in db.portfolioDao().getPortfolios()){
            UpdatePortfolio(portfolio)
        }
    }

    fun getHistoricPrices(name : String) : StockHistoryData {
        val response = NetworkManager.getStockHistory(name).execute()
        val body = response.body()
        if(response.isSuccessful && body != null)
        {
            return StockHistoryData(body.c, body.o, body.l, body.h)
        }
        else{
            //todo error handling
            return StockHistoryData(listOf(), listOf(), listOf(), listOf())
        }
    }

    fun getStockNameList() : List<String> {
        val response = NetworkManager.getStockList().execute()
        val retlist = mutableListOf<String>()
        if(response.isSuccessful){
            val elementlist = response.body()
            if(elementlist != null){
                for(element in elementlist)
                    retlist.add(element.symbol)
            }
        }
        else{
            //todo error handling
        }
        return retlist
    }

    fun getBalance() : Double {
        return portfolio.money
    }

    fun getStocks() : List<StockData> {
        return db.stockDao().getStocks(portfolio.id!!)
    }

    interface Listener{
        fun stockCreated(stock : StockData)
        fun stockDestroyed(stock : StockData)
        fun stockUpdated(stock : StockData)
    }

}