package com.x3.tictactoe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameOptionsFragment extends Fragment {
	
	private boolean mDifficultyOptionVisible = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.game_options, container,
				false);
		updateUI(view);
		return view;
	}
	
	public boolean isDifficultyOptionVisible()
	{
		return mDifficultyOptionVisible;
	}
	
	public void setDifficultyOptionVisible(boolean difficultyOptionVisible)
	{
		mDifficultyOptionVisible = difficultyOptionVisible;
		updateUI(getView());
	}
	
	private void updateUI(View view)
	{
		if (view != null) {
			View difficultyOptionGroup = view
					.findViewById(R.id.difficulty_option_group);
			difficultyOptionGroup
					.setVisibility(mDifficultyOptionVisible ? View.VISIBLE
							: View.INVISIBLE);
		}
	}
	
}
