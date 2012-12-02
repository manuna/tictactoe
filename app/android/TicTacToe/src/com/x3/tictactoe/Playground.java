package com.x3.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Playground {

	public interface DataListener {
		
		void onReset();
		
		void onElementChanged(int x, int y);
		
	}
	
	public interface GameListener {
		
		void onGameEnded(int result);
		
	}
	
	final public static int X = 1;
	final public static int O = 2;
	
	final public static int GAME_RESULT_TIE = 0;
	final public static int GAME_RESULT_X_WINS = X;
	final public static int GAME_RESULT_O_WINS = O;
	
	private int mSize = 0;
	private int[] mField = null;
	private boolean mFinished = false;
	private int mFreeCells = 0;
	private DataListener mDataListener = null;
	private List<GameListener> mGameListeners = new ArrayList<GameListener>(10);
	
	public Playground(int size) {
		setSize(size);
	}
	
	public void reset() {
		Arrays.fill(mField, 0);
		mFreeCells = mField.length;
		mFinished = false;
		if (mDataListener != null) {
			mDataListener.onReset();
		}
	}
	
	public int getSize() {
		return mSize;
	}
	
	public void setSize(int size) {
		mSize = size;
		mField = new int[size * size];
		mFreeCells = mField.length;
		mFinished = false;
		
		if (mDataListener != null) {
			mDataListener.onReset();
		}
	}
	
	public DataListener getDataListener() {
		return mDataListener;
	}
	
	public void setDataListener(DataListener listener) {
		mDataListener = listener;
	}
	
	public void addGameListener(GameListener listener) {
		mGameListeners.add(listener);
	}
	
	public void removeGameListener(GameListener listener) {
		mGameListeners.remove(listener);
	}
	
	public int get(int x, int y) {
		return mField[y * mSize + x];
	}
	
	public boolean set(int x, int y, int value) {
		int index = y * mSize + x;
		boolean succeeded = !mFinished && (mField[index] == 0); 
		if (succeeded) {
			mField[y * mSize + x] = value;
			mFreeCells--;
			
			if (mDataListener != null) {
				mDataListener.onElementChanged(x, y);
			}
			
			checkWinner();
		}
		return succeeded;
	}
	
	private int tokenCountInRow(int row, int token) {
		int count = 0;
		for (int i = 0; i < mSize; i++) {
			if (get(i, row) == token) {
				count++;
			}
		}
		return count;
	}
	
	private int tokenCountInColumn(int column, int token) {
		int count = 0;
		for (int i = 0; i < mSize; i++) {
			if (get(column, i) == token) {
				count++;
			}
		}
		return count;
	}
	
	private int tokenCountInDiag(boolean mainDiag, int token) {
		int count = 0;
		for (int i = 0; i < mSize; i++) {
			int col = mainDiag ? i : (mSize - i - 1);
			if (get(col, i) == token) {
				count++;
			}
		}
		return count;
	}
	
	private boolean isWinner(int token) {
		boolean isWinner = false;
		
		// Check diagonals
		isWinner = (tokenCountInDiag(true, token) == mSize)
				|| (tokenCountInDiag(false, token) == mSize);
		
		// Check rows
		for (int i = 0; i < mSize && !isWinner; i++) {
			isWinner = (tokenCountInRow(i, token) == mSize);
		}
		
		// Check columns
		for (int i = 0; i < mSize && !isWinner; i++) {
			isWinner = (tokenCountInColumn(i, token) == mSize);
		}
		return isWinner;
	}
	
	private void checkWinner() {
		if (isWinner(X)) {
			finishGame(GAME_RESULT_X_WINS);
		} else if (isWinner(O)) {
			finishGame(GAME_RESULT_O_WINS);
		} else if (mFreeCells == 0) {
			finishGame(GAME_RESULT_TIE);
		}
	}
	
	private void finishGame(int result) {
		mFinished = true;
		if (mDataListener != null) {
			for (GameListener listener : mGameListeners) {
				listener.onGameEnded(result);
			}
		}
	}
	
}
