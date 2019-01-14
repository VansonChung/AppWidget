package com.van.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

object Utility {

    @Throws(WriterException::class)
    fun encodeAsBitmap(
        barcode: String,
        format: BarcodeFormat,
        desiredWidth: Int,
        desiredHeight: Int
    ): Bitmap? {
        if (barcode.isEmpty()) return null
        val WHITE = 0xFFFFFFFF.toInt()
        val BLACK = 0xFF000000.toInt()
        var hints: HashMap<EncodeHintType, String>? = null
        var encoding: String? = null
        for (i in barcode) {
            if (i.toInt() > 0xFF) {
                encoding = "UTF-8"
                break
            }
        }
        if (encoding != null) {
            hints = HashMap(2)
            hints[EncodeHintType.CHARACTER_SET] = encoding
        }
        val writer = MultiFormatWriter()
        val result = writer.encode(barcode, format, desiredWidth, desiredHeight, hints)
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (i in 0 until height) {
            val offset = i * width
            for (j in 0 until width) {
                pixels[offset + j] = if (result.get(j, i)) BLACK else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    fun pinningWidget(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mAppWidgetManager = context.getSystemService(AppWidgetManager::class.java)
            val myProvider = ComponentName(context, WidgetProviderOne::class.java)
            if (mAppWidgetManager.isRequestPinAppWidgetSupported) {
                // 同 pinned shortcuts, 成功添加會收到 broadcast.
                val successCallback = PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(context, PinningReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                mAppWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
            }
        }
    }
}