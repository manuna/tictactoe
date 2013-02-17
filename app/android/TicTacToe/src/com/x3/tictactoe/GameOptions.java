package com.x3.tictactoe;

import android.util.Log;

public class GameOptions {
	
	private final static String TAG = GameOptions.class.getSimpleName();
	
	public static final int DIFFICULTY_EASY   = 0;
	public static final int DIFFICULTY_NORMAL = 1;
	public static final int DIFFICULTY_HARD   = 2;
	
	private String mPlayer1Name = "PLAYER 1";
	private String mPlayer2Name = "PLAYER 2";
	private boolean mAIEnabled = true;
	private int mDifficulty =  DIFFICULTY_NORMAL;
	private int mBoardSize = 3;
	
	public String getPlayer1Name() {
		return mPlayer1Name;
	}
	
	public void setPlayer1Name(String player1Name) {
		Log.v(TAG, "Setting player 1 name to: " + player1Name);
		mPlayer1Name = player1Name;
	}
	
	public String getPlayer2Name() {
		return mPlayer2Name;
	}
	
	public void setPlayer2Name(String player2Name) {
		Log.v(TAG, "Setting player 2 name to: " + player2Name);
		mPlayer2Name = player2Name;
	}
	
	public boolean isAIEnabled() {
		return mAIEnabled;
	}
	
	public void setAIEnabled(boolean mAIEnabled) {
		Log.v(TAG, "Setting AIEnabled to: " + mAIEnabled);
		this.mAIEnabled = mAIEnabled;
	}
	
	public int getDifficulty() {
		return mDifficulty;
	}
	
	public void setDifficulty(int difficulty) {
		Log.v(TAG, "Setting difficulty level to: " + difficulty);
		mDifficulty = difficulty;
	}
	
	public int getBoardSize() {
		return mBoardSize;
	}
	
	public void setBoardSize(int boardSize) {
		Log.v(TAG, "Setting board size to: " + boardSize);
		mBoardSize = boardSize;
	}

}
