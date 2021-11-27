package mobwebhf.stocksimulator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioManager
import mobwebhf.stocksimulator.databinding.StockDialogBinding
import kotlin.concurrent.thread

class StockDialogFragment(
    val manager : PortfolioManager,
    var stockname : String? = null
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
            loadStockData()
        }

        return binding.root
    }

    private fun loadStockData(){
        thread{
            if(stockname != null){
                currentQuantity = manager.getQuantity(stockname!!)
                currentPrice = manager.getCurrentPrice(stockname!!)
                PriceHistory = manager.getHistoricPrices(stockname!!)
            }
            requireActivity().runOnUiThread {
                initViewElements()
            }
        }
    }

    private fun lockBuy(){
        binding.stockDialogQuantity.isEnabled = false
        binding.stockDialogSellButton.isEnabled = false
        binding.stockDialogBuyButton.isEnabled = false
    }

    private fun unlockBuy(){
        binding.stockDialogQuantity.isEnabled = true
        validateOptions()
    }

    private fun initViewElements() {
        binding.stockDialogBalance.text =
            getString(R.string.stock_dialog_balance, balance.toString())

        binding.stockdialogPrice.text =
            getString(R.string.price_string, currentPrice.toString(), currentQuantity.toString())

        binding.stockDialogQuantity.addTextChangedListener {
            validateOptions()
        }

        binding.stockDialogBuyButton.setOnClickListener {
            val quantity = binding.stockDialogQuantity.text.toString().toDouble()
            manager.BuyStock(stockname!!, quantity, currentPrice)
            dismiss()
        }

        binding.stockDialogSellButton.setOnClickListener {
            val quantity = binding.stockDialogQuantity.text.toString().toDouble()
            manager.SellStock(stockname!!, quantity, currentPrice)
            dismiss()
        }

        binding.stockInput.validator = object : AutoCompleteTextView.Validator {

            override fun isValid(p0: CharSequence?): Boolean {
                val ret = StockList.contains(p0.toString())
                if(ret) {
                    stockname = p0.toString()
                    loadStockData()
                    unlockBuy()
                }
                else
                    lockBuy()
                return ret
            }

            override fun fixText(p0: CharSequence?): CharSequence {
                return p0 ?: ""
            }
        }

        binding.stockInput.setAdapter(ArrayAdapter(requireActivity().applicationContext, R.layout.stock_autocomplete_list_element,StockList))
        binding.stockInput.setOnItemClickListener { adapterView, view, i, l -> binding.stockInput.performValidation()} //todo completion handling


        if(stockname != null){
            binding.stockInput.isEnabled = false
            unlockBuy()
        }
    }

    private fun validateOptions(){
        val text = binding.stockDialogQuantity.text
        var quantity = 0.0
        if (text.isNotEmpty()) {
            quantity = text.toString().toDouble()
        }
        val transactionValue = quantity * currentPrice
        binding.stockDialogSellButton.isEnabled = quantity <= currentQuantity
        binding.stockDialogBuyButton.isEnabled = transactionValue <= balance
        binding.stockDialogTransactionValue.text =
            getString(R.string.stock_dialogtransaction_value, quantity.toString())
    }

}