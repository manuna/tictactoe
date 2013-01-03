package com.x3.tictactoe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameBoardFragment extends Fragment {

	private PlaygroundView mPlaygroundView = null;
	private Playground mPlayground = new Playground(3);
	
	public void setPlayground(Playground playground) {
		mPlayground = playground;
		if (mPlaygroundView != null) {
			mPlaygroundView.setPlayground(playground);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initialize objects
        mPlayground.addGameListener(new Playground.GameListener() {
			
			@Override
			public void onGameEnded(int result) {
				GameBoardFragment.this.onGameEnded(result);				
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.game_board, container, false);
		mPlaygroundView = (PlaygroundView)view.findViewById(R.id.playground);
		mPlaygroundView.setPlayground(mPlayground);
		mPlaygroundView.setListener(new PlaygroundView.OnClickListener() {
			
			@Override
			public void onClick(PlaygroundView view, int row, int column) {
				if (mPlayground.set(row, column, Playground.X)) {
			    	mPlayground.aiPlay(Playground.O);
		    	}
			}
		});
		return view;
	}
	
	private void restartGame() {
		mPlayground.reset();
	}
	
	private void onGameEnded(int result) {
		String gameResultMsg = null;
		if (result == Playground.GAME_RESULT_X_WINS) {
			gameResultMsg = getResources().getString(R.string.x_wins);
		} else if (result == Playground.GAME_RESULT_O_WINS) {
			gameResultMsg = getResources().getString(R.string.o_wins);
		} else {
			gameResultMsg = getResources().getString(R.string.game_draw);
		}
		
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());

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
