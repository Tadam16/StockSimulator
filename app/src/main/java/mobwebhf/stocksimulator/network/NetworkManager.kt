package mobwebhf.stocksimulator.network

import mobwebhf.stocksimulator.data.responsemodels.finnhub_stock_candles
import mobwebhf.stocksimulator.data.responsemodels.finnhub_stock_quote
import mobwebhf.stocksimulator.data.responsemodels.finnhub_stock_symbols
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkManager {
    private val retrofit : Retrofit
    private val stockAPI : StockAPI

    private const val API_URL = "https://finnhub.io"
    private const val API_KEY = "c6hottqad3ia9lmm11lg"

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        stockAPI = retrofit.create(StockAPI::class.java)
    }

    fun getStockList() : Call<List<finnhub_stock_symbols>> {
        return stockAPI.getSymbolList(API_KEY,"US", "USD")
    }

    fun getCurrentStockPrice(symbol: String) : Call<finnhub_stock_quote> {
        return stockAPI.getStockPrice(API_KEY, symbol)
    }

    fun getStockHistory(symbol : String) : Call<finnhub_stock_candles> {
        val time = System.currentTimeMillis()/1000
        return stockAPI.getStockHistory(API_KEY, symbol, "D", time - 3600*24*360, time)
    }
}