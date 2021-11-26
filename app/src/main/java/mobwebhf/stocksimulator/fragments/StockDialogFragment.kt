package mobwebhf.stocksimulator.fragments

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.data.PortfolioManager
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.databinding.StockDialogBinding
import javax.xml.validation.Validator
import kotlin.concurrent.thread

class StockDialogFragment(
    val manager : PortfolioManager,
    val stockname : String? = null
) : DialogFragment() {

    private lateinit var binding: StockDialogBinding

    private var balance : Double = 0.0
    private var currentQuantity : Double = 0.0
    private var currentPrice : Double = 0.0
    private lateinit var PriceHistory : List<Double>
    private lateinit var StockList : List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = StockDialogBinding.inflate(layoutInflater)

        lockBuy()

        thread{
            balance = manager.getBalance()
            StockList = manager.getStockNameList()
            if(stockname != null){
                currentQuantity = manager.getQuantity(stockname)
                currentPrice = manager.getCurrentPrice(stockname)
                PriceHistory = manager.getHistoricPrices(stockname)
            }
            requireActivity().runOnUiThread {
                initViewElements()
            }
        }

        return binding.root
    }

    private fun lockBuy(){

    }

    private fun unlockBuy(){

    }

    private fun initViewElements() {
        binding.stockDialogBalance.text =
            getString(R.string.stock_dialog_balance, balance.toString())


        binding.stockDialogQuantity.addTextChangedListener {
            val text = it.toString()
            var arg = 0.0
            if (text.isNotEmpty()) {
                arg = text.toDouble() * currentPrice
            }
            validateoptions(arg)
            binding.stockDialogTransactionValue.text =
                getString(R.string.stock_dialogtransaction_value, arg.toString())

        }

        binding.stockDialogBuyButton.setOnClickListener {
            val quantity = binding.stockDialogQuantity.text.toString().toDouble()
            manager.BuyStock(stockname!!, quantity)
            dismiss()
        }

        binding.stockDialogSellButton.setOnClickListener {
            val quantity = binding.stockDialogQuantity.text.toString().toDouble()
            manager.SellStock(stockname!!, quantity)
            dismiss()
        }

        binding.stockInput.setAdapter(
            object : AutoCompleteTextView.Adapter, Filterable, ListAdapter {

            }
        )

        if(stockname != null){
            binding.stockInput.isActivated = false
            unlockBuy()
        }
    }

    private fun validateoptions(quantity : Double) {

    }

    interface Listener {
        fun addStock(stock: StockData)
        fun modifyStock(stock: StockData)
        fun removeStock(stock: StockData)
        fun getContext() : Context
    }
}