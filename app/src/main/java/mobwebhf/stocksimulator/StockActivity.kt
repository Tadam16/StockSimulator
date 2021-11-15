package mobwebhf.stocksimulator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.databinding.StocksBinding

class StockActivity(val portfolio : PortfolioData) : AppCompatActivity(){

    private lateinit var binding : StocksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StocksBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //todo menus and stuff

}