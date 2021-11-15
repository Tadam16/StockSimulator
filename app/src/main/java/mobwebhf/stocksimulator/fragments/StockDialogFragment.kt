package mobwebhf.stocksimulator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mobwebhf.stocksimulator.databinding.StocksBinding

class StockDialogFragment : Fragment() {

    private lateinit var binding: StocksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = StocksBinding.inflate(layoutInflater)

        return binding.root
    }
}