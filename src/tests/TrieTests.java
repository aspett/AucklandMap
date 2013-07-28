package tests;
import mapgraph.*;
import indexstructures.*;
import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TrieTests {
	@Test
	public void testAbbey() {
		Graph g = new Graph();
		//g.loadRoads("/u/students/pettandr/COMP261/Assignments/AucklandMap/data");
		g.loadRoads("C:\\Users\\Andrew\\workspace\\AucklandMap\\small-data");
		//g.roadTrie.getRoad("abbey st newton");
		Set<String> s = g.roadTrie.matchRoads("a");
		for(String st : s) {
			System.out.println(st);
		}
		ArrayList<String> l = new ArrayList<String>(s);
		for(String st : l){
			System.out.println(st);
		}
	}

}
