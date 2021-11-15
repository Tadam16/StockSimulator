package mobwebhf.stocksimulator.data

class StockData(
    val name: String,
    var price: Double,
    var quantity: Double,
    var spent: Double
    //TODO store historic data
) {
    val value: Double
        get() = price * quantity
    val profit: Double
        get() = value - spent
}