package mobwebhf.stocksimulator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.StockActivity
import mobwebhf.stocksimulator.data.StockData
import mobwebhf.stocksimulator.fragments.StockDialogFragment

class StockAdapter(val listener: Listener) : RecyclerView.Adapter<StockAdapter.ViewHolder>() {

    private val stocks = mutableListOf<StockData>()

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        val stock_name : TextView
        val stock_price : TextView
        val stock_value : TextView
        val stock_quantity : TextView
        val stock_profit : TextView
        val stock_root : View

        init {
            stock_root = v
            stock_name = v.findViewById(R.id.stock_name)
            stock_price= v.findViewById(R.id.stock_price)
            stock_quantity = v.findViewById(R.id.stock_quantity)
            stock_value = v.findViewById(R.id.stock_value)
            stock_profit = v.findViewById(R.id.stock_profit)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stocks_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockAdapter.ViewHolder, position: Int) {
        val data = stocks[position]
        holder.stock_name.text = data.name
        holder.stock_price.text = data.price.toString()
        holder.stock_profit.text =data.profit.toString()
        holder.stock_quantity.text = data.quantity.toString()
        holder.stock_value.text = data.value.toString()
        holder.stock_root.setOnClickListener {
            listener.stockSelected(data)
        }
    }

    override fun getItemCount() = stocks.size

    fun addStock(stock : StockData) {
        stocks.add(stock)
        notifyItemInserted(stocks.size-1)
    }

    fun removeStock(stock : StockData) {
        val idx = stocks.indexOf(stock)
        stocks.removeAt(idx)
        notifyItemRemoved(idx)
    }

    fun updateStock(stock : StockData) {
        for(i in 0 until stocks.size)
            if(stocks[i].id == stock.id){
                stocks[i] = stock
                return
            }
    }

    interface Listener {
        fun stockSelected(stock : StockData)
    }

}