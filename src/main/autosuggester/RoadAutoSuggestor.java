package main.autosuggester;

import indexstructures.RoadTrie;

import java.util.List;

public class RoadAutoSuggestor implements AutoSuggestor<String> {

	private RoadTrie trie;

	public RoadAutoSuggestor(RoadTrie trie) {
		this.trie = trie;
	}

	@Override
	public List<String> getSuggestions(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
