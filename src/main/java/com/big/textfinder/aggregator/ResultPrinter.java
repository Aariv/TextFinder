package com.big.textfinder.aggregator;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.big.textfinder.model.WordLocation;

public class ResultPrinter {

	public void printResults(ConcurrentHashMap<String, List<WordLocation>> allMatches) {
		allMatches.forEach((k, v) -> {
			System.out.println(k + " --> " + v);
		});
	}

}
