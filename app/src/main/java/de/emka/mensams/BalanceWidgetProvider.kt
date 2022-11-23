package de.emka.mensams

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import de.emka.mensams.data.BalanceUtils
import java.text.SimpleDateFormat
import java.util.*

class BalanceWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Perform this loop procedure for each widget that belongs to this
        // provider.
        appWidgetIds.forEach { appWidgetId ->

            val sharedPreferences = context.getSharedPreferences("BALANCE_DATA", Context.MODE_PRIVATE)

            val balance = sharedPreferences.getInt("BALANCE", 0)
            val sdf = SimpleDateFormat("dd.MM   HH:mm")
            val currentDate = sdf.format(Date())

            // update textViews on every widget
            appWidgetIds.forEach { appWidgetId ->
                val textViews: RemoteViews = RemoteViews(
                    context.packageName,
                    R.layout.simple_balance_widget
                ).apply {
                    setTextViewText(R.id.tv_balance,BalanceUtils.intToString(balance))
                    setTextViewText(R.id.tv_date, currentDate)
                }
                appWidgetManager.updateAppWidget(appWidgetId, textViews)
            }

        }
    }
}
