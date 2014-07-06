package com.funnywords;

import java.util.ArrayList;
import java.util.Collections;


import android.util.Log;

public class scoreClass {
      public static final int RANKLENGTH = 10;
      public static ArrayList<Integer> scoreRank;
      public static ArrayList<Integer> TAscoreRank;
      
      public scoreClass(){
    	   scoreRank = new ArrayList<Integer>();
    	   TAscoreRank = new ArrayList<Integer>();
    	   for(int i = 0; i < RANKLENGTH; i++ ){
    		       scoreRank.add(0);
    		       TAscoreRank.add(0);
    	     }
        }
      
      public static void sortScore() {
    	    try{
    	    	 Collections.sort(scoreRank);
    	    	 Collections.sort(TAscoreRank);
    	    	 ArrayList<Integer> tempList = new ArrayList<Integer>();
    	    	 ArrayList<Integer> TAtempList = new ArrayList<Integer>();
    	    	 for(int i = 0; i < RANKLENGTH; i++){
    	    		     tempList.add(scoreRank.get(i));
    	    		     TAtempList.add(TAscoreRank.get(i));
    	    	   }
    	    	 scoreRank.clear();
    	    	 TAscoreRank.clear();
    	    	 for(int i = 0; i < RANKLENGTH; i++){
	    		         scoreRank.add(tempList.remove(RANKLENGTH - 1 - i));
	    		         TAscoreRank.add(TAtempList.remove(RANKLENGTH - 1 - i));
	    	       }	 
    	      } catch(NullPointerException e){
    	    	  Log.d(WRmainActivity.TAG, "the list is null!");
    	      }
       }
      
      public static void insertScore(int newHighScore){
    	      scoreRank.remove(RANKLENGTH - 1);
    	      TAscoreRank.remove(RANKLENGTH - 1);
    	      scoreRank.add(newHighScore);
    	      TAscoreRank.add(newHighScore);
    	      sortScore();
         }
    
      public static int getMin(){
    	     return scoreRank.get(RANKLENGTH - 1);
         }
      
      public static int getTAMin(){
    	     return TAscoreRank.get(RANKLENGTH - 1); 
         }
}
