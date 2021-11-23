package mobwebhf.stocksimulator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mobwebhf.stocksimulator.adapters.StockAdapter
import mobwebhf.stocksimulator.data.AppDatabase
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.databinding.StocksBinding
import mobwebhf.stocksimulator.fragments.StockDialogFragment
import kotlin.concurrent.thread

class StockActivity() : AppCompatActivity(), StockAdapter.Listener, StockDialogFragment.Listener{

    private lateinit var binding : StocksBinding
    private lateinit var database : AppDatabase
    private lateinit var portfolio : PortfolioData
    private lateinit var adapter : StockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = AppDatabase.getInstance(applicationContext)
        binding = StocksBinding.inflate(layoutInflater)
        portfolio = database.portfolioDao().getPortfolioById(intent.extras!!.get("id") as Long)

        adapter = StockAdapter(this)
        binding.stockList.layoutManager = LinearLayoutManager(this)
        binding.stockList.adapter = adapter
        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialog = StockDialogFragment(this)
        dialog.show(supportFragmentManager, null)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.stock_toolbar_menu, menu)
        return true
    }

    override fun stockSelected(stock: StockData) {
        val dialog = StockDialogFragment(this)
        dialog.show(supportFragmentManager, null)
    }

    override fun addStock(stock: StockData) {
        thread {
            stock.id = database.stockDao().addStock(stock)
            runOnUiThread{
                adapter.addStock(stock)
            }
        }
    }

    override fun modifyStock(stock: StockData) {
        TODO("Not yet implemented")
    }

    override fun getBalance(): Double {
        TODO("Not yet implemented")
    }

}