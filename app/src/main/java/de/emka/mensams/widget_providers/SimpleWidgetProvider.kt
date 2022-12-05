package de.emka.mensams.widget_providers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import de.emka.mensams.R
import de.emka.mensams.data.BalanceUtils

class SimpleWidgetProvider: BalanceWidgetProvider() {


    override fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
        balance: Int,
        date: String
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val appInfo = appWidgetManager.getAppWidgetInfo(appWidgetId)
            val textViews: RemoteViews = RemoteViews(
                context.packageName,
                appInfo.initialLayout
            ).apply {
                setTextViewText(R.id.tv_balance, BalanceUtils.intToString(balance))
                setTextViewText(R.id.tv_date, date)
            }
            appWidgetManager.updateAppWidget(appWidgetId, textViews)
        }
    }


}