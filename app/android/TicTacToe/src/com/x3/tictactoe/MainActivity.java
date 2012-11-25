package com.x3.tictactoe;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void on3x3Clicked(View view) {
    	Log.v("MainActivity", "3x3 button selected");
    }
    
    public void on5x5Clicked(View view) {
    	Log.v("MainActivity", "5x5 button selected");
    }
    
    public void onRestartGame(View view) {
    	Log.v("MainActivity", "Restarting game");
    }
}
