package indexstructures.roadtrie;

import java.util.*;

import mapgraph.RoadGroup;

public class RoadTrie {
	protected char letter;
	protected RoadGroup value;
	protected Set<RoadTrieNode> children;

	public RoadTrie() {
		children = new HashSet<RoadTrieNode>();
	}

	public RoadGroup getRoad(String roadName) {
		RoadTrie node = getNode(roadName);
		if(node != null) return node.value;
		else return null;
	}

	public TreeSet<String> matchRoads(String prefix) {
		RoadTrie node = getNode(prefix);
		if(node == null) return new TreeSet<String>();
		TreeSet<String> nodes = new TreeSet<String>();
		node.collectNodes(nodes, new StringBuilder(prefix.substring(0, prefix.length()-1)));
		return nodes;
	}

	protected void collectNodes(TreeSet<String> nodes, StringBuilder prefix) {

		prefix = new StringBuilder(prefix).append(letter);
		if(this.value != null) nodes.add(prefix.toString());
		for(RoadTrieNode child : children) {
			child.collectNodes(nodes, prefix);
		}
	}

	public RoadTrie getNode(String prefix) {
		if(prefix.length() == 0) return null;
		RoadTrie letterNode = getLetterNode(prefix.charAt(0));
		if(letterNode == null) return null;
		return letterNode.getNodeRec(prefix, 0);
	}

	public void addNode(String str, RoadGroup value) {
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

	public void addNode(char letter, RoadGroup value) {
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
		//DEBUG System.out.printf("At (%s, %d) %s\n", prefix, index, this.toString());
		if(index >= prefix.length()) return null;

		if(prefix.charAt(index) == this.letter) {
			if(prefix.length() == index+1) {
				return this;
			}
			else {
				index++;
				RoadTrie letterNode = getLetterNode(prefix.charAt(index));
				if(letterNode == null) return null;
				else {
					return letterNode.getNodeRec(prefix, index);
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
