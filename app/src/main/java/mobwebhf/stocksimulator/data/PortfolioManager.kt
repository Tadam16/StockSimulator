package mobwebhf.stocksimulator.data

class PortfolioManager(val portfolio : PortfolioData, val db : AppDatabase, val listener : Listener) {


    fun BuyStock(name : String, quantity : Double, price : Double) {
        val stocks = db.stockDao().getStock(portfolio.id!!, name)
        val stock : StockData
        val transvalue = price*quantity
        if(stocks.isEmpty()) {
            stock = StockData(null, portfolio.id!!, name, price, quantity, transvalue)
            stock.id = db.stockDao().addStock(stock)
            listener.stockCreated(stock)
        }
        else {
            stock = stocks[0]
            stock.price = price
            stock.spent += transvalue
            stock.quantity += quantity
            db.stockDao().updateStock(stock)
            listener.stockUpdated(stock)
        }
        portfolio.money -= transvalue
        db.portfolioDao().updatePortfolio(portfolio)

    }

    fun SellStock(name : String, quantity : Double, price : Double) {
        val stocks = db.stockDao().getStock(portfolio.id!!, name)
        val transvalue = price*quantity
        if(stocks.isEmpty()) {
            //todo error
        }
        val stock = stocks[0]
        stock.price = price
        stock.quantity -= quantity
        stock.spent -= transvalue
        if(stock.quantity > 0) {
            db.stockDao().updateStock(stock)
            listener.stockUpdated(stock)
        }
        else {
            db.stockDao().removeStock(stock)
            listener.stockDestroyed(stock)
        }
        portfolio.money -= transvalue
        db.portfolioDao().updatePortfolio(portfolio)
    }

    fun getQuantity(name : String) : Double {
        val stocks = db.stockDao().getStock(portfolio.id!!, name)
        if(stocks.isEmpty())
            return 0.0
        return stocks[0].quantity
    }

    fun getCurrentPrice(name : String) : Double {
        return 100.0 //todo network query
    }

    fun getCurrentPrices() {
        //todo network query
    }

    fun getHistoricPrices(name : String) : List<Double> {
        return emptyList() //todo network query
    }

    fun getStockNameList() : List<String> {
        return listOf("stock1", "intel", "amd", "meszaros&meszaros") //todo network query
    }

    fun getBalance() : Double {
        return portfolio.money
    }

    interface Listener{
        fun stockCreated(stock : StockData)
        fun stockDestroyed(stock : StockData)
        fun stockUpdated(stock : StockData)
    }

}