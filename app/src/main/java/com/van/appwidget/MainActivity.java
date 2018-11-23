package com.van.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                setLogin(true);
                break;
            case R.id.bt_logout:
                setLogin(false);
                break;
            case R.id.bt_pinning:
                Utility.pinningWidget(this);
                return;
        }
        updateWidget();
    }

    private void setLogin(boolean login) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("login",
                login).apply();
    }

    private void updateWidget() {
        // 動態修改 Widget, 只會覆蓋這次 "有" 修改的內容 (onClick 還在)
        ComponentName thisWidget = new ComponentName(this, WidgetProviderOne.class);
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout
                .layout_widget_one);

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("login", false)) {
            try {
                Bitmap bitmap = Utility.encodeAsBitmap("3345678", BarcodeFormat.CODE_128,
                        getResources().getDimensionPixelSize(R.dimen.cell_four), getResources()
                                .getDimensionPixelSize(R.dimen.cell_two));
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
        } else {
            views.setViewVisibility(R.id.tv_barcode, View.GONE);
            views.setViewVisibility(R.id.iv_barcode, View.GONE);
            views.setViewVisibility(R.id.tv_logout, View.VISIBLE);
        }
        AppWidgetManager.getInstance(this).updateAppWidget(thisWidget, views);
    }
}
