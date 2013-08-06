package main.autosuggester;


import java.util.ArrayList;
import java.util.List;
import indexstructures.roadtrie.*;

public class RoadAutoSuggestor implements AutoSuggestor<String> {

	private RoadTrie roadTrie;

	public RoadAutoSuggestor(RoadTrie roadTrie) {
		this.roadTrie = roadTrie;
	}

	@Override
	public List<String> getSuggestions(String query) {
		ArrayList<String> rn = new ArrayList<String>(this.roadTrie.matchRoads(query));
		return rn;
	}

}
