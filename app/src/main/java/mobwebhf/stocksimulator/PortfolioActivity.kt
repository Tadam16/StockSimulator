package mobwebhf.stocksimulator

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import mobwebhf.stocksimulator.adapters.PortfolioAdapter
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.databinding.PortfoliosBinding

class PortfolioActivity : AppCompatActivity() {

    private lateinit var binding : PortfoliosBinding
    private lateinit var adapter: PortfolioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PortfoliosBinding.inflate(layoutInflater)
        adapter = PortfolioAdapter()
        binding.portfolioList.layoutManager = LinearLayoutManager(this)
        binding.portfolioList.adapter = adapter
        setContentView(binding.root)
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

    fun newPortfolio(p : PortfolioData){
        adapter.addPortfolio(p)
    }
}