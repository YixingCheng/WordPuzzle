package com.funnywords;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyWordsGridView extends DraggableGridView{

	public MyWordsGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}
	
	public int getScoreWords(){
		int score=0;
		for(int i=0;i<this.getChildCount();i++ ){
			score += ((TextView) this.getChildAt(i)).getText().length();
			
		}
		
		return score;
		
	}

}
