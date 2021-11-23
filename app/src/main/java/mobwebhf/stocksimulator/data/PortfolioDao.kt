package mobwebhf.stocksimulator.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query

@Dao
interface PortfolioDao {

    @Query("SELECT * FROM portfolio")
    fun getPortfolios(): List<PortfolioData>

    @Delete
    fun removePortfolio(p : PortfolioData)
}