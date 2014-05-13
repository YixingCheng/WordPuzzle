package com.funnywords;

import android.app.Activity;
import android.app.Fragment;
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
    private final static String TAG = "WRmain";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wrmain);
		
	/*	if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}    */
		
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
	//	final Button takeImage = (Button) findViewById(R.id.take_image);
	//	final Button clearResult = (Button) findViewById(R.id.clear_results);
		
		singlePlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//  when you are writing in the parentheses of setOnClickListener,
				//    you have to use FromActivity. to qualify the this
				 Intent startSinglePlay = new Intent(WRmainActivity.this, WordRoom.class);
                 startActivity(startSinglePlay); 
			  }			
		   });
		
	
		
	/*	takeImage.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent videoIntent = new Intent(MainActivity.this, CameraActivity.class);   
                 startActivity(videoIntent); 
			}
		});
		
		clearResult.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for(int i = 1; i <= 4; i++){
					  String folderPath = sectionFragment.getTargetPath(i);
					  File folder = new File(folderPath);
					  File[] files = folder.listFiles();
					  for (File file : files){
                            file.delete();
                       }
					
				  }
				CameraActivity.javaHaarLength = 0;
				CameraActivity.javaHaarFps = null;
				CameraActivity.javaLbpLength = 0;
				CameraActivity.javaLbpFps = null;
				CameraActivity.nativeHaarLength = 0;
				CameraActivity.nativeHaarFps = null;
				CameraActivity.nativeLbpFps = null;
				CameraActivity.nativeLbpLength = 0;
				String toastMesage = new String("Previous results have been cleared!");
            	Toast toast = Toast.makeText(MainActivity.this, toastMesage, Toast.LENGTH_LONG);
                toast.show();
			}
			
		});    */
	}
	
	
/*	
	//  A placeholder fragment containing a simple view.
	 
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_wrmain,
					container, false);
			return rootView;
		}
		
		public static void addListenerToButton(){
			
		    Log.d(TAG, "before define button");
			final Button singlePlayer = (Button) findViewById(R.id.single_player);
		//	final Button takeImage = (Button) findViewById(R.id.take_image);
		//	final Button clearResult = (Button) findViewById(R.id.clear_results);
			
			Log.d(TAG, "before set Listen");
			singlePlayer.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					  when you are writing in the parentheses of setOnClickListener,
					    you have to use FromActivity. to qualify the this
					 Log.d(TAG, "before define Intent");
					 Intent startSinglePlay = new Intent(WRmainActivity, WordRoom.class);
					 Log.d(TAG, "before start Activity");
	                 startActivity(startSinglePlay); 
	                 Log.d(TAG, "after start Activity");
				  }			
			   });
			
			Log.d(TAG, "after set Listen");
			takeImage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent videoIntent = new Intent(MainActivity.this, CameraActivity.class);   
	                 startActivity(videoIntent); 
				}
			});
			
			clearResult.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(int i = 1; i <= 4; i++){
						  String folderPath = sectionFragment.getTargetPath(i);
						  File folder = new File(folderPath);
						  File[] files = folder.listFiles();
						  for (File file : files){
	                            file.delete();
	                       }
						
					  }
					CameraActivity.javaHaarLength = 0;
					CameraActivity.javaHaarFps = null;
					CameraActivity.javaLbpLength = 0;
					CameraActivity.javaLbpFps = null;
					CameraActivity.nativeHaarLength = 0;
					CameraActivity.nativeHaarFps = null;
					CameraActivity.nativeLbpFps = null;
					CameraActivity.nativeLbpLength = 0;
					String toastMesage = new String("Previous results have been cleared!");
	            	Toast toast = Toast.makeText(MainActivity.this, toastMesage, Toast.LENGTH_LONG);
	                toast.show();
				}
				
			});    
		}
	}   */

}
