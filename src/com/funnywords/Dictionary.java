package com.funnywords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import android.app.Activity;
import android.util.Log;

public class Dictionary {
	private final static String TAG = "WR.Dictionary";
	
	int resId;
	String[] words;
	Activity ctx;
	
	public Dictionary(Activity ctx,int resId) {
		this.ctx = ctx;
		this.resId = resId;
		words = new String[354934];
		buildDictionary();
		Log.d(TAG, "Dictionary has been successfully loaded");
	}

	public Dictionary(Activity ctx, int resId, String[] stringArray) {
		this.ctx = ctx;
		this.resId = resId;
		this.words = stringArray;
	}

	private void buildDictionary() {
		InputStream inputStream = ctx.getResources().openRawResource(resId);         //read raw dictionary file

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
