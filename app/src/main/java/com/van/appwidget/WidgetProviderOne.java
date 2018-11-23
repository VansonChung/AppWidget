package com.van.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class WidgetProviderOne extends AppWidgetProvider {

    private static final String TAG = "WidgetProviderOne";

    // 添加於桌面後會 callback, 後續等待 updatePeriodMillis.
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.i(TAG, "WidgetProviderOne onUpdate");
        for (int appWidgetId : appWidgetIds) {
            // 需與 xml layout 宣告相同
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout
                    .layout_widget_one);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.layout_widget_1, pendingIntent);

            if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("login", false)) {
                views.setViewVisibility(R.id.tv_barcode, View.GONE);
                views.setViewVisibility(R.id.iv_barcode, View.GONE);
                views.setViewVisibility(R.id.tv_logout, View.VISIBLE);
            } else {
                try {
                    Bitmap bitmap = Utility.encodeAsBitmap("3345678", BarcodeFormat.CODE_128,
                            context.getResources().getDimensionPixelSize(R.dimen.cell_four),
                            context.getResources().getDimensionPixelSize(R.dimen.cell_two));
                    views.setImageViewBitmap(R.id.iv_barcode, bitmap);
                    views.setTextViewText(R.id.tv_barcode, "3345678");
                } catch (WriterException e) {
                    Log.e(TAG, "Barcode encodeAsBitmap exception : " + e.toString());
                    views.setImageViewResource(R.id.iv_barcode, R.drawable.default_widget_one);
                    views.setTextViewText(R.id.tv_barcode, e.toString());
                }
                views.setViewVisibility(R.id.tv_barcode, View.VISIBLE);
                views.setViewVisibility(R.id.iv_barcode, View.VISIBLE);
                views.setViewVisibility(R.id.tv_logout, View.GONE);
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
