package com.big.textfinder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.big.textfinder.aggregator.ResultPrinter;
import com.big.textfinder.model.WordLocation;
import com.big.textfinder.processor.FileProcessor;

public class TextFinderApp {

	private static final Set<String> SEARCH_TERMS = new HashSet<String>(
			Arrays.asList(
			"James", "John", "Robert", "Michael", "William",
            "David", "Richard", "Charles", "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald",
            "George", "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary",
            "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory",
            "Joshua", "Jerry", "Dennis", "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur",
            "Ryan", "Roger"));

	public static void main(String[] args) {
		FileProcessor fileProcessor = new FileProcessor();
		// Step 1: Process the file
		ConcurrentHashMap<String, List<WordLocation>> allMatches = fileProcessor.processFile("/Users/al/Downloads/big.txt", SEARCH_TERMS);
		ResultPrinter printer = new ResultPrinter();
		// Step 3: Get the results from all the matchers.
		printer.printResults(allMatches);
	}
}
