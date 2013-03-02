package com.carolineleung.clickcontrols;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

public class ClickControlsWidgetProvider extends AppWidgetProvider {

	// our actions for our buttons
	public static String ACTION_WIDGET_TOGGLE_WIFI = "ToggleWifi";
	public static String ACTION_WIDGET_TOGGLE_3G = "Toggle3G";
	public static String ACTION_WIDGET_TOGGLE_AIRPLANE = "ToggleAirplane";
	public static String ACTION_WIDGET_TOGGLE_AUDIO = "ToggleAudio";

	public static interface ActionTastic {
		void run();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

		if (intent.getAction().equals(ACTION_WIDGET_TOGGLE_WIFI)) {
			toggleWifi(context, remoteViews);

		} else if (intent.getAction().equals(ACTION_WIDGET_TOGGLE_3G)) {
			toggle3G(context, remoteViews);

		} else if (intent.getAction().equals(ACTION_WIDGET_TOGGLE_AIRPLANE)) {
			toggleAirplane(context, remoteViews);

		} else if (intent.getAction().equals(ACTION_WIDGET_TOGGLE_AUDIO)) {
			toggleAudio(context, remoteViews);

		} else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)
				|| intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			updateWifiIcon(context, remoteViews);

		} else {
			super.onReceive(context, intent);
		}

		// Map<String, ActionTastic> actionMap = null;
		// actionMap.get(intent.getACtion).run()

		ComponentName componentName = new ComponentName(context, ClickControlsWidgetProvider.class);
		AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);

		// if (intent.getAction() == null) {
		// context.startService(new Intent(context, ClickControlsWidgetService.class));
		// } else {
		// super.onReceive(context, intent);
		// }
	}

	private void updateWifiIcon(Context context, RemoteViews remoteViews) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			Log.d("WifiReceiver", "Have Wifi Connection");
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_on);

		} else {
			Log.d("WifiReceiver", "Don't have Wifi Connection");
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_off);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void toggle3G(Context context, RemoteViews remoteViews) {
		Log.i("onReceive", ACTION_WIDGET_TOGGLE_3G);
		ConnectivityManager connectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Class connectMgrClass;
		try {
			connectMgrClass = Class.forName(connectivityMgr.getClass().getName());
			Field iConnectivityManagerField = connectMgrClass.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);

			Object iConnectivityManager = iConnectivityManagerField.get(connectivityMgr);
			Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());

			Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
				setMobileDataEnabledMethod.invoke(iConnectivityManager, false);
				remoteViews.setImageViewResource(R.id.toggle3g, R.drawable.toggle_3g_off);

			} else if (telephonyManager.getDataState() == TelephonyManager.DATA_DISCONNECTED) {
				setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
				remoteViews.setImageViewResource(R.id.toggle3g, R.drawable.toggle_3g_on);
			}
		} catch (Exception e) {
			Log.e("onReceive", "Exception when toggling 3G: " + e);
		}

	}

	private void toggleAudio(Context context, RemoteViews remoteViews) {
		Log.i("onReceive", ACTION_WIDGET_TOGGLE_AIRPLANE);
		AudioManager audioManager = (AudioManager) context.getSystemService(Activity.AUDIO_SERVICE);
		if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
			remoteViews.setImageViewResource(R.id.toggleAudio, R.drawable.toggle_sound_on);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		} else {
			remoteViews.setImageViewResource(R.id.toggleAudio, R.drawable.toggle_sound_off);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}
	}

	private void toggleAirplane(Context context, RemoteViews remoteViews) {
		boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
		if (isEnabled) {
			remoteViews.setImageViewResource(R.id.toggleAirplaneMode, R.drawable.toggle_airplane_on);
		} else {
			remoteViews.setImageViewResource(R.id.toggleAirplaneMode, R.drawable.toggle_airplane_off);
		}

		// Post an intent to reload
		// Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		// intent.putExtra("state", !isEnabled);
		// context.sendBroadcast(intent);

		Intent airplaneSettingsIntent = new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS);
		airplaneSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(airplaneSettingsIntent);
	}

	private void toggleWifi(Context context, RemoteViews remoteViews) {
		Log.i("onReceive", ACTION_WIDGET_TOGGLE_WIFI);
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_off);
			wifiManager.setWifiEnabled(false);
		} else {
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_on);
			wifiManager.setWifiEnabled(true);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// context.startService(new Intent(context, ClickControlsWidgetService.class));

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

		Intent active = new Intent(context, ClickControlsWidgetProvider.class);
		active.setAction(ACTION_WIDGET_TOGGLE_WIFI);
		PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.toggleWifi, actionPendingIntent);

		active = new Intent(context, ClickControlsWidgetProvider.class);
		active.setAction(ACTION_WIDGET_TOGGLE_3G);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.toggle3g, actionPendingIntent);

		active = new Intent(context, ClickControlsWidgetProvider.class);
		active.setAction(ACTION_WIDGET_TOGGLE_AIRPLANE);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.toggleAirplaneMode, actionPendingIntent);

		active = new Intent(context, ClickControlsWidgetProvider.class);
		active.setAction(ACTION_WIDGET_TOGGLE_AUDIO);
		actionPendingIntent = PendingIntent.getBroadcast(context, 0, active, 0);
		remoteViews.setOnClickPendingIntent(R.id.toggleAudio, actionPendingIntent);

		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
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
