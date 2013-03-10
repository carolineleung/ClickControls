package com.carolineleung.clickcontrols.styled;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.carolineleung.clickcontrols.R;

public class StyledFragmentActivity extends SherlockFragmentActivity implements ActionBar.TabListener {

	private final Handler handler = new Handler();

	private RoundedCornerFragment left;
	private RoundedCornerFragment right;

	private boolean useLogo = false;
	private boolean showHomeAsUp = false;

	private static final String[] TABS = new String[] { "Left", "Middle", "Right" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.styled_activity);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(showHomeAsUp);
		actionBar.setDisplayUseLogoEnabled(useLogo);

		for (String tabName : TABS) {
			actionBar.addTab(actionBar.newTab().setText(tabName));
		}

		// SpinnerAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.styled_sections, R.layout.sherlock_spinner_dropdown_item);
		// actionBar.setListNavigationCallbacks(arrayAdapter, new OnNavigationListener() {
		//
		// @Override
		// public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		//
		// return false;
		// }
		// });

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
