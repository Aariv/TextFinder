package com.big.textfinder.aggregator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

}
