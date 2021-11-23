package mobwebhf.stocksimulator.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio")
class PortfolioData (
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name= "portfolioname") var name : String,
    @ColumnInfo(name= "portfoliomoney") var money : Double
    //TODO store list of stocks
)
{
    val value : Double
        get() {return money}
    val profit : Double
        get() {return 0.0}
}