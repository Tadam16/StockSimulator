package mobwebhf.stocksimulator.data.responsemodels

data class finnhub_stock_candles (
    val c : List<Double>,
    val h : List<Double>,
    val l : List<Double>,
    val o : List<Double>,
    val s : String,
    val t : List<Long>,
    val v : List<Double>
)