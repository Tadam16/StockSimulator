package mobwebhf.stocksimulator.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.databinding.StockDialogBinding
import javax.xml.validation.Validator

class StockDialogFragment(
    val listener: Listener,
    val portfolio: PortfolioData,
    val stock: StockData? = null
) : DialogFragment() {

    private lateinit var binding: StockDialogBinding
    val price = 100.0 //todo calculate price

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = StockDialogBinding.inflate(layoutInflater)

        binding.stockDialogBalance.text =
            getString(R.string.stock_dialog_balance, portfolio.money.toString())

        val newstock = stock == null
        if (newstock) loadNewStock() else loadExistingStock(stock!!)

        binding.stockDialogQuantity.addTextChangedListener {
            val text = it.toString()
            var arg = "0"
            if (text.isNotEmpty()) {
                arg = (text.toDouble() * binding.stockdialogPrice.text.toString()
                    .toDouble()).toString()
            }
            binding.stockDialogTransactionValue.text =
                getString(R.string.stock_dialogtransaction_value, arg)
        }

        binding.stockDialogBuyButton.setOnClickListener {

            val quantity = binding.stockDialogQuantity.text.toString().toDouble()
            val transvalue = quantity * price
            if (transvalue > portfolio.money) {
                //todo error message
            } else {
                portfolio.money -= transvalue
                val tmpstock = StockData(
                    null,
                    portfolio.id!!,
                    binding.stockInput.text.toString(),
                    price,
                    quantity,
                    price * quantity
                )
                if (newstock)
                    listener.addStock(tmpstock)
                else {
                    tmpstock.quantity += stock!!.quantity
                    tmpstock.spent += stock.spent
                    listener.modifyStock(tmpstock)
                }
                dismiss()
            }
        }

        binding.stockDialogSellButton.setOnClickListener {
            val quantity = binding.stockDialogQuantity.text.toString().toDouble()
            val transvalue = quantity * price
            val tmpstock = stock!!
            if (quantity > tmpstock.quantity) {
                //todo error message
            } else {
                portfolio.money += transvalue
                tmpstock.quantity -= quantity
                tmpstock.price = price
                tmpstock.spent -= transvalue //todo this way?
                if(tmpstock.quantity == 0.0) { //todo better check
                    listener.removeStock(tmpstock)
                }
                else{
                    listener.modifyStock(tmpstock)
                }
            }
        }

        return binding.root
    }

    private fun loadNewStock() {
        binding.stockDialogQuantity.isEnabled = false

        val stocknames = mutableListOf<String>("Stock1", "Stock2", "Stock3") //todo get real list of stock names
        //load list of selections into edittext
        binding.stockInput.setAdapter(ArrayAdapter<String>(listener.getContext(), R.layout.stock_autocomplete_list_element,stocknames))
        //detect selection
        binding.stockInput.validator = object : AutoCompleteTextView.Validator{
            override fun isValid(p0: CharSequence?): Boolean {
                return stocknames.contains(p0)
            }

            override fun fixText(p0: CharSequence?): CharSequence {
                return p0 ?: ""
            }
        }
        //callback function for item selected


    }

    private fun loadExistingStock(stock: StockData) {
        binding.stockInput.setText(stock.name)
        binding.stockInput.isEnabled = false
        binding.stockdialogPrice.text =
            getString(R.string.price_string, stock.price.toString(), stock.quantity.toString())

    }

    private fun quantityValidator(quantity : Double) : Boolean{
        val currentQuantity = stock?.quantity ?: 0.0
        if(-quantity > currentQuantity)
            return false
        val transValue = quantity * price
        if(transValue > portfolio.money)
            return false
        return true
    }

    interface Listener {
        fun addStock(stock: StockData)
        fun modifyStock(stock: StockData)
        fun removeStock(stock: StockData)
        fun getContext() : Context
    }
}