package com.missedops.mainswipetabs;

import snowballstemmer.EnglishStemmer;

public class MatchingActivity {

	EnglishStemmer stemmer = new EnglishStemmer();

	public String stemWord(String word) {
		// set the word to be stemmed
		stemmer.setCurrent("dreadful");
		// call stem() method. stem() method returns boolean value.
		if (stemmer.stem())
			return stemmer.getCurrent();
		else
			return null;

	}
}
