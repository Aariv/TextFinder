package com.big.textfinder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.big.textfinder.aggregator.ResultPrinter;
import com.big.textfinder.model.WordLocation;
import com.big.textfinder.processor.FileProcessor;

/**
 * Design and implement a simple Java program to find specific strings in a
 * large text. 
 * 
 * The program should be composed of the following modules: 
 * 
 * 1. The main module - reads a large text file in parts (e.g. 1000 lines in each part)
 * 	and sends each part (as string) to a matcher. After all matchers completed,
 * it calls the aggregator to combine and print the results 
 * 
 * 2. The matcher - gets a text string as input and searches for matches of a given set of
 * strings. The result is a map from a word to its location(s) in the text 
 * 
 * 3. The aggregator - aggregates the results from all the matchers and prints the results.
 * 
 * @author Ariv
 *
 */
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
		// STEP 1: Process the file
		ConcurrentHashMap<String, List<WordLocation>> allMatches = fileProcessor.processFile("/Users/al/Downloads/big.txt", SEARCH_TERMS);
		ResultPrinter printer = new ResultPrinter();
		
		// STEP 3: Get the results from all the matchers.
		printer.printResults(allMatches);
	}
}
