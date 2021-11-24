package mobwebhf.stocksimulator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mobwebhf.stocksimulator.adapters.PortfolioAdapter
import mobwebhf.stocksimulator.data.AppDatabase
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.databinding.PortfoliosBinding
import mobwebhf.stocksimulator.fragments.PortfolioDialogFragment
import kotlin.concurrent.thread

class PortfolioActivity : AppCompatActivity(), PortfolioAdapter.Listener, PortfolioDialogFragment.Listener {

    private lateinit var binding : PortfoliosBinding
    private lateinit var adapter: PortfolioAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PortfoliosBinding.inflate(layoutInflater)

        adapter = PortfolioAdapter(this)
        binding.portfolioList.layoutManager = LinearLayoutManager(this)
        binding.portfolioList.adapter = adapter
        setContentView(binding.root)

        database = AppDatabase.getInstance(applicationContext)
        thread {
            val list = database.portfolioDao().getPortfolios().toMutableList()
            runOnUiThread {
                adapter.updateDataset(list)
            }
        }

        supportActionBar?.title = getString(R.string.portfolios_title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialog = PortfolioDialogFragment(this)
        dialog.show(supportFragmentManager, null)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.portfolio_toolbar_menu, menu)
        return true;
    }

    override fun itemRemoved(data: PortfolioData) {
        thread {
            database.portfolioDao().removePortfolio(data)
        }
    }

    override fun itemSelected(data: PortfolioData) {
        startActivity(Intent(this, StockActivity::class.java).putExtra(StockActivity.PORTFOLIO_KEY, data))
    }

    override fun addPortfolio(data: PortfolioData) {
        thread {
            data.id = database.portfolioDao().addPortfolio(data)
            runOnUiThread{
                adapter.addPortfolio(data)
            }
        }
    }
}