package mobwebhf.stocksimulator.data

import android.content.Context
import androidx.room.*

@Database(entities = [StockData::class, PortfolioData::class], version = 2)
@TypeConverters(value = [PortfolioData::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun portfolioDao(): PortfolioDao
    abstract fun stockDao(): StockDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

}