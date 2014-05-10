package com.funnywords;

import java.util.ArrayList;
import java.util.Random;

public class ScrabbleDistribution {
	
	static final char[] letters =    {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	static final int[] distribution ={ 9,  2,  2,  4,  13, 2,  3,  2,  9,  1,  1,  4,  2,  6,  8,  2,  1,  6,  4,  6,  4,  2,  2,  1,  2,  1 };
	
	ArrayList<Character> myLetters;
	Random rand;
	
	public ScrabbleDistribution(){
		rebuildLetters();
		rand = new Random();
	}

	private void rebuildLetters() {
		myLetters = new ArrayList<Character>();
		for(int i=0;i<letters.length;i++){
			for(int k=0;k<distribution[i];k++){
				myLetters.add(letters[i]);
			}	
		}	
	}
	
	public char getRandomLetter(){
		
		if(myLetters.size() == 0 || myLetters == null)
			rebuildLetters();

		int index = rand.nextInt(myLetters.size());
		char selected = myLetters.get(index);
		myLetters.remove(index);
		
		return selected;
	}
	
	
}
