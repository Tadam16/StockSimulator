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
        return 10.0
    }

    fun getCurrentPrice(name : String) : Double {
        return 100.0
    }

    fun getCurrentPrices() {

    }

    fun getHistoricPrices(name : String) : List<Double> {
        return emptyList()
    }

    fun getStockNameList() : List<String> {
        return listOf("stock1", "intel", "amd", "meszaros&meszaros")
    }

    fun getBalance() : Double {
        return portfolio.money
    }

    interface Listener{

    }

}