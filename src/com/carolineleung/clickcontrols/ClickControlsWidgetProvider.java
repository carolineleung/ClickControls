package com.carolineleung.clickcontrols;

import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class ClickControlsWidgetProvider extends AppWidgetProvider {

	private static final String TAG = "ClickControls";
	private static final String WIFI_ENABLED = "wifi_enabled";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context, ClickControlsWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

		// if (wifiEnabled) {
		// remoteViews.setImageViewResource(R.id.btnRecordToggle, R.drawable.record_button_enabled);
		// } else {
		// remoteViews.setImageViewResource(R.id.btnRecordToggle, R.drawable.record_button);
		// }
		// ComponentName componentName = new ComponentName(context, ClickControlsWidget.class);
		// AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);

		for (int widgetId : allWidgetIds) {

			// TODO fix below when test on phone
			// boolean wifiEnabled = wifiManager.isWifiEnabled();
			SharedPreferences prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
			boolean wifiEnabled = prefs.getBoolean(WIFI_ENABLED, false);
			Log.d(TAG, "Toggle State: " + wifiEnabled);

			int number = (new Random().nextInt(100));

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			wifiEnabled = !wifiEnabled;
			wifiManager.setWifiEnabled(wifiEnabled);
			String wifiEnabledText = wifiEnabled ? "on" : "off";
			Log.i("ClickControlsWidget", "wifi is " + wifiEnabledText + "_" + String.valueOf(number));

			remoteViews.setTextViewText(R.id.indicatorText, wifiEnabledText + "_" + String.valueOf(number));
			remoteViews.setViewVisibility(R.id.toggleImage, wifiEnabled ? View.INVISIBLE : View.VISIBLE);
			remoteViews.setViewVisibility(R.id.indicatorText, wifiEnabled ? View.VISIBLE : View.INVISIBLE);

			SharedPreferences.Editor editor = prefs.edit();
			editor.putBoolean(WIFI_ENABLED, wifiEnabled);
			editor.commit();

			// Register an onClickListener
			Intent intent = new Intent(context, ClickControlsWidgetProvider.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.control, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}

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
