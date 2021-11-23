package mobwebhf.stocksimulator.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface StockDao {

    @Insert
    fun addStock(stock : StockData) : Long
}