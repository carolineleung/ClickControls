package com.carolineleung.clickcontrols.styled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.carolineleung.clickcontrols.R;

public class DeviceDataActivity extends SherlockFragmentActivity {

	private static final String TAB = "tab";
	private TabHost mTabHost;
	private DeviceDataTabManager mTabManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.device_data_tabs);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mTabManager = new DeviceDataTabManager(this, mTabHost, com.carolineleung.clickcontrols.R.id.real_tab_content);

		mTabManager.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"), FragmentStackSupportActivity.CountingFragment.class, null);
		mTabManager
				.addTab(mTabHost.newTabSpec("contacts").setIndicator("Contacts"), LoaderCursorSupportActivity.CursorLoaderListFragment.class, null);

		// TODO - add rest of tabs

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString(TAB));
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(TAB, mTabHost.getCurrentTabTag());
	}

	public static class DeviceDataTabManager implements TabHost.OnTabChangeListener {

		private FragmentActivity mActivity;
		private TabHost mTabHost;
		private int mContainerId;
		private Map<String, TabInfo> mTabs = new ConcurrentHashMap<String, TabInfo>();
		private TabInfo mLastTab;

		public DeviceDataTabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
			this.mActivity = activity;
			this.mTabHost = tabHost;
			this.mContainerId = containerId;
			mTabHost.setOnTabChangedListener(this);
		}

		public void addTab(TabSpec tabSpec, Class<?> aClass, Bundle args) {
			tabSpec.setContent(new DeviceDataTabFactory(mActivity));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, aClass, args);

			// Check if we already have a fragment for this tab, probably from a previously saved state.
			// If so, deactivate it, because the initial state is that a tab isn't shown
			info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
			if (info.fragment != null && !info.fragment.isDetached()) {
				FragmentTransaction fragmentTx = mActivity.getSupportFragmentManager().beginTransaction();
				fragmentTx.detach(info.fragment);
				fragmentTx.commit();
			}

			mTabs.put(tag, info);
			mTabHost.addTab(tabSpec);
		}

		@Override
		public void onTabChanged(String tabId) {
			TabInfo newTab = mTabs.get(tabId);
			if (mLastTab != newTab) {
				FragmentTransaction fragmentTx = mActivity.getSupportFragmentManager().beginTransaction();
				if (mLastTab != null) {
					if (mLastTab.fragment != null) {
						fragmentTx.detach(mLastTab.fragment);
					}
				}
				if (newTab != null) {
					if (newTab.fragment == null) {
						newTab.fragment = Fragment.instantiate(mActivity, newTab.theClass.getName(), newTab.args);
						fragmentTx.add(mContainerId, newTab.fragment, newTab.tag);
					} else {
						fragmentTx.attach(newTab.fragment);
					}
				}
				mLastTab = newTab;
				fragmentTx.commit();
				mActivity.getSupportFragmentManager().executePendingTransactions();
			}

		}

		final static class TabInfo {
			private String tag;
			private Class<?> theClass;
			private Bundle args;
			private Fragment fragment;

			public TabInfo(String tag, Class<?> theClass, Bundle args) {
				this.tag = tag;
				this.theClass = theClass;
				this.args = args;
			}
		}

		static class DeviceDataTabFactory implements TabHost.TabContentFactory {

			private Context mContext;

			public DeviceDataTabFactory(Context context) {
				this.mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View view = new View(mContext);
				// Trick: set width & height to be 0
				view.setMinimumWidth(0);
				view.setMinimumHeight(0);
				return view;
			}
		}
	}
}
