package com.x3.tictactoe;

import java.util.Arrays;

public class Playground {

	public interface Listener {
		
		void onReset();
		
		void onElementChanged(int x, int y);
		
	}
	
	final public static int X = 1;
	final public static int O = 2;
	
	private int mSize = 0;
	private int[] mField = null;
	private Listener mListener = null;
	
	public Playground(int size) {
		setSize(size);
	}
	
	public void reset() {
		Arrays.fill(mField, 0);
		if (mListener != null) {
			mListener.onReset();
		}
	}
	
	public int getSize() {
		return mSize;
	}
	
	public void setSize(int size) {
		mSize = size;
		mField = new int[size * size];
		
		if (mListener != null) {
			mListener.onReset();
		}
	}
	
	public Listener getListener() {
		return mListener;
	}
	
	public void setListener(Listener listener) {
		mListener = listener;
	}
	
	public int get(int x, int y) {
		return mField[y * mSize + x];
	}
	
	public boolean set(int x, int y, int value) {
		int index = y * mSize + x;
		boolean succeeded = (mField[index] == 0); 
		if (succeeded) {
			mField[y * mSize + x] = value;
			
			if (mListener != null) {
				mListener.onElementChanged(x, y);
			}
		}
		return succeeded;
	}
	
}
