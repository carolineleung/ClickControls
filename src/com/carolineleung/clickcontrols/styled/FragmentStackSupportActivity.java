package com.carolineleung.clickcontrols.styled;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.carolineleung.clickcontrols.R;

public class FragmentStackSupportActivity extends SherlockFragmentActivity {

	private static final String LEVEL = "level";

	int mStackLevel = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_stack);

		// Button clicks
		Button button = (Button) findViewById(R.id.new_fragment);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addFragmentToStack();
			}
		});

		if (savedInstanceState == null) {
			Fragment newFragment = CountingFragment.newInstance(mStackLevel);
			FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
			fragmentTx.add(R.id.simple_fragment, newFragment).commit();
		} else {
			mStackLevel = savedInstanceState.getInt(LEVEL);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(LEVEL, mStackLevel);
	}

	private void addFragmentToStack() {
		mStackLevel++;
		Fragment newFragment = CountingFragment.newInstance(mStackLevel);

		FragmentTransaction fragmentTx = getSupportFragmentManager().beginTransaction();
		fragmentTx.replace(R.id.simple_fragment, newFragment);
		fragmentTx.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTx.addToBackStack(null);
		fragmentTx.commit();
	}

	public static class CountingFragment extends SherlockFragment {

		int mNum;
		private static final String NUM_KEY = "num";

		static CountingFragment newInstance(int num) {
			CountingFragment fragment = new CountingFragment();

			Bundle args = new Bundle();
			args.putInt(NUM_KEY, num);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle args = getArguments();
			mNum = args != null ? args.getInt(NUM_KEY) : 1;
		}

		@SuppressWarnings("deprecation")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.placeholder_textview, container, false);
			TextView textView = (TextView) view.findViewById(R.id.placeholder_text);
			textView.setText("Fragment #" + mNum);
			textView.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.gallery_thumb));
			return view;
		}

	}
}
