package mobwebhf.stocksimulator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.R
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.databinding.PortfolioDialogBinding

class PortfolioDialogFragment(val listener: Listener) : DialogFragment() {

    private lateinit var binding : PortfolioDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding = PortfolioDialogBinding.inflate(inflater)
        binding.buttonOk.setOnClickListener{
            val capital =  binding.portfolioDialogCapital.text.toString()
            val name =  binding.portfolioDialogName.text.toString()
            if(capital.isNotEmpty() && name.isNotEmpty()) {
                listener.addPortfolio(
                    PortfolioData(
                        null, name,
                        capital.toDouble(),
                        capital.toDouble(),
                        capital.toDouble()
                    )
                )
                dismiss()
            }
            else{
                Toast.makeText(context, getString(R.string.portfolio_add_error_toast), Toast.LENGTH_LONG).show()
            }
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    interface Listener{
        fun addPortfolio(data : PortfolioData)
    }
}