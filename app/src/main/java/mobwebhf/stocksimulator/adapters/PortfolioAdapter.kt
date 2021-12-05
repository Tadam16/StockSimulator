package mobwebhf.stocksimulator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.data.PortfolioManager

class PortfolioAdapter(val listener : Listener) : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {



    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        val name : TextView
        val value : TextView
        val profit : TextView
        val money : TextView
        val delete : ImageButton
        val root : View

        init {
            root = v
            name = v.findViewById(R.id.portfolio_name)
            value = v.findViewById(R.id.portfolio_value)
            profit = v.findViewById(R.id.portfolio_profit)
            money = v.findViewById(R.id.portfolio_money)
            delete = v.findViewById(R.id.delete_portfolio)
        }
    }

    private var portfoliolist = mutableListOf<PortfolioData>()

    override fun getItemCount(): Int {
        return portfoliolist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.portfolio_element, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = portfoliolist[position]
        holder.name.text = data.name
        holder.value.text = PortfolioManager.df.format(data.value)
        holder.profit.text = PortfolioManager.df.format(data.profit)
        holder.money.text = PortfolioManager.df.format(data.money)
        holder.delete.setOnClickListener {
            portfoliolist.removeAt(position)
            notifyItemRemoved(position)
            listener.itemRemoved(data)
        }
        holder.root.setOnClickListener {
            listener.itemSelected(data)
        }
    }

    fun addPortfolio(portfolioData: PortfolioData){
        portfoliolist.add(portfolioData)
        notifyItemInserted(portfoliolist.size-1)
    }

    fun updateDataset(data : MutableList<PortfolioData>){
        portfoliolist = data;
        notifyDataSetChanged()
    }

    interface Listener{
        fun itemRemoved(data: PortfolioData)
        fun itemSelected(data: PortfolioData)
    }
}