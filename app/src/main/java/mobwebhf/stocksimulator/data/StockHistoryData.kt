package mobwebhf.stocksimulator.data

data class StockHistoryData (
    val close : List<Double>,
    val open : List<Double>,
    val low : List<Double>,
    val high : List<Double>,
)