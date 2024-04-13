package com.big.textfinder.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.big.textfinder.model.WordLocation;

public class TextMatcher implements Matcher {

	private final Set<String> searchTerms;

	public TextMatcher(Set<String> searchTerms) {
		this.searchTerms = searchTerms;
	}

	@Override
	public Map<String, List<WordLocation>> findMatches(List<String> lines, int startLine) {
		Map<String, List<WordLocation>> matches = new HashMap<>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			for (String term : searchTerms) {
				int index = line.indexOf(term);
				while (index >= 0) {
					matches.computeIfAbsent(term, k -> new ArrayList<>()).add(new WordLocation(startLine + i, index));
					index = line.indexOf(term, index + 1);
				}
			}
		}
		return matches;
	}

}
