package com.x3.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements Playground.GameListener {
	
	private View mSelectedSegment = null;
	private PlaygroundView mPlaygroundView = null;
	private Playground mPlayground = new Playground(3);
	private int mValue = Playground.X;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize objects
        mPlayground.addGameListener(this);
        
        // Initialize UI
        mPlaygroundView = (PlaygroundView)findViewById(R.id.playground);
        mPlaygroundView.setPlayground(mPlayground);
        mPlaygroundView.setListener(new PlaygroundView.OnClickListener() {
			
			@Override
			public void onClick(PlaygroundView view, int row, int column) {
				onPlaygroundClick(row, column);
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
    	restartGame();
    }
    
    private void restartGame() {
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
    
    private void onPlaygroundClick(int row, int column) {
    	if (mPlayground.set(row, column, mValue)) {
	    	mPlayground.aiPlay(Playground.O);
    	}
    }

	@Override
	public void onGameEnded(int result) {
		String gameResultMsg = null;
		if (result == Playground.GAME_RESULT_X_WINS) {
			gameResultMsg = getResources().getString(R.string.x_wins);
		} else if (result == Playground.GAME_RESULT_O_WINS) {
			gameResultMsg = getResources().getString(R.string.o_wins);
		} else {
			gameResultMsg = getResources().getString(R.string.game_draw);
		}
		
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

		dlgAlert.setMessage(gameResultMsg);
		dlgAlert.setTitle(getResources().getString(R.string.app_name));
		dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				restartGame();
			}
			
		});
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}
    
}
