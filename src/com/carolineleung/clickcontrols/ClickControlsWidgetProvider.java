package com.carolineleung.clickcontrols;

import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

public class ClickControlsWidgetProvider extends AppWidgetProvider {

	private static final String ACTION_CLICK = "ACTION_CLICK";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context, ClickControlsWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		boolean wifiEnabled = wifiManager.isWifiEnabled();

		for (int widgetId : allWidgetIds) {
			int number = (new Random().nextInt(100));

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			wifiEnabled = !wifiEnabled;
			wifiManager.setWifiEnabled(wifiEnabled);
			String wifiEnabledText = wifiEnabled ? "on" : "off";
			Log.i("ClickControlsWidget", "wifi is " + wifiEnabledText + "_" + String.valueOf(number));

			remoteViews.setTextViewText(R.id.indicator, wifiEnabledText + "_" + String.valueOf(number));

			// Register an onClickListener
			Intent intent = new Intent(context, ClickControlsWidgetProvider.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.control, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}
