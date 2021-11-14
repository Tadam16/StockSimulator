package mobwebhf.stocksimulator.data

class PortfolioData (
    val name : String,
    val money : Double
    //TODO store list of stocks
)
{
    val value : Double
        get() {return money}
    val profit : Double
        get() {return 0.0}
}