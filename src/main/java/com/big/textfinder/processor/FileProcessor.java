package com.big.textfinder.processor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
		// Used streams to improve the performance and will not load all the file content at a time.
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			List<String> part = new ArrayList<>();
			int lineNumber = 0;
			for (Iterator<String> it = lines.iterator(); it.hasNext();) {
				part.add(it.next());
				lineNumber++;

				if (lineNumber % LINES_PER_PART == 0 || !it.hasNext()) {
					List<String> finalPart = new ArrayList<>(part);
					int finalLineNumber = lineNumber;
					executor.submit(() -> {
						TextMatcher matcher = new TextMatcher(searchTerms);
						Map<String, List<WordLocation>> matches = matcher.findMatches(finalPart,
								finalLineNumber - finalPart.size() + 1);
						// Note:- The code may still run into OutOfMemoryError issues with a 5GB input file on
						// a system with only 2GB of RAM. This is because the code is storing all
						// matches in memory in the allMatches map. If the file contains a large number
						// of matches, this map could potentially consume a lot of memory.
						
						// Instead of storing all matches in memory, write them to a file or a database
						// as you find them. This would allow you to handle files of any size, limited
						// only by the amount of disk space available.
						matches.forEach((k, v) -> allMatches.merge(k, v, (list1, list2) -> {
							list1.addAll(list2);
							return list1;
						}));
						
//						writeMatchesToFile(matches);
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

	private static void writeMatchesToFile(Map<String, List<WordLocation>> matches) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("/Users/al/Downloads/matches.txt", true))) {
            matches.forEach((k, v) -> {
                writer.println(k + " --> " + v);
            });
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
