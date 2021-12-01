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
import com.github.mikephil.charting.components.Legend

import com.github.mikephil.charting.components.XAxis

import com.github.mikephil.charting.components.YAxis

import com.github.mikephil.charting.charts.CandleStickChart




class StockDialogFragment(
    val manager : PortfolioManager,
    var stockname : String? = null
) : DialogFragment() {

    private lateinit var binding: StockDialogBinding

    private var balance : Double = 0.0
    private var currentQuantity : Double = 0.0
    private var currentPrice : Double = 0.0
    private var PriceHistory : StockHistoryData? = null
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
            if(stockname == null)
                StockList = manager.getStockNameList()
            else
                StockList = mutableListOf()
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

        if(PriceHistory != null) {
            val entries = mutableListOf<CandleEntry>()
            for (i in PriceHistory!!.close.indices) {
                entries.add(
                    CandleEntry(
                        i.toFloat(),
                        PriceHistory!!.high[i].toFloat(),
                        PriceHistory!!.low[i].toFloat(),
                        PriceHistory!!.open[i].toFloat(),
                        PriceHistory!!.close[i].toFloat()
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

        binding.stockInput.setAdapter(ArrayAdapter(requireActivity().applicationContext, R.layout.stock_autocomplete_list_element,StockList))
        binding.stockInput.setOnItemClickListener { adapterView, view, i, l -> binding.stockInput.performValidation()} //todo completion handling


        if(stockname != null){
            binding.stockInput.isEnabled = false
            binding.stockInput.setText(stockname)
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
        binding.stockDialogSellButton.isEnabled = quantity <= currentQuantity && quantity > 0
        binding.stockDialogBuyButton.isEnabled = transactionValue <= balance && quantity > 0
        binding.stockDialogTransactionValue.text =
            getString(R.string.stock_dialogtransaction_value, transactionValue.toString())
    }

}