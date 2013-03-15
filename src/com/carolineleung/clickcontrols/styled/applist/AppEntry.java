package com.carolineleung.clickcontrols.styled.applist;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class AppEntry {

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
				mIcon = mInfo.loadIcon(mLoader.getPackageManager());
				return mIcon;
			} else {
				mMounted = false;
			}
		} else if (!mMounted) {
			// Reload app icon as app has changed from !mounted -> mounted
			if (mApkFile.exists()) {
				mMounted = true;
				mIcon = mInfo.loadIcon(mLoader.getPackageManager());
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

	public void loadLabel(Context context) {
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
