package indexstructures;

import java.util.HashSet;
import java.util.Set;

import mapgraph.Road;

public class RoadTrie {
	protected char letter;
	protected Road value;
	protected Set<RoadTrieNode> children;
	
	public RoadTrie() {
		children = new HashSet<RoadTrieNode>();
	}

	public Road hasRoad(String roadName) {
		RoadTrie node = getNode(roadName);
		if(node != null) return node.value;
		else return null;
	}
	
	public RoadTrie getNode(String prefix) {
		//RoadTrie letterNode
		//TODO
		return getNodeRec(prefix, 0);
	}
	
	public void addNode(String str, Road value) {
		if(str.length() == 1) addNode(str.charAt(0), value);
		else {
			RoadTrie letterNode = getLetterNode(str.charAt(0));
			if(letterNode == null) {
				letterNode = new RoadTrieNode(str.charAt(0), null, null);
				children.add((RoadTrieNode)letterNode);
			}
			letterNode.addNode(str.substring(1), value);
		}
	}
	
	public void addNode(char letter, Road value) {
		if(!hasLetter(letter))
			children.add(new RoadTrieNode(letter, value, null));
	}
	
	protected boolean hasLetter(char letter) {
		for(RoadTrieNode n: children) {
			if(n.letter == letter) return true;
		}
		return false;
	}
	
	protected RoadTrie getLetterNode(char letter) {
		for(RoadTrieNode n: children) {
			if(n.letter == letter) return n;
		}
		return null;
	}
	
	protected RoadTrie getNodeRec(String prefix, int index) {
		System.out.printf("At %s\n", this.toString());
		if(index >= prefix.length()) return null;
		
		if(prefix.charAt(index) == this.letter) {
			System.out.println("1");
			if(prefix.length() == index+1) {
				return this;
			}
			else {
				index++;
				RoadTrie letterNode = getLetterNode(prefix.charAt(index));
				if(letterNode == null) return null;
				else {
					return getNodeRec(prefix, index);
				}
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("{ROOT, %s, %s}", value, children);
	}

}
