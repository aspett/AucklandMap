package indexstructures;

import mapgraph.*;
import java.util.*;

public class RoadTrieNode extends RoadTrie {


	public RoadTrieNode(char letter, Road value, Set<RoadTrieNode> children) {
		if((letter > 90 || letter < 48) && letter != 32) throw new RuntimeException();
		assert (letter >= 48 && letter < 91) || letter == 32; //Ensure that the letter is either a space or alpha numerical.
		this.letter = letter;
		this.value = value;
		if(children != null) this.children = new HashSet<RoadTrieNode>(children); //Ensure we're dealing with a hash set. Probably faster.
		else this.children = new HashSet<RoadTrieNode>();
	}

	public Road getRoad(String roadName) {
		if(roadName.charAt(0) == letter) { //If this node's letter is the first letter in the string
			if(roadName.length() > 1) { //If the length of the string is longer than just this letter
				if(hasLetter(roadName.charAt(1))) {
					return getRoad(roadName.substring(1));
				}
			}
			else { //If it's the only letter, than we want to check immediately
				return value;
			}
		}
		return null;
	}

	/*public boolean hasRoad(String roadName) {
		return getRoad(roadName) != null;
	}*/

	public String toString() {
		return String.format("\n{%c, %s, %s}", letter, value, children);
	}



}
