package com.carolineleung.clickcontrols.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.carolineleung.clickcontrols.ClickControlsWidgetProvider;
import com.carolineleung.clickcontrols.R;

public class Toggle3GHandler implements WidgetActionHandler {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run(Context context, Intent intent, RemoteViews remoteViews) {
		Log.i("onReceive", ClickControlsWidgetProvider.ACTION_WIDGET_TOGGLE_3G);
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

}
