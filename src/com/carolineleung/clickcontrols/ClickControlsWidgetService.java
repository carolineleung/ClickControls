package com.carolineleung.clickcontrols;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.widget.RemoteViews;

public class ClickControlsWidgetService extends IntentService {

	public ClickControlsWidgetService() {
		super("ClickControlsWidgetService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected void onHandleIntent(Intent intent) {
		ComponentName me = new ComponentName(this, ClickControlsWidgetProvider.class);
		AppWidgetManager mgr = AppWidgetManager.getInstance(this);
		mgr.updateAppWidget(me, buildUpdate(this));
	}

	private RemoteViews buildUpdate(Context context) {
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

		AudioManager audioManager = (AudioManager) context.getSystemService(Activity.AUDIO_SERVICE);
		if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
			updateViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_on);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		} else {
			updateViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_off);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		}

		Intent intent = new Intent(this, ClickControlsWidgetProvider.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		updateViews.setOnClickPendingIntent(R.id.toggleWifi, pi);

		return updateViews;
	}

	// @Override
	// public void onStart(Intent intent, int startId) {
	// Log.i(LOG, "ClickControlsWidgetProvider called");
	//
	// AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
	//
	// int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
	//
	// ComponentName thisWidget = new ComponentName(getApplicationContext(), ClickControlsWidgetProvider.class);
	// int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
	// Log.w(LOG, "From Intent" + String.valueOf(allWidgetIds.length));
	// Log.w(LOG, "Direct" + String.valueOf(allWidgetIds2.length));
	//
	// for (int widgetId : allWidgetIds) {
	// RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);
	//
	// // Register an onClickListener
	// Intent clickIntent = new Intent(this.getApplicationContext(), ClickControlsWidgetProvider.class);
	//
	// clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
	// clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
	//
	// PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	// remoteViews.setOnClickPendingIntent(R.id.control, pendingIntent);
	//
	// appWidgetManager.updateAppWidget(widgetId, remoteViews);
	// }
	//
	// stopSelf();
	//
	// super.onStart(intent, startId);
	// }

}
