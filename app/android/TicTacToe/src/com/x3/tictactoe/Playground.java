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
		
		void onElementChanged(int row, int column);
		
	}
	
	public interface GameListener {
		
		void onGameEnded(int result);
		
	}
	
	final private static String TAG = Playground.class.getSimpleName();
	
	final public static int EMPTY = 0;
	final public static int X = 1;
	final public static int O = 2;
	
	final public static int GAME_RESULT_TIE = 0;
	final public static int GAME_RESULT_X_WINS = X;
	final public static int GAME_RESULT_O_WINS = O;
	
	final public static int AI_CELL_CLEAR = -1001;
	final public static int AI_CELL_OCCUPIED = -1000;
	final public static int AI_CELL_NO_WIN = -1;  
	
	private Random mRandom = new Random(System.currentTimeMillis());
	
	private int mSize = 0;
	private int mWinCellsCount = 0;
	private int[][] mField = null;
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
		for (int i = 0; i < mField.length; i++) {
			Arrays.fill(mField[i], 0);
		}
		mFreeCells = mField.length * mField[0].length;
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
		mField = new int[size][size];
		mAIWeightsX = new int[size][size];
		mAIWeightsO = new int[size][size];
		mFreeCells = mField.length * mField[0].length;
		mFinished = false;
		mWinCellsCount = Math.min(4, size);
		
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
	
	public int get(int row, int column) {
		return mField[row][column];
	}
	
	public boolean set(int row, int column, int token) {
		boolean succeeded = !mFinished && (mField[row][column] == 0); 
		if (succeeded) {
			mField[row][column] = token;
			mFreeCells--;
			
			if (mDataListener != null) {
				mDataListener.onElementChanged(row, column);
			}
			
			checkWinner();
		}
		return succeeded;
	}
	
	private void processAI(int[] slice, int[] wslice, int token, int opponentToken) {
		for (int j = 0; j < slice.length; j++) {
			if (slice[j] != EMPTY) {
				wslice[j] = AI_CELL_OCCUPIED;
			}
		}
		
		int start = 0, end = 0;
		while (true) {
			start = ArrayUtils.findFirstNotOf(slice, opponentToken, start);
			if (start == -1) {
				break;
			}
			
			end = ArrayUtils.findFirstOf(slice, opponentToken, start);
			int endIdx = end == -1 ? slice.length - 1 : end;
			if (endIdx - start + 1 < mWinCellsCount) {
				for (int j = start; j <= endIdx; j++) {
					if (slice[j] == EMPTY && wslice[j] == AI_CELL_CLEAR) {
						wslice[j] = AI_CELL_NO_WIN;
					}
				}
			} else {
				for (int j = start; j <= endIdx - mWinCellsCount + 1; j++) {
					int tokenCount = ArrayUtils.countOf(slice, j, j
							+ mWinCellsCount - 1, token);
					for (int i = j; i < j + mWinCellsCount; i++) {
						if (slice[i] == EMPTY) {
							wslice[i] = Math.max(tokenCount, wslice[i]);
						}
					}
				}
			}
			
			if (end == -1 || end + 1 >= slice.length) {
				break;
			}
			start = end + 1;
		}
	}
	
	private static void resetAIWeights(int[][] weights) {
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = AI_CELL_CLEAR;
			}
		}
	}
	
	private void updateAIWeights(int token) {
		int[][] weights = (token == X) ? mAIWeightsX : mAIWeightsO;
		int opponentToken = (token == X) ? O : X;
		int[] slice = new int[weights.length];
		int[] wslice = new int[weights.length];
		
		resetAIWeights(weights);
		
		// Update row weights
		for (int i = 0; i < mSize; i++) {
			ArrayUtils.rowSlice(mField, i, slice);
			ArrayUtils.rowSlice(weights, i, wslice);
			processAI(slice, wslice, token, opponentToken);
			ArrayUtils.applyRowSlice(weights, i, wslice);
		}
		
		// Update column weights
		for (int i = 0; i < mSize; i++) {
			ArrayUtils.columnSlice(mField, i, slice);
			ArrayUtils.columnSlice(weights, i, wslice);
			processAI(slice, wslice, token, opponentToken);
			ArrayUtils.applyColumnSlice(weights, i, wslice);
		}
		
		// Update diagonal weights
		for (int i = 0; i <= mSize - mWinCellsCount; i++) {
			ArrayUtils.diagSlice(mField, i, 0, true, slice);
			ArrayUtils.diagSlice(weights, i, 0, true, wslice);
			processAI(slice, wslice, token, opponentToken);
			ArrayUtils.applyDiagSlice(weights, i, 0, true, wslice);
			
			ArrayUtils.diagSlice(mField, i, mSize - 1, false, slice);
			ArrayUtils.diagSlice(weights, i, mSize - 1, false, wslice);
			processAI(slice, wslice, token, opponentToken);
			ArrayUtils.applyDiagSlice(weights, i, mSize - 1, false, wslice);
		}
		
		// Update diagonal weights
		for (int i = 0; i <= mSize - mWinCellsCount; i++) {
			ArrayUtils.diagSlice(mField, 0, i, true, slice);
			ArrayUtils.diagSlice(weights, 0, i, true, wslice);
			processAI(slice, wslice, token, opponentToken);
			ArrayUtils.applyDiagSlice(weights, 0, i, true, wslice);
			
			ArrayUtils.diagSlice(mField, 0, mSize - i - 1, false, slice);
			ArrayUtils.diagSlice(weights, 0, mSize - i - 1, false, wslice);
			processAI(slice, wslice, token, opponentToken);
			ArrayUtils.applyDiagSlice(weights, 0, mSize - i - 1, false, wslice);
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
		int maxAttackWeight = AI_CELL_OCCUPIED;
		for (int i = 0; i < mSize; i++) {
			int maxRowWeight = ArrayUtils.maxElement(aiWeights[i]);
			if (maxRowWeight > maxAttackWeight) {
				maxAttackWeight = maxRowWeight;
			}
		}
		
		final int defenseMinWeight = 2;
		final int attackMinWeight = mWinCellsCount - 1;
		
		// Calculate defense possibilities if AI is not winning
		if (maxAttackWeight < attackMinWeight) {
			int maxDefenseWeight = AI_CELL_OCCUPIED;
			for (int i = 0; i < mSize; i++) {
				int maxRowWeight = ArrayUtils.maxElement(playerWeights[i]);
				if (maxRowWeight > maxDefenseWeight) {
					maxDefenseWeight = maxRowWeight;
				}
			}
			
			if (maxDefenseWeight >= defenseMinWeight) {
				ArrayList<int[]> defenseMoves = new ArrayList<int[]>(4);
				for (int i = 0; i < mSize; i++) {
					for (int j = 0; j < mSize; j++) {
						if (playerWeights[i][j] >= maxDefenseWeight) {
							defenseMoves.add(new int[] {i, j});
						}
					}
				}
				
				// Defense
				if (defenseMoves.size() > 0) {
					return defenseMoves.get(mRandom.nextInt(defenseMoves.size()));
				}
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
	
	private boolean isWinner(int token) {
		boolean isWinner = false;
		int[] slice = new int[mField.length];
		
		// Check diagonals
		// T - -
		// T T -
		// T T T
		for (int i = 0; i <= mSize - mWinCellsCount && !isWinner; i++) {
			ArrayUtils.diagSlice(mField, i, 0, true, slice);
			isWinner = ArrayUtils.maxRange(slice, 0, mSize - i - 1, token) >= mWinCellsCount;
		}
		
		// - - T
		// - T T
		// T T T
		for (int i = 0; i <= mSize - mWinCellsCount && !isWinner; i++) {
			ArrayUtils.diagSlice(mField, i, mSize - 1, false, slice);
			isWinner = ArrayUtils.maxRange(slice, 0, mSize - i - 1, token) >= mWinCellsCount;
		}
		
		// - T T
		// - - T
		// - - -
		for (int i = 1; i <= mSize - mWinCellsCount && !isWinner; i++) {
			ArrayUtils.diagSlice(mField, 0, i, true, slice);
			isWinner = ArrayUtils.maxRange(slice, 0, mSize - i - 1, token) >= mWinCellsCount;
		}
		
		// T T -
		// T - -
		// - - -
		for (int i = 1; i <= mSize - mWinCellsCount && !isWinner; i++) {
			ArrayUtils.diagSlice(mField, 0, mSize - i - 1, false, slice);
			isWinner = ArrayUtils.maxRange(slice, 0, mSize - i - 1, token) >= mWinCellsCount;
		}
		
		// Check rows
		for (int i = 0; i < mSize && !isWinner; i++) {
			ArrayUtils.rowSlice(mField, i, slice);
			isWinner = ArrayUtils.maxRange(slice, token) >= mWinCellsCount;
		}
		
		// Check columns
		for (int i = 0; i < mSize && !isWinner; i++) {
			ArrayUtils.columnSlice(mField, i, slice);
			isWinner = ArrayUtils.maxRange(slice, token) >= mWinCellsCount;
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
