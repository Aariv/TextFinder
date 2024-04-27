package com.big.textfinder.aggregator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.big.textfinder.model.WordLocation;

public class ResultPrinter {

	public void printResults(ConcurrentHashMap<String, List<WordLocation>> allMatches) {
		allMatches.forEach((k, v) -> {
			System.out.println(k + " --> " + v);
		});
	}

	public void printResultsInFile(ConcurrentHashMap<String, List<WordLocation>> allMatches) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("/Users/al/Downloads/results.txt"))) {
            allMatches.forEach((k, v) -> {
                writer.println(k + " --> " + v);
            });
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
	
	public void printResultsInFile(Map<String, List<WordLocation>> allMatches) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("/Users/al/Downloads/results.txt"))) {
            allMatches.forEach((k, v) -> {
                writer.println(k + " --> " + v);
            });
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

	public Map<String, List<WordLocation>> constructResultFromFile() {
	    Map<String, List<WordLocation>> result = new HashMap<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader("/Users/al/Downloads/matches.txt"))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(" --> ");
	            String key = parts[0];
	            List<WordLocation> value = Arrays.stream(parts[1].substring(1, parts[1].length() - 1).split(", "))
	                    .map(s -> {
	                        String[] offsets = s.substring(s.indexOf('=') + 1, s.lastIndexOf(',')).split(", ");
	                        return new WordLocation(Integer.parseInt(offsets[0]), Integer.parseInt(offsets[1]));
	                    })
	                    .collect(Collectors.toList());
	            result.put(key, value);
	        }
	    } catch (IOException e) {
	        System.out.println("An error occurred while reading from the file: " + e.getMessage());
	    }

	    return result;
	}
}
