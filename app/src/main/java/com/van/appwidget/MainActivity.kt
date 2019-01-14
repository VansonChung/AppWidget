package com.van.appwidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.bt_login -> setLogin(true)
            R.id.bt_logout -> setLogin(false)
            R.id.bt_pinning -> {
                // 直接添加 widget
                Utility.pinningWidget(this)
                return
            }
        }
        updateWidget()
    }

    private fun setLogin(login: Boolean) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("login", login)
            .apply()
    }

    private fun updateWidget() {
        // 動態修改 Widget, 只會覆蓋這次 "有" 修改的內容 (onClick 還在)
        val thisWidget = ComponentName(this, WidgetProviderOne::class.java)
        val views = RemoteViews(this.packageName, R.layout.layout_widget_one)

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("login", false)) {
            try {
                val bitmap = Utility.encodeAsBitmap(
                    "3345678",
                    BarcodeFormat.CODE_128,
                    resources.getDimensionPixelSize(R.dimen.cell_four),
                    resources.getDimensionPixelSize(R.dimen.cell_two)
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
        AppWidgetManager.getInstance(this).updateAppWidget(thisWidget, views)
    }
}