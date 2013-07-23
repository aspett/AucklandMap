package tests;
import mapgraph.*;
import indexstructures.*;
import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TrieTests {

	@Test
	public void test() {
		RoadTrie root = new RoadTrie();
		root.addNode("My+street", new Road("My street"));
		root.addNode("My street", new Road("My street2"));
		TreeSet<String> nodes = root.matchRoads("My");
		System.out.println(nodes.toString());
	}
	/*@Test
	public void sbtests() {
		StringBuilder b = new StringBuilder();
		b = b.append("h");
		b.append("c");
		System.out.println(b);
	}*/

}
