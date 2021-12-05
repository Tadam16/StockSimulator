package mobwebhf.stocksimulator.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val manager = context.applicationContext.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context.applicationContext, UpdateReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context.applicationContext, 0, alarmIntent, 0)
        val interval : Long = 1000 * 60 * 60
        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            interval,
            pendingIntent
        )
        Log.i("mobwebhf", "Boot completed")
    }
}