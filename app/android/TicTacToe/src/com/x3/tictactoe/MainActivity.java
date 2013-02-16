package com.x3.tictactoe;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends FragmentActivity implements Playground.GameListener {
	
	private final static String TAG = "MainActivity";
	private final static String GAME_OPTIONS_FRAGMENT = "GameOptions";
	private final static String GAMEBOARD_FRAGMENT = "GameBoard";
	
	private NavMenuFragment mNavMenuFragment = null;
	private View mNavMenuRoot = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animateLogo();
        
        FragmentManager fragmentMgr = getSupportFragmentManager();
		fragmentMgr
				.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged() {
						MainActivity.this.onBackStackChanged();
					}
				});
        
        mNavMenuRoot = findViewById(R.id.nav_menu_root);
		mNavMenuFragment = (NavMenuFragment) fragmentMgr
				.findFragmentById(R.id.nav_menu);
		
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
				.add(R.id.fragment_root, new MainMenuFragment())
				.commit();
    }
    
    private void showGameOptions(boolean showDifficulty) {
    	GameOptionsFragment fragment = new GameOptionsFragment();
    	fragment.setDifficultyOptionVisible(showDifficulty);
    	
    	FragmentManager fragmentMgr = getSupportFragmentManager();
		fragmentMgr
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_right,
						R.anim.slide_out_left,
						android.R.anim.slide_in_left,
						android.R.anim.slide_out_right)
				.replace(R.id.fragment_root, fragment)
				.addToBackStack(GAME_OPTIONS_FRAGMENT).commit();
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
				.addToBackStack(GAMEBOARD_FRAGMENT).commit();
    }
    
    private void showNavMenu(boolean showNavbar) {
    	mNavMenuRoot.setVisibility(showNavbar ? View.VISIBLE : View.INVISIBLE);
    }
    
    private void showStartButton(boolean showStartButton) {
    	mNavMenuFragment.setStartVisible(showStartButton);
    }
    
    private void onBackStackChanged() {
    	FragmentManager fragmentMgr = getSupportFragmentManager();
    	int backStackEntryCount = fragmentMgr.getBackStackEntryCount();
    	
    	// Hide nav menu when main menu is shown
    	showNavMenu(backStackEntryCount != 0);
    	
    	// Hide start button when game board is shown
    	boolean startButtonVisible = true;
    	if (backStackEntryCount > 0) {
			FragmentManager.BackStackEntry topEntry = fragmentMgr
					.getBackStackEntryAt(backStackEntryCount - 1);
			startButtonVisible = !topEntry.getName().equals(GAMEBOARD_FRAGMENT);
    	}
    	
    	showStartButton(startButtonVisible);
    }
    
    private void animateLogo() {
    	final View logoView = findViewById(R.id.logo_image_view); 
        
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        logoAnim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				animateSplash();
			}
		});
        
        logoView.startAnimation(logoAnim);
    }
    
    private void animateSplash() {
    	final View splashView = findViewById(R.id.splash_screen);
    	
		Animation splashAnim = AnimationUtils.loadAnimation(this,
				R.anim.splash_anim);
		splashAnim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				splashView.setVisibility(View.GONE);
			}
		});
		
		splashView.startAnimation(splashAnim);
    }

	@Override
	public void onGameEnded(int result) {
		
	}
    
}
