package mobwebhf.stocksimulator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import mobwebhf.stocksimulator.databinding.PortfolioDialogBinding

class PortfolioDialogFragment : DialogFragment() {

    private lateinit var binding : PortfolioDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding = PortfolioDialogBinding.inflate(inflater)
        return binding.root
    }
}