package mobwebhf.stocksimulator.activities

import android.content.DialogInterface
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
import kotlin.concurrent.thread

class StockActivity() : AppCompatActivity(), StockAdapter.Listener{

    companion object {
        const val PORTFOLIO_KEY = "StockActivityPortfolioKey"
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

        adapter.initStocks(PortfolioManager(portfolio,database,adapter))
        binding.stockList.layoutManager = LinearLayoutManager(this)
        binding.stockList.adapter = adapter
        UpdatePortfolioData()
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.stocks_title, portfolio.name)
    }

    private fun UpdatePortfolioData(){
        binding.tvStockDialog.text = getString(R.string.tv_stock_dialog,
            PortfolioManager.df.format(portfolio.value),
            PortfolioManager.df.format(portfolio.money),
            PortfolioManager.df.format(portfolio.profit))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_stock_button -> {
                val dialog = StockDialogFragment(PortfolioManager(portfolio, database, adapter))
                dialog.show(supportFragmentManager, null)
            }
            R.id.update_stock_button -> {
                thread {
                    PortfolioManager(portfolio, database, adapter).UpdateStocks()
                }
            }
        }
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