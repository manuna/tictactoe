package com.x3.tictactoe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NavMenuFragment extends Fragment {
	
	private boolean mStartVisible = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nav_menu, container, false);
		updateUI(view);
		return view;
	}
	
	public boolean isStartVisible() {
		return mStartVisible;
	}
	
	public void setStartVisible(boolean startVisible) {
		mStartVisible = startVisible;
		updateUI(getView());
	}
	
	private void updateUI(View view)
	{
		if (view != null) {
			View start_button = view.findViewById(R.id.start_button);
			start_button.setVisibility(mStartVisible ? View.VISIBLE
					: View.INVISIBLE);
		}
	}

}
