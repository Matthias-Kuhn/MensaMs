package de.emka.mensams.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import de.emka.mensams.widget_providers.BalanceWidgetProvider
import de.emka.mensams.R
import de.emka.mensams.widget_providers.SimpleWidgetBlueProvider
import de.emka.mensams.widget_providers.SimpleWidgetGreenProvider
import de.emka.mensams.widget_providers.SimpleWidgetProvider
import java.text.SimpleDateFormat
import java.util.*

object StoringAndNotifyingUtils {

    private const val NOTIFICATION_NAME = "MensaMs"
    private const val NOTIFICATION_CHANNEL_ID = "MensaMs_balance_changes_channel"

    fun storeAndUpdateViews(applicationContext: Context, balance: Int, responseType: ResponseType){
        if (responseType == ResponseType.SUCCESSFUL_RESPONSE){
            val prefs = applicationContext.getSharedPreferences("BALANCE_DATA", Context.MODE_PRIVATE)
            val oldBalance = prefs.getInt("BALANCE", 0)
            val editor = prefs.edit()
            editor.putInt("BALANCE", balance)
            editor.commit()
            updateWidgets(applicationContext)
            if (oldBalance != balance) showNotification(applicationContext, balance, oldBalance)

        }
    }

    fun updateWidgets(context: Context) {
        updateIntent(context, SimpleWidgetProvider::class.java)
        updateIntent(context, SimpleWidgetGreenProvider::class.java)
        updateIntent(context, SimpleWidgetBlueProvider::class.java)
    }

    private fun updateIntent(context: Context, providerClass: Class<*>){
        val intent = Intent(context, providerClass)
        intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, providerClass))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids)
        context.sendBroadcast(intent)
    }

    fun showNotification(applicationContext: Context, balance: Int, balanceOld: Int) {
        createNotificationChannel(applicationContext)

        val notificationId = SimpleDateFormat("ddHHmmss", Locale.GERMANY).format(Date()).toInt()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationTitle = applicationContext.getString(R.string.notification_title)

        val contentText = applicationContext.getString(R.string.notification_body,
            BalanceUtils.intToString(balance-balanceOld),
            BalanceUtils.intToString(balance))



        val notificationBuilder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)

        notificationBuilder
            .setContentTitle(notificationTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_notification)
        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT


        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun createNotificationChannel(applicationContext: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                applicationContext.getString(R.string.balance_changes_channel_name),
                importance)
            notificationChannel.description = applicationContext.getString(R.string.notification_channel_description)
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }




}