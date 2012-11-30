package com.x3.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	private View mSelectedSegment = null;
	private PlaygroundView mPlaygroundView = null;
	private Playground mPlayground = new Playground(3);
	private int mValue = Playground.X;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize UI
        mPlaygroundView = (PlaygroundView)findViewById(R.id.playground);
        mPlaygroundView.setPlayground(mPlayground);
        mPlaygroundView.setListener(new PlaygroundView.OnClickListener() {
			
			@Override
			public void onClick(PlaygroundView view, int cellX, int cellY) {
				onPlaygroundClick(cellX, cellY);
			}
		});
        
        selectSegment(findViewById(R.id.button_3x3));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void on3x3Clicked(View view) {
    	Log.v("MainActivity", "3x3 button selected");
    	
    	mPlayground.setSize(3);
    	selectSegment(view);
    }
    
    public void on5x5Clicked(View view) {
    	Log.v("MainActivity", "5x5 button selected");
    	
    	mPlayground.setSize(5);
    	selectSegment(view);
    }
    
    public void onRestartGame(View view) {
    	Log.v("MainActivity", "Restarting game");
    	mPlayground.reset();
    }
    
    private void selectSegment(View view) {
    	if (mSelectedSegment != view) {
    		if (mSelectedSegment != null) {
    			mSelectedSegment.setSelected(false);
    		}
    		mSelectedSegment = view;
    		mSelectedSegment.setSelected(true);
    	}
    }
    
    private void onPlaygroundClick(int cellX, int cellY) {
    	if (mPlayground.set(cellX, cellY, mValue)) {
	    	if (mValue == Playground.X) {
	    		mValue = Playground.O;
	    	} else {
	    		mValue = Playground.X;
	    	}
    	}
    }
    
}
