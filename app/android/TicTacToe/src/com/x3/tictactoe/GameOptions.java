package com.x3.tictactoe;

public class GameOptions {
	
	public static final int DIFFICULTY_EASY   = 0;
	public static final int DIFFICULTY_NORMAL = 1;
	public static final int DIFFICULTY_HARD   = 2;
	
	private String mPlayer1Name = "PLAYER 1";
	private String mPlayer2Name = "PLAYER 2";
	private boolean mAIEnabled = true;
	private int mDifficulty =  DIFFICULTY_NORMAL;
	private int mBoardSize = 3;
	
	public String getmPlayer1Name() {
		return mPlayer1Name;
	}
	
	public void setPlayer1Name(String player1Name) {
		mPlayer1Name = player1Name;
	}
	
	public String getPlayer2Name() {
		return mPlayer2Name;
	}
	
	public void setPlayer2Name(String player2Name) {
		mPlayer2Name = player2Name;
	}
	
	public boolean isAIEnabled() {
		return mAIEnabled;
	}
	
	public void setAIEnabled(boolean mAIEnabled) {
		this.mAIEnabled = mAIEnabled;
	}
	
	public int getDifficulty() {
		return mDifficulty;
	}
	
	public void setDifficulty(int mDifficulty) {
		this.mDifficulty = mDifficulty;
	}
	
	public int getBoardSize() {
		return mBoardSize;
	}
	
	public void setBoardSize(int mBoardSize) {
		this.mBoardSize = mBoardSize;
	}

}
