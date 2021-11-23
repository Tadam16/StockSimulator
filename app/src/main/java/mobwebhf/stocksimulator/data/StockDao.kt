package mobwebhf.stocksimulator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface StockDao {

    @Insert
    fun addStock(stock : StockData) : Long

    @Update
    fun updateStock(stock : StockData)

    @Delete
    fun removeStock(stock : StockData)
}