package com.carolineleung.clickcontrols.styled.applist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.IntentCompat;

/**
 * Subscribe to interesting changes to the installed apps - for updating the loader
 * 
 */
public class PackageIntentReceiver extends BroadcastReceiver {

	private AppListLoader mLoader;

	public PackageIntentReceiver(AppListLoader mLoader) {
		this.mLoader = mLoader;

		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		mLoader.getContext().registerReceiver(this, filter);

		IntentFilter sdcardFilter = new IntentFilter();
		sdcardFilter.addAction(IntentCompat.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
		sdcardFilter.addAction(IntentCompat.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
		mLoader.getContext().registerReceiver(this, sdcardFilter);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// Inform the loader of the content changes
		mLoader.onContentChanged();
	}

}