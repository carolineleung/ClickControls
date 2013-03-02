package com.carolineleung.clickcontrols.handler;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.carolineleung.clickcontrols.ClickControlsWidgetProvider;
import com.carolineleung.clickcontrols.R;

public class ToggleWifiHandler implements WidgetActionHandler {

	@Override
	public void run(Context context, Intent intent, RemoteViews remoteViews) {
		Log.i(ClickControlsWidgetProvider.APP_TAG, ClickControlsWidgetProvider.ACTION_WIDGET_TOGGLE_WIFI);
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_off);
			wifiManager.setWifiEnabled(false);
		} else {
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_on);
			wifiManager.setWifiEnabled(true);
		}
	}

}
