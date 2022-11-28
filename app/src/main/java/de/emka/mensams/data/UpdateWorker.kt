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
        if (responseType == ResponseType.SUCCESSFUL_RESPONSE){
            val prefs = applicationContext.getSharedPreferences("BALANCE_DATA", Context.MODE_PRIVATE)
            val oldBalance = prefs.getInt("BALANCE", 0)
            val editor = prefs.edit()
            editor.putInt("BALANCE", balance)
            editor.commit()
            updateWidgets()
            if (oldBalance != balance) showNotification(oldBalance-balance)
        }
    }
    fun updateWidgets() {
        val intent = Intent(applicationContext, BalanceWidgetProvider::class.java)
        intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
        val ids = AppWidgetManager.getInstance(applicationContext).getAppWidgetIds(ComponentName(applicationContext, BalanceWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids)
        applicationContext.sendBroadcast(intent)
    }

    fun showNotification(diff:Int) {
        createNotificationChannel()

        val notificationId = SimpleDateFormat("ddHHmmss", Locale.GERMANY).format(Date()).toInt()

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationTitle = "Zahlung mit der MensaCard"
        val contentText = "Du hast ${BalanceUtils.intToString(diff)} bezahlt."
        //val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)


        val notificationBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)

        notificationBuilder
            .setContentTitle(notificationTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_notification)
        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT


        notificationManager.notify(notificationId, notificationBuilder.build())

    }

    private fun createNotificationChannel() {
        if (SDK_INT >= O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, importance)
            notificationChannel.description = "test Beschreibung"
            val notificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }
}