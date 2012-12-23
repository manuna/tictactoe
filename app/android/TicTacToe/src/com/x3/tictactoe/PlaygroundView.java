package com.x3.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PlaygroundView extends View implements Playground.DataListener {

	public interface OnClickListener {
		
		void onClick(PlaygroundView view, int cellX, int cellY);
		
	}
	
	private Drawable mCellDrawable = null;
	private Drawable mXDrawable = null;
	private Drawable mODrawable = null;
	private Playground mPlayground = null;
	private OnClickListener mListener = null;
	
	public PlaygroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mCellDrawable = context.getResources().getDrawable(R.drawable.game_cell);
		mXDrawable = context.getResources().getDrawable(R.drawable.x);
		mODrawable = context.getResources().getDrawable(R.drawable.o);
	}
	
	public Playground getPlayground() {
		return mPlayground;
	}
	
	public void setPlayground(Playground playground) {
		if (mPlayground != null && mPlayground.getDataListener() == this) {
			mPlayground.setDataListener(null);
		}
		
		mPlayground = playground;
		
		if (mPlayground != null) {
			mPlayground.setDataListener(this);
		}
		
		reload();
	}
	
	public OnClickListener getListener() {
		return mListener;
	}
	
	public void setListener(OnClickListener listener) {
		mListener = listener;
	}
	
	public void reload() {
		invalidate();
	}
	
	public void reload(int row, int column) {
		if (mPlayground != null) {
			final int playgroundSize = mPlayground.getSize();
			final int cellWidth = getMeasuredWidth() / playgroundSize;
			final int cellHeight = getMeasuredHeight() / playgroundSize;
			final int left = column * cellWidth, top = row * cellHeight;
			
			invalidate(left, top, left + (column + 1) * cellWidth, top + (row + 1)
					* cellHeight);
		} else {
			invalidate();
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		final float ICON_SIZE = 0.6f;
		
		if (mPlayground != null) {
			final int playgroundSize = mPlayground.getSize();
			final int cellWidth = getMeasuredWidth() / playgroundSize;
			final int cellHeight = getMeasuredHeight() / playgroundSize;
			final int iconWidth = (int)(cellWidth * ICON_SIZE);
			final int iconHeight = (int)(cellHeight * ICON_SIZE);
			final int iconOffsetX = (cellWidth - iconWidth) / 2;
			final int iconOffsetY = (cellHeight - iconHeight) / 2;
			
			for (int row = 0; row < playgroundSize; row++) {
				for (int column = 0; column < playgroundSize; column++) {
					final int left = column * cellWidth, top = row * cellHeight;
					mCellDrawable.setBounds(left, top, left + cellWidth, top
							+ cellHeight);
					mCellDrawable.draw(canvas);
					
					final int cellValue = mPlayground.get(row, column);
					
					Drawable iconDrawable = null;
					if (cellValue == Playground.X) {
						iconDrawable = mXDrawable;
					} else  if (cellValue == Playground.O) {
						iconDrawable = mODrawable;
					}
					
					if (iconDrawable != null) {
						final int iconLeft = left + iconOffsetX;
						final int iconTop = top + iconOffsetY;
						iconDrawable.setBounds(iconLeft, iconTop, iconLeft
								+ iconWidth, iconTop + iconHeight);
						iconDrawable.draw(canvas);
					}
				}
			}
		}
	}
		
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mPlayground == null) {
			return false;
		}
		
		final int eventAction = event.getAction();
		final int playgroundSize = mPlayground.getSize();
		if (eventAction == MotionEvent.ACTION_CANCEL
				|| eventAction == MotionEvent.ACTION_UP) {
			final float viewX = event.getX();
			final float viewY = event.getY();
			
			if (viewX >= 0 && viewX <= getMeasuredWidth() && viewY >= 0
					&& viewY <= getMeasuredHeight()) {
				final int row = (int) (event.getY() * playgroundSize / getMeasuredHeight());
				final int column = (int) (event.getX() * playgroundSize / getMeasuredWidth());

				if (mListener != null) {
					mListener.onClick(this, row, column);
				}
			}
		}
		
		return true;
	}

	@Override
	public void onReset() {
		reload();
		
	}

	@Override
	public void onElementChanged(int row, int column) {
		reload(row, column);
	}
	
}
