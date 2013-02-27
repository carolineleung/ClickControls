package com.carolineleung.clickcontrols;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class ClickControlsWidgetProvider extends AppWidgetProvider {

	public static final String EXTRA_CONTROL = "com.carolineleung.clickcontrols.TOGGLE";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction() == null) {
			context.startService(new Intent(context, ClickControlsWidgetService.class));
		} else {
			super.onReceive(context, intent);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		context.startService(new Intent(context, ClickControlsWidgetService.class));
	}

	// @Override
	// public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	// ComponentName thisWidget = new ComponentName(context, ClickControlsWidgetProvider.class);
	// int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	//
	// WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	//
	// for (int widgetId : allWidgetIds) {
	//
	// // boolean wifiEnabled = wifiManager.isWifiEnabled();
	// SharedPreferences prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
	// boolean wifiEnabled = prefs.getBoolean(WIFI_ENABLED, false);
	// Log.d(TAG, "Toggle State: " + wifiEnabled);
	//
	// int number = (new Random().nextInt(100));
	//
	// RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	// wifiEnabled = !wifiEnabled;
	// wifiManager.setWifiEnabled(wifiEnabled);
	// String wifiEnabledText = wifiEnabled ? "on" : "off";
	// Log.i("ClickControlsWidget", "wifi is " + wifiEnabledText + "_" + String.valueOf(number));
	// remoteViews.setViewVisibility(R.id.toggleImage, wifiEnabled ? View.INVISIBLE : View.VISIBLE);
	//
	// SharedPreferences.Editor editor = prefs.edit();
	// editor.putBoolean(WIFI_ENABLED, wifiEnabled);
	// editor.commit();
	//
	// // Register an onClickListener
	// Intent intent = new Intent(context, ClickControlsWidgetProvider.class);
	// intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	// intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
	//
	// PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	// remoteViews.setOnClickPendingIntent(R.id.control, pendingIntent);
	// appWidgetManager.updateAppWidget(widgetId, remoteViews);
	// }
	// }

	// @Override
	// public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
	// Log.w("ClickControlsWidget", "onUpdate method called");
	// // Get all ids
	// ComponentName thisWidget = new ComponentName(context, ClickControlsWidgetProvider.class);
	// int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	//
	// // Build the intent to call the service
	// Intent intent = new Intent(context.getApplicationContext(), ClickControlsWidgetService.class);
	// intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
	//
	// // Update the widgets via the service
	// context.startService(intent);
	// }
}
