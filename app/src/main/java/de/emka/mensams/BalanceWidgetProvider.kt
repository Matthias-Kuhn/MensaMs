package de.emka.mensams

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class BalanceWidgetProvider  : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform this loop procedure for each widget that belongs to this
        // provider.
        appWidgetIds.forEach { appWidgetId ->

            val balance = 1234
            val sdf = SimpleDateFormat("dd.MM   HH:mm:ss")
            val currentDate = sdf.format(Date())

            // update textViews on every widget
            appWidgetIds.forEach { appWidgetId ->
                val textViews: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.simple_balance_widget
                ).apply {
                    setTextViewText(R.id.tv_balance, balance.toString())
                    setTextViewText(R.id.tv_date, currentDate)
                }
                appWidgetManager.updateAppWidget(appWidgetId, textViews)
            }

        }
    }
}
