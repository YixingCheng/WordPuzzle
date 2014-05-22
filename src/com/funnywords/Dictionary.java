package com.funnywords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class Dictionary {
	
	int resId;
	String[] words;
	Activity callingAct;    //the calling activity
	
	// better to use context for the first parameter
	public Dictionary(Activity activity, int resId) {
		this.callingAct = activity;
		this.resId = resId;
		words = new String[354934];
		// this is where do IO to load dictionary
		buildDictionary();
		Log.d(WRmainActivity.TAG, "after build dictionary");
	}

	public Dictionary(Activity activity, int resId, String[] stringArray) {
		// this.ctx = ctx;
		this.callingAct = activity;
		this.resId = resId;
		this.words = stringArray;
	}

	private void buildDictionary() {
		InputStream inputStream = callingAct.getResources().openRawResource(resId);         //read raw dictionary file
	    InputStreamReader inputreader = new InputStreamReader(inputStream);
	    BufferedReader buffreader = new BufferedReader(inputreader);
	    String line;
	    int i=0;
	    
	    try {
	        while (( line = buffreader.readLine()) != null) {
	        	words[i]=line.toUpperCase();       
	            //Log.d("DBG",words[i]);
	            i++;
	        }
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
		
	}

	public boolean isWord(String input){
		return Arrays.binarySearch(words, input)>=0;
	}
	
	public String[] getWords(){
		return words;
	}
	
	public void setWords(String[] words){
		this.words = words;
	}

}
