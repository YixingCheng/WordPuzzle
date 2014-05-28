package com.funnywords;

import java.util.ArrayList;
import java.util.Collections;

import android.util.Log;

public class scoreClass {
      public static final int RANKLENGTH = 10;
      public static ArrayList<Integer> scoreRank;
      
      public scoreClass(){
    	   scoreRank = new ArrayList<Integer>();
        }
      
      public static ArrayList<Integer> sortScore(ArrayList<Integer> unsortedList) {
    	    try{
    	    	 Collections.sort(unsortedList);
    	    	 
    	      } catch(NullPointerException e){
    	    	  Log.d(WRmainActivity.TAG, "the list is null!");
    	      }
    	    
			return unsortedList;
       }
      
      public static ArrayList<Integer> insertScore(int newHighScore, ArrayList<Integer> originalList){
    	      scoreRank.remove(RANKLENGTH - 1);
    	      scoreRank.add(newHighScore);
    	      scoreRank = sortScore(originalList);
              return scoreRank;
        }
      
      public static ArrayList<Integer> getRank() {
    	    return scoreRank;  
        }
      
      public static ArrayList<Integer> updateScoreRank(int score){
            if (scoreRank.size()< RANKLENGTH){
            	    scoreRank.add(score);
               }  
            else{
            	  scoreRank = insertScore(score, scoreRank);
               }
    	    return scoreRank;
        }
}
