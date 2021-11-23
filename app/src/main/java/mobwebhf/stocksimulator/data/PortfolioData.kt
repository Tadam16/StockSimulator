package mobwebhf.stocksimulator.data

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "portfolio")
class PortfolioData (
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name= "portfolioname") var name : String,
    @ColumnInfo(name= "portfoliomoney") var money : Double,
    @ColumnInfo(name= "portfolioStocks") var stocks : MutableList<StockData> = mutableListOf<StockData>()
)
{
    val value : Double
        get() {return money}
    val profit : Double
        get() {return 0.0}

    companion object{
        @JvmStatic
        @TypeConverter
        fun stocksToString(list : List<StockData>) : String {
            val gson = Gson()
            return gson.toJson(list)
        }

        @JvmStatic
        @TypeConverter
        fun stringToStocks(s : String) : List<StockData> {
            val gson = Gson()
            return gson.fromJson(s, object : TypeToken<ArrayList<StockData>>() {}.type)
        }
    }
}