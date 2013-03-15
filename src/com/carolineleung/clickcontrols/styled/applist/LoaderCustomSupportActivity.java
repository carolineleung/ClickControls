package com.carolineleung.clickcontrols.styled.applist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoaderCustomSupportActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentManager fragmentMgr = getSupportFragmentManager();

		// Create the fragment as content
		if (fragmentMgr.findFragmentById(android.R.id.content) == null) {
			AppListFragment list = new AppListFragment();
			fragmentMgr.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

}
