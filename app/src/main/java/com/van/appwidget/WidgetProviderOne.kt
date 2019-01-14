package com.van.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException

class WidgetProviderOne : AppWidgetProvider() {

    companion object {
        private const val TAG = "WidgetProviderOne"
    }

    // 添加於桌面後會 callback, 後續等待 updatePeriodMillis.
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i(TAG, "WidgetProviderOne onUpdate")
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.layout_widget_one)

            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            views.setOnClickPendingIntent(R.id.layout_widget_1, pendingIntent)
            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                    "login",
                    false
                )
            ) {
                try {
                    val bitmap = Utility.encodeAsBitmap(
                        "3345678", BarcodeFormat.CODE_128,
                        context.resources.getDimensionPixelSize(R.dimen.cell_four),
                        context.resources.getDimensionPixelSize(R.dimen.cell_two)
                    )
                    views.setImageViewBitmap(R.id.iv_barcode, bitmap)
                    views.setTextViewText(R.id.tv_barcode, "3345678")
                } catch (e: WriterException) {
                    Log.e(TAG, "Barcode encodeAsBitmap exception : " + e.toString())
                    views.setImageViewResource(R.id.iv_barcode, R.drawable.default_widget_one)
                    views.setTextViewText(R.id.tv_barcode, e.toString())
                }
                views.setViewVisibility(R.id.tv_barcode, View.VISIBLE)
                views.setViewVisibility(R.id.iv_barcode, View.VISIBLE)
                views.setViewVisibility(R.id.tv_logout, View.GONE)
            } else {
                views.setViewVisibility(R.id.tv_barcode, View.GONE)
                views.setViewVisibility(R.id.iv_barcode, View.GONE)
                views.setViewVisibility(R.id.tv_logout, View.VISIBLE)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}