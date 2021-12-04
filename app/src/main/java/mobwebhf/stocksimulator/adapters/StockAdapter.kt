package mobwebhf.stocksimulator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioManager
import mobwebhf.stocksimulator.data.StockData
import kotlin.concurrent.thread

class StockAdapter(val listener: Listener) : RecyclerView.Adapter<StockAdapter.ViewHolder>(), PortfolioManager.Listener {

    private var stocks = mutableListOf<StockData>()

    fun initStocks(portfolioManager: PortfolioManager){
        thread {
            stocks = portfolioManager.getStocks().toMutableList()
            notifyDataSetChanged()
        }
    }

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

    interface Listener {
        fun stockSelected(stock : StockData)
        fun runOnUiThread(r : Runnable)
    }

    override fun stockCreated(stock: StockData) {
        listener.runOnUiThread {
            stocks.add(stock)
            notifyItemInserted(stocks.size - 1)
        }
    }

    override fun stockDestroyed(stock: StockData) {
        listener.runOnUiThread {
            val idx = stocks.indexOf(stock)
            stocks.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }

    override fun stockUpdated(stock: StockData) {
        listener.runOnUiThread {
            for (i in 0 until stocks.size)
                if (stocks[i].id == stock.id) {
                    stocks[i] = stock
                    notifyItemChanged(i)
                    break
                }
        }
    }

}