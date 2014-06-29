package com.funnywords;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

 
public class WRScoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(WRmainActivity.TAG, "finally I got here");
		setContentView(R.layout.activity_wrscore);
		Log.d(WRmainActivity.TAG, "finally I got here");
        
		final ListView scoreList = (ListView) findViewById(R.id.listview);
		
		ArrayAdapter<Integer> scoreAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, scoreClass.scoreRank);
		scoreList.setAdapter(scoreAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wrscore, menu);
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


}
