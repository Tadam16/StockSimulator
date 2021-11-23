package mobwebhf.stocksimulator.data

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "portfolio")
class PortfolioData (
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    @ColumnInfo(name= "portfolioname") var name : String,
    @ColumnInfo(name= "portfoliomoney") var money : Double,
    @ColumnInfo(name= "initialcapital") var capital : Double,
    @ColumnInfo(name= "portfoliovalue") var value : Double
) : Serializable
{

    val profit : Double
        get() {
            return value - capital
        }
}