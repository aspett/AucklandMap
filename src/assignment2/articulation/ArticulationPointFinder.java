package assignment2.articulation;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class ArticulationPointFinder {
	Set<ArticulationFindable> rootNodes;
	Set<ArticulationFindable> allArtNodes;
	public ArticulationPointFinder(Set<ArticulationFindable> rootNodes) {
		if(rootNodes == null || rootNodes.size() < 1) throw new IllegalArgumentException("Nodes can not be null or empty");
		this.rootNodes = rootNodes;
		allArtNodes = new HashSet<ArticulationFindable>();
		
	}
	
	public void run(Set<ArticulationFindable> allNodes) {
		for(ArticulationFindable n : rootNodes) {
			allArtNodes.addAll(findArticulationPoints(n, allNodes));
		}
		for(ArticulationFindable n : allArtNodes) n.setArticulationPoint(true);
	}
	
	public boolean isArticulationNode(ArticulationFindable node) {
		return allArtNodes.contains(node);
	}
	
	public int numArtNodes() {
		return allArtNodes.size();
	}
	
	private Set<ArticulationFindable> findArticulationPoints(ArticulationFindable start, Set<ArticulationFindable> allNodes) {
		int numSubTrees = 0;
		Set<ArticulationFindable> articulationPoints = new HashSet<ArticulationFindable>();
		for(ArticulationFindable n : allNodes) {
			n.setDepth(-1);
			n.initArticulationNeighbours();
			n.setArticulationPoint(false);
		}
		start.setDepth(0);
		for(ArticulationFindable neighbour : start.getArticulationNeighbours()) {
			if(neighbour.getDepth() == -1) {
				findArticulationPoints(neighbour, start, articulationPoints );
				numSubTrees++;
			}
		}
		if(numSubTrees > 1) articulationPoints.add(start);
		return articulationPoints;
	}
	
	private Set<ArticulationFindable> findArticulationPoints(ArticulationFindable firstNode, ArticulationFindable root, Set<ArticulationFindable> articulationPoints) {
		Stack<StackNode> stack = new Stack<StackNode>();
		//Set<ArticulationFindable> articulationPoints = new HashSet<ArticulationFindable>();
		stack.push(new StackNode(firstNode, 1, new StackNode(root, 0, null)));
		while(stack.size() > 0) {
			StackNode elem = stack.peek();
			ArticulationFindable node = elem.node;
			if(elem.children == null) {
				node.setDepth(elem.depth);
				elem.reach = elem.depth;
				elem.children = new ArrayDeque<ArticulationFindable>();
				for(ArticulationFindable neighbour : node.getArticulationNeighbours()) 
					if(neighbour != elem.parent.node)
						elem.children.add(neighbour);	
			}
			else if(elem.children.size() > 0) {
				ArticulationFindable child = elem.children.poll();
				if(child.getDepth() != -1) 
					elem.reach = Math.min(elem.reach, child.getDepth());
				else
					stack.push(new StackNode(child, node.getDepth()+1, elem));
			}
			else {
				if(node != firstNode) {
					if(elem.reach >= elem.parent.depth) {
						articulationPoints.add(elem.parent.node);
					}
					elem.parent.reach = Math.min(elem.parent.reach, elem.reach);
				}
				stack.pop();
			}
		}
		return articulationPoints;
	}
	
	private class StackNode {
		public ArticulationFindable node;
		public int depth;
		public int reach;
		public StackNode parent;
		public Queue<ArticulationFindable> children;
		
		public StackNode(ArticulationFindable node, int depth, StackNode parent) {
			this.node = node;
			this.depth = depth;
			this.parent = parent;
			this.reach = -1;
		}
		
	}
}
