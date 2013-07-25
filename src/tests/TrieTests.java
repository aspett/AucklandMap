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
		g.loadRoads("/u/students/pettandr/COMP261/Assignments/AucklandMap/data");
		//g.roadTrie.getRoad("abbey st newton");
		Road r = g.roadTrie.getRoad("abbey st newton");
		System.out.println(r.toString());
	}

}
