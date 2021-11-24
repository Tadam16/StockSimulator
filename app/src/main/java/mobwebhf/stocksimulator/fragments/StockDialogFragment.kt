package mobwebhf.stocksimulator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.databinding.StockDialogBinding

class StockDialogFragment(
    val listener: Listener,
    val portfolio: PortfolioData,
    val stock: StockData? = null
) : DialogFragment() {

    private lateinit var binding: StockDialogBinding
    val price = 0.0 //todo calculate price

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = StockDialogBinding.inflate(layoutInflater)

        binding.stockDialogBalance.text =
            getString(R.string.stock_dialog_balance, portfolio.money.toString())

        val newstock = stock == null;
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
                    tmpstock.spent += stock!!.spent
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

        //load list of selections into edittext
        //detect selection
        //callback function for item selected


    }

    private fun loadExistingStock(stock: StockData) {
        binding.stockInput.setText(stock.name)
        binding.stockInput.isEnabled = false
        binding.stockdialogPrice.text =
            getString(R.string.price_string, stock.price.toString(), stock.quantity.toString())

    }

    interface Listener {
        fun addStock(stock: StockData)
        fun modifyStock(stock: StockData)
        fun removeStock(stock: StockData)
    }
}