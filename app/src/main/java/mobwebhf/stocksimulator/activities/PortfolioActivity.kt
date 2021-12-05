package mobwebhf.stocksimulator.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.UpdateReceiver
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

        /*val filter = IntentFilter("mobwebhf.stocksimulator.updatestocks")
        registerReceiver(UpdateReceiver(), filter)
        sendBroadcast(Intent("mobwebhf.stocksimulator.updatestocks"))
        if (PendingIntent.getBroadcast(this, 0,
                Intent("mobewbhf.stocksimulator.updatestocks"),
                PendingIntent.FLAG_NO_CREATE) == null) {
            val intent = Intent("mobewbhf.stocksimulator.updatestocks")
            val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
            val alarm = this.getSystemService(ALARM_SERVICE) as AlarmManager
            val interval: Long = 3600
            alarm.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + interval,
                interval,
                pending
            )

            val filter = IntentFilter("mobwebhf.stocksimulator.updatestocks")
            registerReceiver(object : BroadcastReceiver {

                                                        }, filter)
        }*/

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

    override fun onStart() {
        super.onStart()
        adapter.notifyDataSetChanged()
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