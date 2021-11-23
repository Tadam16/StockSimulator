package mobwebhf.stocksimulator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.PortfolioActivity
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.databinding.PortfolioDialogBinding

class PortfolioDialogFragment(val listener: Listener) : DialogFragment() {

    private lateinit var binding : PortfolioDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding = PortfolioDialogBinding.inflate(inflater)
        binding.buttonOk.setOnClickListener{
            listener.addPortfolio(PortfolioData(null,binding.portfolioDialogName.text.toString(),
                binding.portfolioDialogCapital.text.toString().toDouble(),
                binding.portfolioDialogCapital.text.toString().toDouble(),
                binding.portfolioDialogCapital.text.toString().toDouble()))
            dismiss()
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