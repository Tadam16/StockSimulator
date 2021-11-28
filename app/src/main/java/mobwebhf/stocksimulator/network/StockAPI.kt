package mobwebhf.stocksimulator.network

import mobwebhf.stocksimulator.data.responsemodels.finnhub_stock_candles
import mobwebhf.stocksimulator.data.responsemodels.finnhub_stock_quote
import mobwebhf.stocksimulator.data.responsemodels.finnhub_stock_symbols
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StockAPI {

    @GET("api/v1/stock/symbol")
    fun getSymbolList(@Query("token") apikey : String,
                      @Query("exchange") exchange : String,
                      @Query("currency") currency : String

    ) : Call<List<finnhub_stock_symbols>>

    @GET("api/v1/quote")
    fun getStockPrice(@Query("token") apikey : String,
                      @Query("symbol") symbol: String) : Call<finnhub_stock_quote>

    @GET("api/v1/stock/candle")
    fun getStockHistory(@Query("token") apikey : String,
                        @Query("symbol") symbol : String,
                        @Query("resolution") resolution : String,
                        @Query("from") from : Long,
                        @Query("to") to : Long
    ) : Call<finnhub_stock_candles>

}