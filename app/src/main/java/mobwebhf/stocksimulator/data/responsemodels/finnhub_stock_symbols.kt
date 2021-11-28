package mobwebhf.stocksimulator.data.responsemodels

data class finnhub_stock_symbols(
    val currency: String,
    val description: String,
    val displaySymbol: String,
    val figi: String,
    val mic: String,
    val symbol: String,
    val type: String,
)