package com.x3.tictactoe;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends FragmentActivity implements Playground.GameListener {
	
	private final static String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupMainMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onOnePlayerClicked(View view) {
    	Log.v(TAG, "One player game choosen");
    	
    	View fragmentRoot = findViewById(R.id.fragment_root);
        int width = fragmentRoot.getWidth();
        int height = fragmentRoot.getHeight();
        Log.v(TAG, "Width: " + width + ", Height: " + height);
    	
    	showGameOptions(true);
    }
    
    public void onTwoPlayersClicked(View view) {
    	Log.v(TAG, "Two players game choosen");
    	showGameOptions(false);
    }
    
    public void onBackClicked(View view) {
    	Log.v(TAG, "Back button clicked");
    	FragmentManager fragmentMgr = getSupportFragmentManager();
    	if (fragmentMgr.getBackStackEntryCount() > 0) {
    		fragmentMgr.popBackStack();
    	}
    }
    
    public void onStartClicked(View view) {
    	Log.v(TAG, "Start game clicked");
    	showGameBoard();
    }
    
    private void setupMainMenu() {
    	FragmentManager fragmentMgr = getSupportFragmentManager();
		fragmentMgr.beginTransaction()
				.add(R.id.fragment_root, new MainMenuFragment()).commit();
    }
    
    private void showGameOptions(boolean showDifficulty) {
    	FragmentManager fragmentMgr = getSupportFragmentManager();
		fragmentMgr
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right,
						R.anim.slide_out_left,
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right)
				.replace(R.id.fragment_root, new GameOptionsFragment())
				.addToBackStack(null).commit();
    }
    
    private void showGameBoard() {
    	FragmentManager fragmentMgr = getSupportFragmentManager();
		fragmentMgr
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right,
						R.anim.slide_out_left,
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right)
				.replace(R.id.fragment_root, new GameBoardFragment())
				.addToBackStack(null).commit();
    }

	@Override
	public void onGameEnded(int result) {
		
	}
    
}
