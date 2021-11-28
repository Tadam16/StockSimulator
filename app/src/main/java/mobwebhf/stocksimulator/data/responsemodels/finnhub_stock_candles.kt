package mobwebhf.stocksimulator.data.responsemodels

data class finnhub_stock_candles (
    val close_prices : List<Double>,
    val high_prices : List<Double>,
    val low_prices : List<Double>,
    val open_prices : List<Double>,
    val status : String,
    val timestamps : List<Long>,
    val volumes : List<Double>
)