package mobwebhf.stocksimulator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.databinding.StockDialogBinding

class StockDialogFragment(val stock : StockData? = null) : DialogFragment() {

    private lateinit var binding: StockDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = StockDialogBinding.inflate(layoutInflater)

        if(stock == null) loadNewStock() else loadExistingStock()

        return binding.root
    }

    private fun loadNewStock(){

    }

    private fun loadExistingStock(){

    }
}