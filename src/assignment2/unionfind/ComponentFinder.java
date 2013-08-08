package assignment2.unionfind;

import java.util.*;

import assignment2.astar.*;
import main.*;
import mapgraph.*;

public class ComponentFinder {
	private Set<UnionFindable> nodes;
	
	public ComponentFinder() {
		nodes = new HashSet<UnionFindable>();
	}
	
	public ComponentFinder(Set<UnionFindable> hashSet) {
		nodes = hashSet;
	}
	
	public void union(UnionFindable x, UnionFindable y) {
		UnionFindable xroot = x.findKruskParent(), yroot = y.findKruskParent();
		if(xroot == yroot) return;
		if(xroot.getKruskRank() < yroot.getKruskRank()) {
			xroot.setKruskParent(yroot);
			nodes.remove(xroot);
		}
		else {
			yroot.setKruskParent(xroot);
			nodes.remove(yroot);
			if(xroot.getKruskRank() == yroot.getKruskRank())
				xroot.setKruskRank(xroot.getKruskRank()+1);
		}
	}
	
	public int numberComponents() {
		return nodes.size();
	}
	
	public boolean add(UnionFindable node) {
		return nodes.add(node);
	}
	
	public boolean isRootNode(UnionFindable node) {
		return nodes.contains(node);
	}
	
	public Set<UnionFindable> rootNodes() {
		return nodes;
	}
}
