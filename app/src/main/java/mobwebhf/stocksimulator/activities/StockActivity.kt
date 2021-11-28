package mobwebhf.stocksimulator.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.adapters.StockAdapter
import mobwebhf.stocksimulator.data.AppDatabase
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.data.PortfolioManager
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.databinding.StocksBinding
import mobwebhf.stocksimulator.fragments.StockDialogFragment

class StockActivity() : AppCompatActivity(), StockAdapter.Listener{

    companion object {
        val PORTFOLIO_KEY = "StockActivityPortfolioKey"
    }

    private lateinit var binding : StocksBinding
    private lateinit var database : AppDatabase
    private lateinit var portfolio : PortfolioData
    private val adapter = StockAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        portfolio = intent.getSerializableExtra(PORTFOLIO_KEY) as PortfolioData
        database = AppDatabase.getInstance(applicationContext)
        binding = StocksBinding.inflate(layoutInflater)

        binding.stockList.layoutManager = LinearLayoutManager(this)
        binding.stockList.adapter = adapter
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.stocks_title, portfolio.name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialog = StockDialogFragment(PortfolioManager(portfolio, database, adapter))
        dialog.show(supportFragmentManager, null)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.stock_toolbar_menu, menu)
        return true
    }

    override fun stockSelected(stock: StockData) {
        val dialog = StockDialogFragment(PortfolioManager(portfolio, database, adapter), stock.name)
        dialog.show(supportFragmentManager, null)
    }

}