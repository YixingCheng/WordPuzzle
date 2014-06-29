package com.funnywords;

import java.util.ArrayList;
import java.util.Collections;


import android.util.Log;

public class scoreClass {
      public static final int RANKLENGTH = 10;
      public static ArrayList<Integer> scoreRank;
      
      public scoreClass(){
    	   scoreRank = new ArrayList<Integer>();
    	   for(int i = 0; i < RANKLENGTH; i++ ){
    		       scoreRank.add(0);
    	     }
        }
      
      public static void sortScore() {
    	    try{
    	    	 Collections.sort(scoreRank);
    	    	 ArrayList<Integer> tempList = new ArrayList<Integer>();
    	    	 for(int i = 0; i < RANKLENGTH; i++){
    	    		     tempList.add(scoreRank.get(i));
    	    	   }
    	    	 scoreRank.clear();
    	    	 for(int i = 0; i < RANKLENGTH; i++){
	    		         scoreRank.add(tempList.remove(RANKLENGTH - 1 - i));
	    	       }	 
    	      } catch(NullPointerException e){
    	    	  Log.d(WRmainActivity.TAG, "the list is null!");
    	      }
       }
      
      public static void insertScore(int newHighScore){
    	      scoreRank.remove(RANKLENGTH - 1);
    	      scoreRank.add(newHighScore);
    	      sortScore();
         }
    
      public static int getMin(){
    	     return scoreRank.get(RANKLENGTH - 1);
         }
}
