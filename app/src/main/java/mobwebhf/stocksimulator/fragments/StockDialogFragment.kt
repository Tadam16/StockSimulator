package mobwebhf.stocksimulator.fragments

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioManager
import mobwebhf.stocksimulator.data.StockHistoryData
import mobwebhf.stocksimulator.databinding.StockDialogBinding
import kotlin.concurrent.thread

class StockDialogFragment(
    val manager : PortfolioManager,
    var stockname : String = ""
) : DialogFragment() {

    private lateinit var binding: StockDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = StockDialogBinding.inflate(layoutInflater)

        initView()

        return binding.root
    }

    private fun lockBuy(){
        binding.stockDialogQuantity.isEnabled = false
        binding.stockDialogSellButton.isEnabled = false
        binding.stockDialogBuyButton.isEnabled = false
    }

    private fun unlockBuy(){
        binding.stockDialogQuantity.isEnabled = true
        binding.stockDialogQuantity.text = binding.stockDialogQuantity.text
    }

    private fun initView() {
        lockBuy()

        binding.stockDialogBalance.text =
            getString(R.string.stock_dialog_balance, PortfolioManager.df.format(manager.getBalance()))

        if(stockname == ""){
            thread {
                manager.getStockNameList(::stockNamesLoaded)
            }
        }else{
            stockSelected()
        }

    }

    private fun validateOptions(price : Double, currentQuantity : Double){
        val text = binding.stockDialogQuantity.text
        var quantity = 0.0
        if (text.isNotEmpty()) {
            quantity = text.toString().toDouble()
        }
        val transactionValue = quantity * price
        binding.stockDialogSellButton.isEnabled = quantity <= currentQuantity && quantity > 0
        binding.stockDialogBuyButton.isEnabled = transactionValue <= manager.getBalance() && quantity > 0
        binding.stockDialogTransactionValue.text =
            getString(R.string.stock_dialogtransaction_value, transactionValue.toString())
    }

    private fun stockSelected(){
        binding.stockInput.setText(stockname)
        binding.stockInput.isEnabled = false
        thread{
            val price = manager.getCurrentPrice(stockname)
            val quantity = manager.getQuantity(stockname)
            stockDataLoaded(price, quantity)
        }
        thread {
            val history = manager.getHistoricPrices(stockname)
            stockHistoryLoaded(history)
        }
    }

    private fun loadChart(PriceHistory : StockHistoryData){
        val entries = mutableListOf<CandleEntry>()
        for (i in PriceHistory.close.indices) {
            entries.add(
                CandleEntry(
                    i.toFloat(),
                    PriceHistory.high[i].toFloat(),
                    PriceHistory.low[i].toFloat(),
                    PriceHistory.open[i].toFloat(),
                    PriceHistory.close[i].toFloat()
                )
            )
        }

        val set = CandleDataSet(entries, "Dataset")
        set.color = Color.rgb(80, 80, 80)
        set.shadowColor = Color.DKGRAY
        set.shadowWidth = 0.8f
        set.decreasingColor = Color.RED
        set.decreasingPaintStyle = Paint.Style.FILL
        set.increasingColor = Color.GREEN
        set.increasingPaintStyle = Paint.Style.FILL
        set.neutralColor = Color.DKGRAY
        binding.stockDialogHistoryChart.data = CandleData(set)
        binding.stockDialogHistoryChart.xAxis.setDrawGridLines(false)
        binding.stockDialogHistoryChart.xAxis.setDrawLabels(false)
        binding.stockDialogHistoryChart.axisLeft.setDrawLabels(false)
        binding.stockDialogHistoryChart.legend.isEnabled = false
        binding.stockDialogHistoryChart.description.isEnabled = false
        binding.stockDialogHistoryChart.invalidate()
    }

    fun stockNamesLoaded(stocknames : List<String>){
        try {
            requireActivity().runOnUiThread {
                binding.stockInput.validator = object : AutoCompleteTextView.Validator {

                    override fun isValid(p0: CharSequence?): Boolean {
                        val ret = stocknames.contains(p0.toString())
                        if (ret) {
                            stockname = p0.toString()
                            stockSelected()
                        } else
                            lockBuy()
                        return ret
                    }

                    override fun fixText(p0: CharSequence?): CharSequence {
                        return p0 ?: ""
                    }
                }

                binding.stockInput.setAdapter(
                    ArrayAdapter(
                        requireActivity().applicationContext,
                        R.layout.stock_autocomplete_list_element,
                        stocknames
                    )
                )
                binding.stockInput.setOnItemClickListener { adapterView, view, i, l -> binding.stockInput.performValidation() } //todo completion handling

            }
        }
        catch (e : Throwable){
            Log.e("debug", e.printStackTrace().toString())
        }

    }

    fun stockDataLoaded(price : Double, currentQuantity : Double){
        try {
            requireActivity().runOnUiThread {
                binding.stockdialogPrice.text =
                    getString(R.string.price_string, PortfolioManager.df.format(price), PortfolioManager.df.format(currentQuantity))

                binding.stockDialogQuantity.addTextChangedListener {
                    validateOptions(price, currentQuantity)
                }

                binding.stockDialogBuyButton.setOnClickListener {
                    val quantity = binding.stockDialogQuantity.text.toString().toDouble()
                    manager.BuyStock(stockname!!, quantity, price)
                    dismiss()
                }

                binding.stockDialogSellButton.setOnClickListener {
                    val quantity = binding.stockDialogQuantity.text.toString().toDouble()
                    manager.SellStock(stockname!!, quantity, price)
                    dismiss()
                }
                unlockBuy()
            }
        }
        catch (e : Throwable){
            Log.e("debug", e.printStackTrace().toString())
        }
    }

    fun stockHistoryLoaded(history : StockHistoryData){
        try {
            requireActivity().runOnUiThread {
                loadChart(history)
            }
        }
        catch (e : Throwable){
            Log.e("debug", e.printStackTrace().toString())
        }
    }

}