package com.carolineleung.clickcontrols.styled;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

import com.actionbarsherlock.app.SherlockFragment;
import com.carolineleung.clickcontrols.R;

@SuppressLint("ValidFragment")
public class RoundedCornerFragment extends SherlockFragment {

	private View mView;
	private int mBackgroundColor;
	private float mWeight;
	private int marginLeft, marginRight, marginTop, marginBottom;

	public RoundedCornerFragment() {

	}

	public RoundedCornerFragment(int mBackgroundColor, float mWeight, int marginLeft, int marginRight, int marginTop, int marginBottom) {
		this.mBackgroundColor = mBackgroundColor;
		this.mWeight = mWeight;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.marginTop = marginTop;
		this.marginBottom = marginBottom;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mView = new View(getActivity());
		LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, mWeight);
		layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		mView.setLayoutParams(layoutParams);

		GradientDrawable backgroundDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.rounded_rect);
		backgroundDrawable.setColor(mBackgroundColor);
		mView.setBackgroundDrawable(backgroundDrawable);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return mView;
	}

}
