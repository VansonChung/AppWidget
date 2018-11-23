# AppWidget
#### https://developer.android.com/guide/topics/appwidgets/overview
#### https://developer.android.com/guide/practices/ui_guidelines/widget_design

![image](https://github.com/VansonChung/AppWidget/blob/master/app_widgets-1.jpg)    ![image](https://github.com/VansonChung/AppWidget/blob/master/app_widget-2.jpg)
 
     APP widgets 與 shortcuts 類似, 可使用客製化 layout.
     多個 widgets 就要聲明多個 receiver (網路上說用同一個是錯的).

     APP widgets 實作 : (此範例搭配會員相關, 登入狀態顯示會員 barcode, 登出則顯示請登入會員)

     1. AndroidManifest 添加 receiver

        <receiver android:name=".yourclass">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
            </intent-filter>
            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/yourxml" />
        </receiver>

     2. 新增 class (.yourclass) 繼承 AppWidgetProvider 並覆寫相關 func.

     3. res/xml/yourxml.xml 加入詳細項.

     8.0 Pinning App Widgets 實作見 code.
