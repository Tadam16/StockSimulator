package mobwebhf.stocksimulator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock")
class StockData(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "portfoliokey") var key : Long,
    @ColumnInfo(name = "stockname") val name: String,
    @ColumnInfo(name= "stockprice") var price: Double,
    @ColumnInfo(name= "stockquantity") var quantity: Double,
    @ColumnInfo(name= "spent") var spent: Double
    //TODO store historic data - are we sure we want that?
) {
    val value: Double
        get() = price * quantity
    val profit: Double
        get() = value - spent
}