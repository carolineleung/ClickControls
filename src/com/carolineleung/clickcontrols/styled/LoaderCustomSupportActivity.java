package com.carolineleung.clickcontrols.styled;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.pm.ActivityInfoCompat;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoaderCustomSupportActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fragmentMgr = getSupportFragmentManager();
		if (fragmentMgr.findFragmentById(android.R.id.content) == null) {

		}
	}

	public static class SubscribedConfigChanges {
		private Configuration mLastConfiguration = new Configuration();
		private int mLastDensity;

		boolean applyNewConfig(Resources resources) {
			int configChanges = mLastConfiguration.updateFrom(resources.getConfiguration());
			boolean densityChanged = mLastDensity != resources.getDisplayMetrics().densityDpi;
			if (densityChanged
					|| (configChanges & (ActivityInfo.CONFIG_LOCALE | ActivityInfoCompat.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
				mLastDensity = resources.getDisplayMetrics().densityDpi;
				return true;
			}
			return false;
		}
	}

	public static class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {

		private SubscribedConfigChanges mLastConfig = new SubscribedConfigChanges();
		private PackageManager mPackageManager;
		private List<AppEntry> mInstalledApps;

		public AppListLoader(Context context) {
			super(context);
			mPackageManager = getContext().getPackageManager();
			// NOTE: use global getContext() instead of the passed in context
		}

		@Override
		public List<AppEntry> loadInBackground() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	public static class AppEntry {

		private AppListLoader mLoader;
		private ApplicationInfo mInfo;
		private File mApkFile;
		private String mLabel;
		private Drawable mIcon;
		private boolean mMounted;

		public AppEntry(AppListLoader loader, ApplicationInfo info) {
			this.mLoader = loader;
			this.mInfo = info;
			this.mApkFile = new File(info.sourceDir);
		}

		public ApplicationInfo getApplicationInfo() {
			return mInfo;
		}

		public String getLabel() {
			return mLabel;
		}

		public Drawable getIcon() {
			if (mIcon == null) {
				if (mApkFile.exists()) {
					mIcon = mInfo.loadIcon(mLoader.mPackageManager);
					return mIcon;
				} else {
					mMounted = false;
				}
			} else if (!mMounted) {
				// Reload app icon as app has changed from !mounted -> mounted
				if (mApkFile.exists()) {
					mMounted = true;
					mIcon = mInfo.loadIcon(mLoader.mPackageManager);
					return mIcon;
				}
			} else {
				return mIcon;
			}
			return mLoader.getContext().getResources().getDrawable(android.R.drawable.sym_def_app_icon);
		}

		@Override
		public String toString() {
			return mLabel;
		}

		private void loadLabel(Context context) {
			if (mLabel == null || !mMounted) {
				if (!mApkFile.exists()) {
					mMounted = false;
					mLabel = mInfo.packageName;
				} else {
					mMounted = true;
					CharSequence label = mInfo.loadLabel(context.getPackageManager());
					mLabel = label != null ? label.toString() : mInfo.packageName;
				}
			}
		}
	}

	// public static class InstalledAppListFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<List<E>>

}
