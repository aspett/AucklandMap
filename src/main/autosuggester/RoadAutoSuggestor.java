package main.autosuggester;

import indexstructures.RoadTrie;

import java.util.ArrayList;
import java.util.List;

public class RoadAutoSuggestor implements AutoSuggestor<String> {

	private RoadTrie roadTrie;

	public RoadAutoSuggestor(RoadTrie roadTrie) {
		this.roadTrie = roadTrie;
		System.out.println("Initialized autosuggestor");
	}

	@Override
	public List<String> getSuggestions(String query) {
		ArrayList<String> rn = new ArrayList<String>(this.roadTrie.matchRoads(query));
		return rn;
	}

}
