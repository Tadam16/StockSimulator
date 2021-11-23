package mobwebhf.stocksimulator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.databinding.StockDialogBinding

class StockDialogFragment(val listener : Listener, val stock : StockData? = null, val balance : Double = 0.0) : DialogFragment() {

    private lateinit var binding: StockDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = StockDialogBinding.inflate(layoutInflater)

        binding.stockDialogBalance.text = getString(R.string.stock_dialog_balance, balance.toString())
        if(stock == null) loadNewStock() else loadExistingStock(stock)

        binding.stockDialogQuantity.addTextChangedListener {
            val text = it.toString()
            var arg = "0"
            if(text.isNotEmpty()){
                arg = (text.toDouble() * binding.stockdialogPrice.text.toString().toDouble()).toString()
            }
            binding.stockDialogTransactionValue.text = getString(R.string.stock_dialogtransaction_value, arg)
        }
        binding.stockDialogBuyButton.setOnClickListener {

        }
        binding.stockDialogSellButton.setOnClickListener {

        }

        return binding.root
    }

    private fun loadNewStock(){
        binding.stockDialogQuantity.isEnabled = false


    }

    private fun loadExistingStock(stock : StockData){
        binding.stockInput.setText(stock.name)
        binding.stockInput.isEnabled = false
        binding.stockdialogPrice.text = getString(R.string.price_string, stock.price.toString(), stock.quantity.toString())

    }

    interface Listener{
        fun addStock(stock : StockData)
        fun modifyStock(stock : StockData)
    }
}