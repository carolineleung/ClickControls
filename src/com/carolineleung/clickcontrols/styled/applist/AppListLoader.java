package com.carolineleung.clickcontrols.styled.applist;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

public class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {

	private ConfigurationChanges mLastConfig = new ConfigurationChanges();
	private PackageManager mPackageManager;
	private List<AppEntry> mApps;
	PackageIntentReceiver mPackageIntentReceiver;

	public static final Comparator<AppEntry> APP_LABEL_COMPARATOR = new Comparator<AppEntry>() {
		private Collator sCollator = Collator.getInstance();

		@Override
		public int compare(AppEntry entry1, AppEntry entry2) {
			return sCollator.compare(entry1.getLabel(), entry2.getLabel());
		}
	};

	public AppListLoader(Context context) {
		super(context);

		// NOTE: use global getContext() instead of the passed in context
		mPackageManager = getContext().getPackageManager();

	}

	public PackageManager getPackageManager() {
		return mPackageManager;
	}

	// Call in background thread - main work task
	@Override
	public List<AppEntry> loadInBackground() {
		// Retrieve all applications, including disabled and uninstalled ones
		List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES
				| PackageManager.GET_DISABLED_COMPONENTS);
		if (apps == null) {
			apps = new ArrayList<ApplicationInfo>();
		}

		Context context = getContext();

		List<AppEntry> entries = new ArrayList<AppEntry>(apps.size());
		for (ApplicationInfo appInfo : apps) {
			AppEntry appEntry = new AppEntry(this, appInfo);
			appEntry.loadLabel(context);
			entries.add(appEntry);
		}

		Collections.sort(entries, APP_LABEL_COMPARATOR);
		return entries;
	}

	// When there is new data to deliver to the client
	@Override
	public void deliverResult(List<AppEntry> apps) {
		if (isReset()) {
			// An async query comes in while the loader is stopped - ignore the result
			if (apps != null) {
				onReleaseResources(apps);
			}
		}

		List<AppEntry> oldApps = apps;
		mApps = apps;

		if (isStarted()) {
			// Loader has started, deliver the results now
			super.deliverResult(apps);
		}

		// No longer need the old apps, can release
		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	@Override
	protected void onStartLoading() {
		if (mApps != null) {
			// Deliver result immediately if it's available
			deliverResult(mApps);
		}

		if (mPackageIntentReceiver == null) {
			mPackageIntentReceiver = new PackageIntentReceiver(this);
		}

		// Check if there is any interesting changes in the configuration since the app list was last loaded
		boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

		if (takeContentChanged() || mApps == null || configChange) {
			// If data has changed since last load, or is currently unavailable, start a load
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(List<AppEntry> apps) {
		super.onCanceled(apps);
		// Release resources for data
		onReleaseResources(apps);
	}

	@Override
	protected void onReset() {
		super.onReset();

		// Ensure loader is stopped
		onStartLoading();

		// Release resources
		if (mApps != null) {
			onReleaseResources(mApps);
			mApps = null;
		}

		// Stop monitoring changes
		if (mPackageIntentReceiver != null) {
			getContext().unregisterReceiver(mPackageIntentReceiver);
			mPackageIntentReceiver = null;
		}
	}

	// Helper method to release resources for an actively loaded data set
	private void onReleaseResources(List<AppEntry> entries) {
		// if it's a cursor, close it
	}

}