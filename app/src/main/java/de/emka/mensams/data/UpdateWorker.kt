package de.emka.mensams.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.getActivity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.Worker
import androidx.work.WorkerParameters
import de.emka.mensams.BalanceWidgetProvider
import de.emka.mensams.R
import de.emka.mensams.views.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class UpdateWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    val TAG = "UpdateWorker"

    override fun doWork(): Result {
        Log.d(TAG, "doWork called")
        val prefs = applicationContext.getSharedPreferences("BALANCE_DATA", Context.MODE_PRIVATE)
        val cardNr = prefs.getString("CARD_NR", "").toString()
        BalanceUtils.getBalanceAndExecute(cardNr, ::storeAndUpdateViews)
        return Result.success()
    }

    fun storeAndUpdateViews(balance: Int, responseType: ResponseType){
        StoringAndNotifyingUtils.storeAndUpdateViews(applicationContext, balance, responseType)
    }

}