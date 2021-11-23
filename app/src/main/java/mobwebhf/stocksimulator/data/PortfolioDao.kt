package mobwebhf.stocksimulator.data

import androidx.room.*

@Dao
interface PortfolioDao {

    @Query("SELECT * FROM portfolio")
    fun getPortfolios(): List<PortfolioData>

    @Query("SELECT * FROM portfolio WHERE id = :id")
    fun getPortfolioById(id : Long) : PortfolioData

    @Insert
    fun addPortfolio(p : PortfolioData): Long

    @Update
    fun updatePortfolio(p : PortfolioData)

    @Delete
    fun removePortfolio(p : PortfolioData)
}