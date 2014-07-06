package com.funnywords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class WRmainActivity extends Activity {
    public final static String TAG = "WoodRoom";
    public final static int SINGLEMODE = 1;
    public final static int TIMEATTACK = 2;
    public scoreClass singleScoreList;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wrmain);
		
		if (scoreClass.scoreRank  == null){
			  Log.d(WRmainActivity.TAG, "create the score arraly list the first time");
		      singleScoreList = new scoreClass();                                           //instantiate scoreList
		  }
		else{
			  Log.d(WRmainActivity.TAG, "scoreRank already exists");
		  }
		
		addListenerToButton();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wrmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void addListenerToButton(){
	
		final Button singlePlayer = (Button) findViewById(R.id.single_player);
		
		singlePlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				 Intent startSinglePlay = new Intent(WRmainActivity.this, LoadingActivity.class);
				 Bundle b = new Bundle();
				 b.putInt("gamemode", SINGLEMODE);
				 startSinglePlay.putExtras(b);
                 startActivity(startSinglePlay);
                 finish();
			   }			
		   });
		
		final Button highScore = (Button) findViewById(R.id.view_highscore);
	
			highScore.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					 Intent viewScore = new Intent(WRmainActivity.this, WRScoreActivity.class);
					 startActivity(viewScore);
	              
				  }			
			   });
		
		final Button timeAttack = (Button) findViewById(R.id.time_attack);
			
			timeAttack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					 Intent startSinglePlay = new Intent(WRmainActivity.this, LoadingActivity.class);
					 Bundle b = new Bundle();
					 b.putInt("gamemode", TIMEATTACK);
					 startSinglePlay.putExtras(b);
	                 startActivity(startSinglePlay);
	                 finish();
				  }			
			   });
			
	}

}
