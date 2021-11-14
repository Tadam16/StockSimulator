package mobwebhf.stocksimulator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioData

class PortfolioAdapter() : RecyclerView.Adapter<PortfolioAdapter.ViewHolder>() {

    class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        val name : TextView
        val value : TextView
        val profit : TextView
        val money : TextView
        val delete : Button

        init {
            name = v.findViewById(R.id.portfolio_name)
            value = v.findViewById(R.id.portfolio_value)
            profit = v.findViewById(R.id.portfolio_profit)
            money = v.findViewById(R.id.portfolio_money)
            delete = v.findViewById(R.id.delete_portfolio)
        }
    }

    private val portfoliolist = mutableListOf<PortfolioData>()

    override fun getItemCount(): Int {
        return portfoliolist.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.portfolio_element, parent)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = portfoliolist[position]
        holder.name.text = data.name
        holder.value.text = data.value.toString()
        holder.profit.text = data.profit.toString()
        holder.money.text = data.money.toString()
        holder.delete.setOnClickListener {
            portfoliolist.removeAt(position)
            notifyItemRemoved(position)
            //TODO persistent delete
        }
    }

    fun addPortfolio(portfolioData: PortfolioData){
        portfoliolist.add(portfolioData)
        notifyItemInserted(portfoliolist.size-1)
    }
}