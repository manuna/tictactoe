package com.x3.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.x3.tictactoe.utils.ArrayUtils;

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
	
	public boolean set(int x, int y, int token) {
		int index = y * mSize + x;
		boolean succeeded = !mFinished && (mField[index] == 0); 
		if (succeeded) {
			mField[y * mSize + x] = token;
			mFreeCells--;
			
			if (mDataListener != null) {
				mDataListener.onElementChanged(x, y);
			}
			
			checkWinner();
		}
		return succeeded;
	}
	
	private int[] nextMove(int token) {
		int opponent = (token == X) ? O : X;
		
		final int defenceThreshold = mSize - 1;
		final int attackThreshold = mSize - 1;
		
		int[] diagCountAI = { tokenCountInDiag(true, token),
				tokenCountInDiag(false, token) };
		int[] diagCountPlayer = { tokenCountInDiag(true, opponent),
				tokenCountInDiag(false, opponent) };
		
		int[] rowCountAI = new int[mSize];
		int[] rowCountPlayer = new int[mSize];
		for (int i = 0; i < mSize; i++) {
			rowCountAI[i] = tokenCountInRow(i, token);
			rowCountPlayer[i] = tokenCountInRow(i, opponent);
		}
		
		int[] colCountAI = new int[mSize];
		int[] colCountPlayer = new int[mSize];
		for (int i = 0; i < mSize; i++) {
			colCountAI[i] = tokenCountInColumn(i, token);
			colCountPlayer[i] = tokenCountInColumn(i, opponent);
		}
		
		// Attack tactics
		int[] diagPriorities = new int[] { diagCountPlayer[0] == 0 ? diagCountAI[0] : 0,
				diagCountPlayer[1] == 0 ? diagCountAI[1] : 0 };
		int[] rowPriorities = new int[mSize];
		int[] colPriorities = new int[mSize];
		
		for (int i = 0; i < mSize; i++) {
			rowPriorities[i] = rowCountPlayer[i] == 0 ? rowCountAI[i] : 0;
		}
		
		for (int i = 0; i < mSize; i++) {
			colPriorities[i] = colCountPlayer[i] == 0 ? colCountAI[i] : 0;
		}
		
		int maxDiagPriority = ArrayUtils.maxElement(diagPriorities);
		int maxRowPriority = ArrayUtils.maxElement(rowPriorities);
		int maxColPriority = ArrayUtils.maxElement(colPriorities);
		
		if (maxDiagPriority < attackThreshold && maxRowPriority < attackThreshold
				&& maxColPriority < attackThreshold) {
			// Defend tactics
			if (diagCountAI[0] == 0 && diagCountPlayer[0] >= defenceThreshold) {
				for (int i = 0; i < mSize; i++) {
					if (get(i, i) == 0) {
						return new int[] {i, i};
					}
				}
			}
			
			if (diagCountAI[1] == 0 && diagCountPlayer[1] >= defenceThreshold) {
				for (int i = 0; i < mSize; i++) {
					int col = mSize - i - 1;
					if (get(col, i) == 0) {
						return new int[] {col, i};
					}
				}
			}
			
			for (int i = 0; i < mSize; i++) {
				if (rowCountAI[i] == 0 && rowCountPlayer[i] >= defenceThreshold) {
					for (int j = 0; j < mSize; j++) {
						if (get(j, i) == 0) {
							return new int[] {j, i};
						}
					}
				}
			}
			
			for (int i = 0; i < mSize; i++) {
				if (colCountAI[i] == 0 && colCountPlayer[i] >= defenceThreshold) {
					for (int j = 0; j < mSize; j++) {
						if (get(i, j) == 0) {
							return new int[] {i, j};
						}
					}
				}
			}
		}
		
		// Continue attack tactics
		if (maxDiagPriority > maxRowPriority && maxDiagPriority > maxColPriority) {
			int diagIdx = ArrayUtils.search(diagPriorities, maxDiagPriority);
			if (diagIdx >= 0) {
				boolean mainDiag = (diagIdx == 0);
				for (int i = 0; i < mSize; i++) {
					int col = mainDiag ? i : mSize - i -  1;
					if (get(col, i) == 0) {
						return new int[] {col, i};
					}
				}
			}
		} else if (maxRowPriority > maxColPriority) {
			int rowIdx = ArrayUtils.search(rowPriorities, maxRowPriority);
			for (int i = 0; i < mSize; i++) {
				if (get(i, rowIdx) == 0) {
					return new int[] {i, rowIdx};
				}
			}
		} else {
			int colIdx = ArrayUtils.search(colPriorities, maxColPriority);
			for (int i = 0; i < mSize; i++) {
				if (get(colIdx, i) == 0) {
					return new int[] {colIdx, i};
				}
			}
		}
		
		return null;
	}
	
	public void aiPlay(int token) {
		int[] nextMove = nextMove(token);
		if (nextMove != null) {
			set(nextMove[0], nextMove[1], token);
		}
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
