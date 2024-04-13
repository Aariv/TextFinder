package com.big.textfinder.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.big.textfinder.matcher.TextMatcher;
import com.big.textfinder.model.WordLocation;

public class FileProcessor {

	private static final int LINES_PER_PART = 1000;

	/**
	 * Process the given file.
	 * 
	 * @param filePath
	 * @param searchTerms
	 * @return
	 */
	public ConcurrentHashMap<String, List<WordLocation>> processFile(String filePath, Set<String> searchTerms) {
		System.out.println("Processing the given file " + filePath);
		System.out.println("========================");
		ExecutorService executor = Executors.newFixedThreadPool(10);
		ConcurrentHashMap<String, List<WordLocation>> allMatches = new ConcurrentHashMap<>();
		// Used streams to improve the performance
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {

			List<String> part = new ArrayList<>();
			int lineNumber = 0;

			for (Iterator<String> it = lines.iterator(); it.hasNext();) {
				part.add(it.next());
				lineNumber++;

				if (lineNumber % LINES_PER_PART == 0 || !it.hasNext()) {
					List<String> finalPart = new ArrayList<>(part);
					int finalLineNumber = lineNumber;
					/*
					 * STEP 2: The concurrent matcher
					 */
					executor.submit(() -> {
						TextMatcher matcher = new TextMatcher(searchTerms);
						Map<String, List<WordLocation>> matches = matcher.findMatches(finalPart,
								finalLineNumber - finalPart.size() + 1);

						matches.forEach((k, v) -> allMatches.merge(k, v, (list1, list2) -> {
							list1.addAll(list2);
							return list1;
						}));
					});
					part.clear();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Properly close the connections;
		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return allMatches;
	}

}
