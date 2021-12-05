package mobwebhf.stocksimulator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import mobwebhf.stocksimulator.data.AppDatabase
import mobwebhf.stocksimulator.data.PortfolioData
import mobwebhf.stocksimulator.data.PortfolioManager
import kotlin.concurrent.thread

class UpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        thread {
            PortfolioManager(
                PortfolioData(null, "", 0.0, 0.0, 0.0),
                AppDatabase.getInstance(context)
            ).UpdateStocks()
        }
        Log.i("mobwebhf", "receiver called")
    }
}