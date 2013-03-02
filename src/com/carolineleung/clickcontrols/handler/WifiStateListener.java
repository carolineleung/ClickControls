package com.carolineleung.clickcontrols.handler;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.carolineleung.clickcontrols.R;

public class WifiStateListener implements WidgetActionHandler {

	@Override
	public void run(Context context, Intent intent, RemoteViews remoteViews) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			Log.d("WifiReceiver", "Have Wifi Connection");
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_on);
		} else {
			Log.d("WifiReceiver", "Don't have Wifi Connection");
			remoteViews.setImageViewResource(R.id.toggleWifi, R.drawable.toggle_wifi_off);
		}
	}
}
