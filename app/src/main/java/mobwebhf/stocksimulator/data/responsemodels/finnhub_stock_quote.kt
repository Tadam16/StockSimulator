package mobwebhf.stocksimulator.data.responsemodels

data class finnhub_stock_quote (
    val current_price : Double,
    val change : Double,
    val percent_change : Double,
    val day_high_price : Double,
    val day_low_price : Double,
    val day_open_price : Double,
    val previous_close_price : Double
    )