package de.emka.mensams.widget_providers

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import de.emka.mensams.R
import de.emka.mensams.data.BalanceUtils

class SimpleWidgetGreenProvider: BalanceWidgetProvider() {


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
                setTextViewText(R.id.wg_tv_balance, BalanceUtils.intToString(balance))
                setTextViewText(R.id.wg_tv_date, date)
            }
            appWidgetManager.updateAppWidget(appWidgetId, textViews)
        }
    }


}