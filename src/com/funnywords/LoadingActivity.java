package com.funnywords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class LoadingActivity extends Activity {
    private static long SPLASH_TIME_OUT = 1000;
	public static Dictionary dict;
	private Handler delayHandler;
	private int gameMode;
	private Bundle b;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.d(WRmainActivity.TAG, "Loading activity onCreate called");
        
        b = getIntent().getExtras();
        gameMode = b.getInt("gamemode");
        
        if (dict != null){
             Log.d(WRmainActivity.TAG, "the dictionary has been created before");
             Intent i = new Intent(LoadingActivity.this, WordRoom.class);
             Bundle bundle = new Bundle();
             bundle.putInt("gameMode", gameMode);
             i.putExtras(bundle);
             startActivity(i);
             Log.d(WRmainActivity.TAG, "so started game activity immediately");
             finish();
          }
        
    }
	
	@Override
    protected void onStart() {
        super.onStart();
        
        delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
        	  @Override
        	  public void run(){
        		  Intent i = new Intent(LoadingActivity.this, WordRoom.class);
        		  Bundle bundle = new Bundle();
                  bundle.putInt("gameMode", gameMode);
                  i.putExtras(bundle);
                  startActivity(i);
                  Log.d(WRmainActivity.TAG, "after started game activity");
                  finish();
        	    }
          }, SPLASH_TIME_OUT);
        
        Log.d(WRmainActivity.TAG, "Loading activity onStart called");
      //now I will move the dictionary loading step to here
        dict = new Dictionary(this, R.raw.dictionary_en);
        if (dict == null){
        	Log.d(WRmainActivity.TAG, "dict is null");
          }
        Log.d(WRmainActivity.TAG, "sucessfully loaded dictionary");
        
     }

}
