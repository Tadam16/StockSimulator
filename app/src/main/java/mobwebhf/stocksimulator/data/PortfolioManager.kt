package mobwebhf.stocksimulator.data

class PortfolioManager(val portfolio : PortfolioData, val db : AppDatabase, val listener : Listener) {

    fun getStockInfo(name : String) : StockData {
        return db.stockDao().getStock(portfolio.id!!, name)
    }

    fun BuyStock(name : String, quantity : Double) {

    }

    fun SellStock(name : String, quantity : Double) {

    }

    fun getQuantity(name : String) : Double {

    }

    fun getCurrentPrice(name : String) : Double {

    }

    fun getCurrentPrices() {

    }

    fun getHistoricPrices(name : String) : List<Double> {

    }

    fun getStockNameList() : List<String> {
        return emptyList()
    }

    fun getBalance() : Double {
        return portfolio.money
    }

    interface Listener{

    }

}