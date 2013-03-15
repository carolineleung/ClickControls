package com.carolineleung.clickcontrols.styled.applist;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.pm.ActivityInfoCompat;

/**
 * Configuration changes that should trigger rebuilding of the app list
 * 
 */
public class ConfigurationChanges {

	private Configuration mLastConfiguration = new Configuration();
	private int mLastDensity;

	public boolean applyNewConfig(Resources resources) {
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