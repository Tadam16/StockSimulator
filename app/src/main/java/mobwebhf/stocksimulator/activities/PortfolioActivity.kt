package mobwebhf.stocksimulator.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.receivers.UpdateReceiver
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

        val cmp = PendingIntent.getBroadcast(this, 0,
            Intent(applicationContext, UpdateReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE)
        if (cmp == null) {
            val manager = getSystemService(ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(applicationContext, UpdateReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, alarmIntent, 0)
            val interval : Long = 1000 * 60 * 60
            manager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                interval,
                pendingIntent
            )

        }

        adapter = PortfolioAdapter(this)
        binding.portfolioList.layoutManager = LinearLayoutManager(this)
        binding.portfolioList.adapter = adapter
        setContentView(binding.root)

        database = AppDatabase.getInstance(applicationContext)
        loadPortfolios()

        supportActionBar?.title = getString(R.string.portfolios_title)
    }

    override fun onStart() {
        super.onStart()
        loadPortfolios()
    }

    private fun loadPortfolios(){
        thread {
            val list = database.portfolioDao().getPortfolios().toMutableList()
            runOnUiThread {
                adapter.updateDataset(list)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dialog = PortfolioDialogFragment(this)
        dialog.show(supportFragmentManager, null)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.portfolio_toolbar_menu, menu)
        return true
    }

    override fun itemRemoved(data: PortfolioData) {
        thread {
            database.portfolioDao().removePortfolio(data)
            for (stock in database.stockDao().getStocks(data.id!!))
                database.stockDao().removeStock(stock)
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