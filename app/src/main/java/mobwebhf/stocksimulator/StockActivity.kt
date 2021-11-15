package mobwebhf.stocksimulator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mobwebhf.stocksimulator.adapters.StockAdapter
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.databinding.StocksBinding
import mobwebhf.stocksimulator.fragments.StockDialogFragment

class StockActivity(val portfolio : PortfolioData) : AppCompatActivity(){

    private lateinit var binding : StocksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StocksBinding.inflate(layoutInflater)

        val adapter = StockAdapter(this)
        binding.stockList.layoutManager = LinearLayoutManager(this)
        binding.stockList.adapter = adapter
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialog = StockDialogFragment()
        dialog.show(supportFragmentManager, null)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.stock_toolbar_menu, menu)
        return true
    }

}