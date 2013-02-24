package com.x3.tictactoe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class GameOptionsFragment extends Fragment {
	
	private static final String TAG = GameOptionsFragment.class.getSimpleName();
	
	private GameOptions mGameOptions = null;
	private boolean mDifficultyOptionVisible = false;
	private EditText mPlayer1NameEdit = null;
	private EditText mPlayer2NameEdit = null;
	
	private SparseArray<View> mDifficultyButtons;
	private SparseArray<View> mBoardSizeButtons;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v(TAG, "Loading game options view");
		
		View view = inflater.inflate(R.layout.game_options, container,
				false);
		initUI(view);
		updateDifficultyUI(view);
		updateGameOptionsUI(view);
		return view;
	}
	
	public void onHardSelected(View view) {
		Log.v(TAG, "Hard difficulty selected");
    	mGameOptions.setDifficulty(GameOptions.DIFFICULTY_HARD);
    	updateSelectedDifficultyButton();
	}
	
	public void onNormalSelected(View view) {
		Log.v(TAG, "Normal difficulty selected");
    	mGameOptions.setDifficulty(GameOptions.DIFFICULTY_NORMAL);
    	updateSelectedDifficultyButton();
	}
	
	public void onEasySelected(View view) {
		Log.v(TAG, "Easy difficulty selected");
    	mGameOptions.setDifficulty(GameOptions.DIFFICULTY_EASY);
    	updateSelectedDifficultyButton();
	}
	
	public void on3x3Selected(View view) {
		Log.v(TAG, "3x3 game board selected");
    	mGameOptions.setBoardSize(3);
    	updateSelectedBoardSizeButton();
	}
	
	public void on5x5Selected(View view) {
		Log.v(TAG, "5x5 game board selected");
    	mGameOptions.setBoardSize(5);
    	updateSelectedBoardSizeButton();
	}
	
	public boolean isDifficultyOptionVisible()
	{
		return mDifficultyOptionVisible;
	}
	
	public void setDifficultyOptionVisible(boolean difficultyOptionVisible)
	{
		mDifficultyOptionVisible = difficultyOptionVisible;
		updateDifficultyUI(getView());
	}
	
	public GameOptions getGameOptions()
	{
		return mGameOptions;
	}
	
	public void setGameOptions(GameOptions gameOptions)
	{
		mGameOptions = gameOptions;
		updateGameOptionsUI(getView());
	}
	
	private void initUI(View view) {
		mPlayer1NameEdit = (EditText)view.findViewById(R.id.player_1_name);
		mPlayer1NameEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (mGameOptions != null) {
					mGameOptions.setPlayer1Name(s.toString());
				}
			}
		});
		
		mPlayer2NameEdit = (EditText)view.findViewById(R.id.player_2_name);
		mPlayer2NameEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (mGameOptions != null) {
					mGameOptions.setPlayer2Name(s.toString());
				}
			}
		});
		
		mDifficultyButtons = new SparseArray<View>();
		mDifficultyButtons.put(GameOptions.DIFFICULTY_EASY, view.findViewById(R.id.easy_mode_btn));
		mDifficultyButtons.put(GameOptions.DIFFICULTY_NORMAL, view.findViewById(R.id.normal_mode_btn));
		mDifficultyButtons.put(GameOptions.DIFFICULTY_HARD, view.findViewById(R.id.hard_mode_btn));
		
		mBoardSizeButtons = new SparseArray<View>();
		mBoardSizeButtons.put(3, view.findViewById(R.id.board_3x3_btn));
		mBoardSizeButtons.put(5, view.findViewById(R.id.board_5x5_btn));
	}
	
	private void updateDifficultyUI(View view)
	{
		if (view != null) {
			View difficultyOptionGroup = view
					.findViewById(R.id.difficulty_option_group);
			difficultyOptionGroup
					.setVisibility(mDifficultyOptionVisible ? View.VISIBLE
							: View.INVISIBLE);
		}
	}
	
	private void updateGameOptionsUI(View view) {
		if (view != null && mGameOptions != null) {
			mPlayer1NameEdit.setText(mGameOptions.getPlayer1Name());
			mPlayer2NameEdit.setText(mGameOptions.getPlayer2Name());
			
			updateSelectedDifficultyButton();
			updateSelectedBoardSizeButton();
		}
	}
	
	private void updateSelectedDifficultyButton()
	{
		for (int i = 0; i < mDifficultyButtons.size(); i++) {
			int key = mDifficultyButtons.keyAt(i);
			View btn = mDifficultyButtons.valueAt(i);
			btn.setSelected(key == mGameOptions.getDifficulty());
		}
	}
	
	private void updateSelectedBoardSizeButton()
	{
		for (int i = 0; i < mBoardSizeButtons.size(); i++) {
			int key = mBoardSizeButtons.keyAt(i);
			View btn = mBoardSizeButtons.valueAt(i);
			btn.setSelected(key == mGameOptions.getBoardSize());
		}
	}
	
}
