package com.funnywords;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;


public class TimeAttackActivity extends Activity {
	private static final String CURRENT_SCORE = "current_score";
	
	DraggableGridView               workSpace;
	MyWordsGridView                 wordsSpace;
	DraggableGridView               lettersSpace;
	ScrabbleDistribution            letters;
	
	Activity                        currentAct;                            // current activity
	private Dictionary              dic;
	
	public  int                     gameScore;
//	private int                     requiredScore;
	boolean                         modifying = false;
	String                          modifyingWord = "";
	private String                  currentWord = "";
	private TextView                scoreText;
	private TextView                remainingTimeText;             // textView for remaining time
	private TextView                levelLable;
	public int                      remainingTime;
	Button                          submitWord,addLetter;
	private boolean                 runnableFlag = true;           // Flag for runnable
//	private Bundle                  intentExtra;
//	private Bundle                  intentMode;
	
	//timer thread
	private Handler                 timerHandler;
	private final Runnable timerRunnable = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			  if(runnableFlag){
			        if(remainingTime <= 1){
			             timesUP();
		             }
			        remainingTime -= 1;
			     
				    remainingTimeText.setText(Integer.toString(remainingTime));
				    timerHandler.postDelayed(timerRunnable, 1000);
			     }
			  else{
				  timerHandler.postDelayed(timerRunnable, 1000);
			   }
			}
	 };
	 
		
    private enum UPDATETYPE {
    	LETTER, WORDIN, WORDOUT
    }
    
	public enum GRIDTYPE {
		WORD,LETTER, WORK
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	    Log.d(WRmainActivity.TAG,"restore from previous instance state");
	    // Restore state members from saved instance
	     
	    runnableFlag = savedInstanceState.getBoolean("timer_flag");
	    Log.d(WRmainActivity.TAG, Boolean.toString(runnableFlag));
	    gameScore = savedInstanceState.getInt("score_int",0);
	    modifying = savedInstanceState.getBoolean("modifying",false);
	    modifyingWord = savedInstanceState.getString("modifyingWord");
	    
	    //may be something wrong here
	    //remainingTime = savedInstanceState.getInt("remainingTime", 10);
	    
	    scoreText.setText( String.valueOf(gameScore));
	    
	    if(savedInstanceState.getStringArray("workSpace") != null)
    		buildFromStringArray(savedInstanceState.getStringArray("workSpace"),GRIDTYPE.WORK);
    	if(savedInstanceState.getStringArray("lettersSpace") != null)
    		buildFromStringArray(savedInstanceState.getStringArray("lettersSpace"),GRIDTYPE.LETTER);	
    	if(savedInstanceState.getStringArray("wordsSpace") != null)
    		buildFromStringArray(savedInstanceState.getStringArray("wordsSpace"),GRIDTYPE.WORD);
    	
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_attack);
        Log.d(WRmainActivity.TAG, "game activity, after set Content");
        runnableFlag = true;                                                       //reset the runnable flag to true
        
        gameScore = 0;
        levelLable = (TextView) findViewById(R.id.levelTA);
        levelLable.setText("1");        //when first start the game, set the level label to 1
        
        currentAct = this;                                                             //get the current activity
        letters = new ScrabbleDistribution();                                          //initialize letter pool
        
        scoreText = (TextView) findViewById(R.id.scoreTA);
        scoreText.setText(Integer.toString(gameScore));
        remainingTimeText = (TextView) findViewById(R.id.timeRemainingTA); 
        workSpace = (DraggableGridView) findViewById(R.id.workSpaceTA);                  //that's where the constructor is called
        workSpace.setColCount(10);
        wordsSpace = (MyWordsGridView) findViewById(R.id.wordsSpaceTA);
        lettersSpace = (DraggableGridView) findViewById(R.id.lettersSpaceTA);
        lettersSpace.setColCount(8);
        submitWord = (Button) findViewById(R.id.submitWordTA);
        addLetter = (Button) findViewById(R.id.addLetterTA);
        wordsSpace.setColCount(3);
        submitWord.setOnClickListener(submitListener());
        addLetter.setOnClickListener(addLetterListener());
        
        Log.d(WRmainActivity.TAG, "game activity, after all texts & buttons");
        
        //I'll need to modify here since I don't want to load dictionary 
        if(savedInstanceState != null)
	        if(savedInstanceState.getStringArray("dic") != null){
	        	Log.d(WRmainActivity.TAG,"restore the instance state of the dictionary");
	        	dic = new Dictionary(this,R.raw.dictionary_en, savedInstanceState.getStringArray("dic"));
	        	return;
	        }
        
        //check if the dict is already existed
        dic = LoadingActivity.dict;
        if (dic == null){
        	 Log.d(WRmainActivity.TAG, "dictionary doesn't not exist");
        	 LoadingActivity.dict = new Dictionary(this.getParent(), R.raw.dictionary_en);       //maybe something wrong here
             dic = LoadingActivity.dict;
           }
        else{
        	 // dic = new Dictionary(this, R.raw.dictionary_en);
        	 Log.d(WRmainActivity.TAG, "dictionary already existed");
          }  
    	
    	//start timer thread
	    timerHandler = new Handler();
		remainingTime = 15;
		timerHandler.post(timerRunnable);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(WRmainActivity.TAG, "onStart called");
        runnableFlag = true;
     }
    /*
     * Function to add a new letter
     */
    private OnClickListener addLetterListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Button letter = newLetter(String.valueOf(letters.getRandomLetter()));
				letter.setOnClickListener(clickLetterListener());         //set onclicklistener to new letter
				lettersSpace.addView(letter);                             //add new letter to letter space
				updateScoreAndTime(UPDATETYPE.LETTER);
			}
		};
	}

    /*
     * Function to create a new Button with a leter or string
     */
	protected Button newLetter(String c) {
		Button l = new Button(this);
		
		l.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL); 
		l.setText(c);
	
		l.setTextColor(Color.WHITE);
		
		if(c.length() == 1){                                                  //need to take a look at here
			l.setTextSize(18);
			/*
			 * Code for unique color depending on the distribution of the letter 
			 * 
			int v = (c.charAt(0)-'A');
			int d = ScrabbleDistribution.distribution[v];
			int intColor = v*400+d*1100;
			
			String hexColor = String.format("#%06X", (0xFFFFFF & intColor));
			Log.d("DBG","Cor:"+intColor);*/
		
			l.setBackgroundResource(R.drawable.unselected);
		  }
		else {
			l.setTextSize(18-c.length()/2);
			switch(c.length()){
				case 1: 
					l.setBackgroundResource(R.drawable.black_b);
					break;
				case 2:
					l.setBackgroundResource(R.drawable.purple_b);
					break;
				case 3:
					l.setBackgroundResource(R.drawable.grey_b);
					l.setTextColor(Color.BLACK);
					break;
				case 4:
					l.setBackgroundResource(R.drawable.green_b);
					break;
				case 5:
					l.setBackgroundResource(R.drawable.orange_b);
					break;		
				default:
					l.setBackgroundResource(R.drawable.blue_b);
					break;	
			 }
		}
		
		return l;
	}

	/*
	 * Function to handle the click of a letter from letterSpace
	 */
	protected OnClickListener clickLetterListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				lettersSpace.removeView(v);
				
				Button tvNew =  newLetter(((Button)v).getText().toString());             //add the letter to workspace
				tvNew.setOnClickListener(workingLetterClickListener());
				
				workSpace.addView(tvNew);
			}
		};
	}

	/*
	 * Function to handle the click of a letter on the workspace
	 */
	protected OnClickListener workingLetterClickListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				workSpace.removeView(v);                                              //add the letter back to letter space
				
				Button tvNew =  newLetter(((Button)v).getText().toString());
				tvNew.setOnClickListener(clickLetterListener());
				
				lettersSpace.addView(tvNew);
			}
		};
	}
	
	/*
	 * Function to submit a word for the game to process
	 */
	private OnClickListener submitListener() {
		return new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentWord = "";                                                 //reset currentWord
				for(int i=0;i<workSpace.getChildCount();i++){
					currentWord += ((Button)workSpace.getChildAt(i)).getText();
				  }
				if(isWord(currentWord)){
					workSpace.removeAllViews();
					Button w = newLetter(currentWord);
					w.setOnClickListener(wordClickListener());
					wordsSpace.addView(w);
						
				 }
				updateScoreAndTime(UPDATETYPE.WORDIN);                                    //that's where update the score
			 }
		 };
	 }
	
	/*
	 * Function to handle the press of a word
	 */
	protected OnClickListener wordClickListener() {
		
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = (String) ((Button) v).getText();
				
				if(modifying){
					Toast.makeText(currentAct, currentAct.getResources().getString(R.string.use_modified), Toast.LENGTH_LONG).show();
					return;
				}
				for(int i=0;i<text.length();i++){
					Button l = newLetter(String.valueOf(text.charAt(i)));
					l.setBackgroundResource(R.drawable.unselected_alt);
					l.setOnClickListener(clickLetterListener());
					lettersSpace.addView(l);
				}

				wordsSpace.removeView(v);
				modifying = true;
				modifyingWord = text;
				updateScoreAndTime(UPDATETYPE.WORDOUT);
			}
		};
	}

	/*
	 * Method to verify if this is a real word
	 */
	protected boolean isWord(String word) {
		
		if(modifying){
			if(isWordInThisWord(word,modifyingWord)){
				
				if(dic.isWord(word)){
					modifying = false;
					modifyingWord = "";
					return true;
				}else{
					Toast.makeText(currentAct, currentAct.getResources().getString(R.string.invalid_word), Toast.LENGTH_LONG).show();
					return false;
				}
			}else{
				Toast.makeText(currentAct, currentAct.getResources().getString(R.string.must_use_previous_word)+"->"+modifyingWord, Toast.LENGTH_LONG).show();
				return false;
			}
			
		}else{
			if(dic.isWord(word)){
				return true;
			}else{
				Toast.makeText(currentAct, currentAct.getResources().getString(R.string.invalid_word)+"->"+word, Toast.LENGTH_LONG).show();
				return false;
			}
			
		}

	}

	private boolean isWordInThisWord(String word, String old) {
		
		if(old.length() > word.length())
			return false;
		
		if(word.compareTo(modifyingWord) == 0)
			return true;
		
		for(int i=0;i<old.length();i++){
			
			int index = word.indexOf(old.charAt(i));
			if (index == -1)
			{
			    return false;
			}
			else
			{
			    word = word.substring(0, index) + word.substring(index + 1, word.length());
			}
			
		}
		return true;
	}
    
	
	//method used to update time and score
	protected void updateScoreAndTime(UPDATETYPE updatetype) {
		switch(updatetype){
		     case LETTER:
		    	           gameScore--;
		    	           break;
		     case WORDIN:
		    	           gameScore = gameScore + 2 * currentWord.length();
		    	           remainingTime +=5;
		    	           break;
		     case WORDOUT: 
		                   gameScore = gameScore - 2 * currentWord.length();
		                   break;
		     default:
		    	           break;
		 }
		scoreText.setText(getFinalScore());
	}

	private String getFinalScore(){
	
    	return String.valueOf(gameScore);
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		Log.d(WRmainActivity.TAG, "leaving wordroom");
		runnableFlag = false;
		// int currentRemainingTime = remainingTime;
		String[] workSpaceArray = getStringArray( workSpace);
		String[] wordsSpaceArray = getStringArray( wordsSpace);
		String[] lettersSpaceArray = getStringArray( lettersSpace);
		
		outState.putStringArray("workSpace", workSpaceArray);
		outState.putStringArray("wordsSpace", wordsSpaceArray);
		outState.putStringArray("lettersSpace", lettersSpaceArray);
		// outState.putInt("remainingTime", currentRemainingTime);
		outState.putBoolean("timer_flag", true);
		Log.d(WRmainActivity.TAG, Boolean.toString(runnableFlag));
		outState.putInt("score_int", gameScore);
		outState.putBoolean("modifying", modifying);
		outState.putString("modifyingWord", modifyingWord);
		
		if(dic != null)
		if(dic.getWords().length > 1)
			outState.putStringArray("dic", dic.getWords());

	}

	private String[] getStringArray(DraggableGridView v) {
		
		String[] array = new String[v.getChildCount()];
		
		for(int i=0;i<v.getChildCount();i++){
			array[i] = ((Button)v.getChildAt(i)).getText().toString();
			Log.d(WRmainActivity.TAG,"Vai guardar:"+ array[i]);
		}
		if(array.length > 0)
			return array;
		else return null;
	}
	
	private void buildFromStringArray(String[] array, GRIDTYPE type){
		
		for(int i=0;i<array.length;i++){
			
			Button n = newLetter(array[i]);

			if(type == GRIDTYPE.LETTER){
				
				n.setOnClickListener(this.clickLetterListener());
				lettersSpace.addView(n);
				
			}else if(type == GRIDTYPE.WORD){
				n.setOnClickListener(this.wordClickListener());
				wordsSpace.addView(n);
			}	
			else if(type == GRIDTYPE.WORK){
				n.setOnClickListener(this.workingLetterClickListener());
				workSpace.addView(n);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		   if (keyCode == KeyEvent.KEYCODE_BACK){
			     AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
			     alertbox.setIcon(R.drawable.ic_launcher);
			     alertbox.setTitle("Exit Game");
			     alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						 finish();
					}
			    	 
			       });
			     alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
			    	 
			       });
			     
			     alertbox.show();
		      }
		   return super.onKeyDown(keyCode, event);
	  }
	
	private void timesUP(){
		   
		   runnableFlag = false;
		   
		   AlertDialog.Builder timesUPAlert = new AlertDialog.Builder(this);
		   timesUPAlert.setIcon(R.drawable.ic_launcher);
		   timesUPAlert.setTitle("Time's UP");
		   timesUPAlert.setPositiveButton("Back", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				 if (gameScore > scoreClass.getMin()) {
			    	  
			    	   scoreClass.insertScore(gameScore);
					   
					   AlertDialog.Builder highscore = new AlertDialog.Builder(TimeAttackActivity.this);
					   highscore.setIcon(R.drawable.ic_launcher);
					   highscore.setTitle("New high score in time attack!");
					   highscore.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							     finish();
						     }
					     });
					     highscore.show();
			         }
				 else{
					    finish(); 
				  }
			   }
		     });
		   timesUPAlert.show();
	  }
    
}