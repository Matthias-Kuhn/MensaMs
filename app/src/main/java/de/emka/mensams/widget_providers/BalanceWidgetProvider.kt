package de.emka.mensams.widget_providers

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

abstract class BalanceWidgetProvider : AppWidgetProvider() {

    abstract fun updateWidget(context: Context,
                              appWidgetManager: AppWidgetManager,
                              appWidgetIds: IntArray,
                              balance: Int,
                              date: String)

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
            updateWidget(context,appWidgetManager, appWidgetIds, balance, currentDate)
        }
    }
}
