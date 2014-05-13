package com.funnywords;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.Color;



public class WordRoom extends Activity {
	
	public static final String      TAG = "WordRoom";
	DraggableGridView               workSpace;
	MyWordsGridView                 wordsSpace;
	DraggableGridView               lettersSpace;
	ScrabbleDistribution            letters;
	Dictionary                      dic;
	Activity                        ctx;
	int                             score_int;
	boolean                         modifying = false;
	String                          modifyingWord = "";
	private TextView                score;
	private TextView                remainingTimeText;             // textView for remaining time
	private int                     remainingTime;
	private long                    startTime;
	Button                          submitWord,addLetter;
	private Handler                 timerHandler;
	private final Runnable timerRunnable = new Runnable(){
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			     remainingTime -= 1;
				 remainingTimeText.setText(Integer.toString(remainingTime));
				 timerHandler.postDelayed(timerRunnable, 1000);
			}
	};
	
	public enum GRIDTYPE {
		WORD,LETTER, WORK
	}
	

	public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	    Log.d(TAG,"restore from previous instance state");
	    // Restore state members from saved instance
	    
	    score_int = savedInstanceState.getInt("score_int",0);
	    modifying = savedInstanceState.getBoolean("modifying",false);
	    modifyingWord = savedInstanceState.getString("modifyingWord");
	    
	    score.setText( String.valueOf(score_int));
	    
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
        setContentView(R.layout.activity_word_room);
        
        ctx = this;
        letters = new ScrabbleDistribution();
        
        score = (TextView) findViewById(R.id.score);
        remainingTimeText = (TextView) findViewById(R.id.tvTimeRemaining);
        workSpace = (DraggableGridView) findViewById(R.id.workSpace);
        workSpace.setColCount(10);
        wordsSpace = (MyWordsGridView) findViewById(R.id.wordsSpace);
        lettersSpace = (DraggableGridView) findViewById(R.id.lettersSpace);
        lettersSpace.setColCount(8);
        submitWord = (Button) findViewById(R.id.submitWord);
        addLetter = (Button) findViewById(R.id.addLetter);
        wordsSpace.setColCount(3);
        submitWord.setOnClickListener(submitListener());
        addLetter.setOnClickListener(addLetterListener());
        
        if(savedInstanceState != null)
	        if(savedInstanceState.getStringArray("dic") != null){
	        	Log.d(TAG,"restore the instance state of the dictionary");
	        	dic = new Dictionary(this,R.raw.dictionary_en,savedInstanceState.getStringArray("dic"));
	        	return;
	        }
        
        //other wise load the dictionary
        Log.d(TAG,"Load the dictionary the first time");                               //ethan's cheng
    	dic = new Dictionary(this,R.raw.dictionary_en);
    	
    	//start timer thread
	    timerHandler = new Handler();
		remainingTime = 300;
		timerHandler.post(timerRunnable);
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
				
				
				updateScore();
				
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
			
		}else{
			
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
				
				String word ="";
				for(int i=0;i<workSpace.getChildCount();i++){
					word+= ((Button)workSpace.getChildAt(i)).getText();
				}
				
				if(isWord(word)){
					workSpace.removeAllViews();
					
					Button w = newLetter(word);
					w.setOnClickListener(wordClickListener());
					
					wordsSpace.addView(w);
					
					
				}
				
				updateScore();
				
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
					Toast.makeText(ctx, ctx.getResources().getString(R.string.use_modified), Toast.LENGTH_LONG).show();
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
				updateScore();
				
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
					Toast.makeText(ctx, ctx.getResources().getString(R.string.invalid_word), Toast.LENGTH_LONG).show();
					return false;
				}
			}else{
				Toast.makeText(ctx, ctx.getResources().getString(R.string.must_use_previous_word)+"->"+modifyingWord, Toast.LENGTH_LONG).show();
				return false;
			}
			
		}else{
			if(dic.isWord(word)){
				return true;
			}else{
				Toast.makeText(ctx, ctx.getResources().getString(R.string.invalid_word)+"->"+word, Toast.LENGTH_LONG).show();
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

	protected void updateScore() {
		score.setText(getFinalScore());
		
	}

	private String getFinalScore(){
		score_int = wordsSpace.getScoreWords()-lettersSpace.getChildCount()+workSpace.getChildCount();
    	return String.valueOf(score_int);
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		String[] workSpaceArray = getStringArray( workSpace);
		String[] wordsSpaceArray = getStringArray( wordsSpace);
		String[] lettersSpaceArray = getStringArray( lettersSpace);
		
		outState.putStringArray("workSpace", workSpaceArray);
		outState.putStringArray("wordsSpace", wordsSpaceArray);
		outState.putStringArray("lettersSpace", lettersSpaceArray);
		outState.putInt("score_int", score_int);
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
			Log.d(TAG,"Vai guardar:"+ array[i]);
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

	
    
}
