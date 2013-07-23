package tests;
import mapgraph.*;
import indexstructures.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class TrieTests {

	@Test
	public void test() {
		RoadTrie root = new RoadTrie();
		root.addNode("My street", new Road("My street"));
		root.addNode("My street", new Road("My street2"));
		RoadTrie pre = root.getNode("My");
		//System.out.println(root.toString());
		System.out.printf("\n\n%s", pre.toString());
	}

}
