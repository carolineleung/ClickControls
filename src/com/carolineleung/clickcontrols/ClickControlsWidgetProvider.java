package com.carolineleung.clickcontrols;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.carolineleung.clickcontrols.handler.Toggle3GHandler;
import com.carolineleung.clickcontrols.handler.ToggleAirplaneHandler;
import com.carolineleung.clickcontrols.handler.ToggleAudioHandler;
import com.carolineleung.clickcontrols.handler.ToggleWifiHandler;
import com.carolineleung.clickcontrols.handler.WidgetActionHandler;
import com.carolineleung.clickcontrols.handler.WifiStateListener;

public class ClickControlsWidgetProvider extends AppWidgetProvider {

	// Action declarations
	public static String ACTION_WIDGET_TOGGLE_WIFI = "ToggleWifi";
	public static String ACTION_WIDGET_TOGGLE_3G = "Toggle3G";
	public static String ACTION_WIDGET_TOGGLE_AIRPLANE = "ToggleAirplane";
	public static String ACTION_WIDGET_TOGGLE_AUDIO = "ToggleAudio";

	private Map<String, WidgetActionHandler> actionMap;

	public ClickControlsWidgetProvider() {
		actionMap = new ConcurrentHashMap<String, WidgetActionHandler>();
		actionMap.put(WifiManager.NETWORK_STATE_CHANGED_ACTION, new WifiStateListener());
		actionMap.put(ConnectivityManager.CONNECTIVITY_ACTION, new WifiStateListener());
		actionMap.put(ACTION_WIDGET_TOGGLE_WIFI, new ToggleWifiHandler());
		actionMap.put(ACTION_WIDGET_TOGGLE_3G, new Toggle3GHandler());
		actionMap.put(ACTION_WIDGET_TOGGLE_AIRPLANE, new ToggleAirplaneHandler());
		actionMap.put(ACTION_WIDGET_TOGGLE_AUDIO, new ToggleAudioHandler());
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction() == null) {
			Log.i("onReceive", "Intent has null action: " + intent);
		}
		if (actionMap.containsKey(intent.getAction())) {
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			actionMap.get(intent.getAction()).run(context, intent, remoteViews);
			ComponentName componentName = new ComponentName(context, ClickControlsWidgetProvider.class);
			AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);

		} else {
			super.onReceive(context, intent);
		}

		// if (intent.getAction() == null) {
		// context.startService(new Intent(context, ClickControlsWidgetService.class));
		// } else {
		// super.onReceive(context, intent);
		// }
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		setOnClickIntent(context, remoteViews, ACTION_WIDGET_TOGGLE_WIFI, R.id.toggleWifi);
		setOnClickIntent(context, remoteViews, ACTION_WIDGET_TOGGLE_3G, R.id.toggle3g);
		setOnClickIntent(context, remoteViews, ACTION_WIDGET_TOGGLE_AIRPLANE, R.id.toggleAirplaneMode);
		setOnClickIntent(context, remoteViews, ACTION_WIDGET_TOGGLE_AUDIO, R.id.toggleAudio);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		// context.startService(new Intent(context, ClickControlsWidgetService.class));
	}

	private void setOnClickIntent(Context context, RemoteViews remoteViews, String action, int viewId) {
		Intent active = new Intent(context, ClickControlsWidgetProvider.class);
		active.setAction(action);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(viewId, actionPendingIntent);
	}
}
