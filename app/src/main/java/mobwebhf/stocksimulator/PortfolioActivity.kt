package mobwebhf.stocksimulator

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import mobwebhf.stocksimulator.databinding.PortfoliosBinding

class PortfolioActivity : AppCompatActivity() {

    private lateinit var binding : PortfoliosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = PortfoliosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.portfolio_toolbar_menu, menu)
        return true;
    }
}