package mobwebhf.stocksimulator.data

import androidx.room.*

@Dao
interface StockDao {

    @Query("SELECT * FROM stock WHERE portfoliokey = :portfolio_key")
    fun getStocks(portfolio_key : Long) : List<StockData>

    @Query("SELECT * FROM stock WHERE portfoliokey = :portfolio_key AND stockname = :stock_name")
    fun getStock(portfolio_key: Long, stock_name : String) : List<StockData>

    @Insert
    fun addStock(stock : StockData) : Long

    @Update
    fun updateStock(stock : StockData)

    @Delete
    fun removeStock(stock : StockData)
}