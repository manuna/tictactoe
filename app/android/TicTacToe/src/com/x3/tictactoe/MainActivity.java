package com.x3.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends Activity {
	
	private View mSelectedSegment = null;
	private TableLayout mPlayground3x3 = null;
	private TableLayout mPlayground5x5 = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize UI
        selectSegment(findViewById(R.id.button_3x3));
        
        mPlayground3x3 = (TableLayout)findViewById(R.id.playground_root_3x3);
        generatePlayground(mPlayground3x3, 3);
        
        mPlayground5x5 = (TableLayout)findViewById(R.id.playground_root_5x5);
        generatePlayground(mPlayground5x5, 5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void on3x3Clicked(View view) {
    	Log.v("MainActivity", "3x3 button selected");
    	
    	if (view != mSelectedSegment) {
    		mPlayground5x5.setVisibility(View.INVISIBLE);
    		mPlayground3x3.setVisibility(View.VISIBLE);
    	}
    	
    	selectSegment(view);
    }
    
    public void on5x5Clicked(View view) {
    	Log.v("MainActivity", "5x5 button selected");
    	
    	if (view != mSelectedSegment) {
    		mPlayground5x5.setVisibility(View.VISIBLE);
    		mPlayground3x3.setVisibility(View.INVISIBLE);
    	}
    	
    	selectSegment(view);
    }
    
    public void onRestartGame(View view) {
    	Log.v("MainActivity", "Restarting game");
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
    
    private void generatePlayground(TableLayout layout, int size) {
    	layout.removeAllViews();
    	    	
    	for (int i = 0; i < size; i++) {
    		TableRow row = new TableRow(this);
    		for (int j = 0; j < size; j++) {
    			ImageButton cell = new ImageButton(this);
    			cell.setBackgroundResource(R.drawable.game_cell);
    			row.addView(cell,  new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));
    		}
    		    		
    		layout.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT));
    	}
    }
}
