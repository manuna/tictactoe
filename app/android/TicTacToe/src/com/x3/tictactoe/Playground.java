package com.x3.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.x3.tictactoe.utils.ArrayUtils;

public class Playground {

	public interface DataListener {
		
		void onReset();
		
		void onElementChanged(int x, int y);
		
	}
	
	public interface GameListener {
		
		void onGameEnded(int result);
		
	}
	
	final private static String TAG = Playground.class.getSimpleName();
	
	final public static int X = 1;
	final public static int O = 2;
	
	final public static int GAME_RESULT_TIE = 0;
	final public static int GAME_RESULT_X_WINS = X;
	final public static int GAME_RESULT_O_WINS = O;
	
	private Random mRandom = new Random(System.currentTimeMillis());
	
	private int mSize = 0;
	private int[] mField = null;
	private int[][] mAIWeightsX = null;
	private int[][] mAIWeightsO = null;
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
		mAIWeightsX = new int[size][size];
		mAIWeightsO = new int[size][size];
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
	
	private void updateAIWeights(int token) {
		int[][] weights = (token == X) ? mAIWeightsX : mAIWeightsO;
		
		// Update row weights
		for (int i = 0; i < mSize; i++) {
			int count = tokenCountInRow(i, token);
			for (int j = 0; j < mSize; j++) {
				weights[j][i] = (get(j, i) == 0) ? count : -1;
			}
		}
		
		// Update column weights
		for (int i = 0; i < mSize; i++) {
			int count = tokenCountInColumn(i, token);
			for (int j = 0; j < mSize; j++) {
				// Put maximum value
				weights[i][j] = (get(i, j) == 0) ? Math.max(count,
						weights[i][j]) : -1;
			}
		}
		
		int mainDiagCount = tokenCountInDiag(true, token);
		for (int i = 0; i < mSize; i++) {
			weights[i][i] = (get(i, i) == 0) ? Math.max(mainDiagCount,
					weights[i][i]) : -1;
		}
		
		int diagCount = tokenCountInDiag(false, token);
		for (int i = 0; i < mSize; i++) {
			int col = mSize - i - 1;
			weights[col][i] = (get(col, i) == 0) ? Math.max(diagCount,
					weights[col][i]) : -1;
		}
		
		Log.v(TAG,
				"Weights for token " + token + " " + Arrays.deepToString(weights));
	}
	
	private int[] nextMove(int token) {
		// Update AI logic weights
		updateAIWeights(X);
		updateAIWeights(O);
		
		int[][] aiWeights = (token == X) ? mAIWeightsX : mAIWeightsO;
		int[][] playerWeights = (token == X) ? mAIWeightsO : mAIWeightsX;
		
		// Calculate attack possibilities
		int maxAttackWeight = -1;
		for (int i = 0; i < mSize; i++) {
			int maxColWeight = ArrayUtils.maxElement(aiWeights[i]);
			if (maxColWeight > maxAttackWeight) {
				maxAttackWeight = maxColWeight;
			}
		}
		
		// Calculate defense possibilities if AI is not winning
		if (maxAttackWeight < mSize - 1) {
			ArrayList<int[]> defenseMoves = new ArrayList<int[]>(4);
			for (int i = 0; i < mSize; i++) {
				for (int j = 0; j < mSize; j++) {
					if (playerWeights[i][j] >= mSize - 1) {
						defenseMoves.add(new int[] {i, j});
					}
				}
			}
			
			// Defense
			if (defenseMoves.size() > 0) {
				return defenseMoves.get(mRandom.nextInt(defenseMoves.size()));
			}
		}
		
		// Attack
		ArrayList<int[]> attackMoves = new ArrayList<int[]>(mSize * mSize);
		for (int i = 0; i < mSize; i++) {
			for (int j = 0; j < mSize; j++) {
				if (aiWeights[i][j] == maxAttackWeight) {
					attackMoves.add(new int[] {i, j});
				}
			}
		}
		
		if (attackMoves.size() > 0) {
			return attackMoves.get(mRandom.nextInt(attackMoves.size()));
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
