package com.big.textfinder.matcher;

import java.util.List;
import java.util.Map;

import com.big.textfinder.model.WordLocation;

public interface Matcher {

	Map<String, List<WordLocation>> findMatches(List<String> lines, int startLine);
}
