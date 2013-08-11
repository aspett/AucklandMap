package assignment2.astar;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import assignment2.unionfind.UnionFindable;

import mapgraph.*;
import main.*;


public class PathFinder implements MapDrawable {
	private PathFindable start;
	private PathFindable goal;
	private PriorityQueue<AStarTuple> fringe;
	private Set<RoadSegment> path;
	private Set<IntersectionNode> nodes = new HashSet<IntersectionNode>();
	private Stack<RoadSegment> directions;
	private Set<RoadSegment> selectedEdges;
	
	public PathFinder() {
		initializeVars();
	}
	
	public PathFinder(PathFindable start, PathFindable end) {
		this();
		if(fringe == null) throw new RuntimeException("Fringe on pathfinder should not be null");
		this.start = start;
		this.goal = end;
		
	}
	
	private void initializeVars() {
		fringe = new PriorityQueue<AStarTuple>();
		path = new HashSet<RoadSegment>();
		directions = new Stack<RoadSegment>();
		selectedEdges = new HashSet<RoadSegment>();
	}
	public void buildPath(Graph g) {
		initializeVars();
		buildDirectionsSegments(g);
	}
	private void buildDirectionsSegments(Graph g) {
		PathFindable end = getPath(g);
		directions = new Stack<RoadSegment>();
		if(end == null) { return; }
		double len = 0;
		while(end.getPathFrom() != null) {
			nodes.add((IntersectionNode) end);
			RoadSegment edge = end.getEdgesIn().get(end.getPathFrom());
			path.add(edge);
			directions.push(edge);
			//directions = String.format("%s\nRoad: %s, length: %f", directions, edge.getParentRoad().getName(), edge.getLength());
			len += edge.getLength();
			end = end.getPathFrom();
		}
		selectedEdges = new HashSet<RoadSegment>(directions);
		//directions = String.format("%s\nLength: %f\n", directions, len);
	}
	private PathFindable getPath(Graph g) {
		if(start == null || goal == null) throw new RuntimeException("No start/end");//return null;//"No path available; either start or end node not set.";
		for(PathFindable node : g.getNodes().values()) {
			node.setVisited(false);
			node.setPathCost(0);
			node.setPathFrom(null);
		}
		fringe.add(new AStarTuple(start, null, 0, estimate(start, goal)));
		while(fringe.size() > 0) {
			AStarTuple currentTuple = fringe.poll();
			PathFindable node = currentTuple.node, from = currentTuple.from;
			double costToHere = currentTuple.costToHere, totalCostToGoal = currentTuple.totalCostToGoal;
			
			if(!node.isVisited()) {
				node.setVisited(true);
				node.setPathFrom(from);
				node.setPathCost(costToHere);
				
				if(node == this.goal) { return node; } //System.out.println("END");
				
				for(Map.Entry<PathFindable, RoadSegment> e : node.getEdgesOut().entrySet()) {
					PathFindable neighbour = e.getKey();
					RoadSegment edge = e.getValue();
					Road r = edge.getParentRoad();
					float roadSpeed = r.roadSpeedWithClass();
					if(!AucklandMap.USEALLROADS && r.getNotforcar() == 1) continue;

					if(!neighbour.isVisited()) {
						double costToNeighbour;
						double estimatedTotal;
						if(!AucklandMap.ASTARTIMEBASED) {
							costToNeighbour = currentTuple.costToHere + edge.getLength();
							estimatedTotal = costToNeighbour + estimate(neighbour, this.goal);
						}
						else {
							costToNeighbour = currentTuple.costToHere + (edge.getLength()/(roadSpeed));
							estimatedTotal = costToNeighbour + estimateTimeBased(neighbour, this.goal);
						}
						fringe.add(new AStarTuple(neighbour, node, costToNeighbour, estimatedTotal));
					}
				}
			}
		}
		return null;// no path
	}

	private double estimateTimeBased(PathFindable node1, PathFindable node2) {
		Location loc1, loc2;
		loc1 = node1.getLocation();
		loc2 = node2.getLocation();
		
		double dist = loc1.distanceTo(loc2);
		return dist/110;
	}

	public PathFindable getStart() {
		return start;
	}

	public void setStart(IntersectionNode start) {
		this.start = start;
	}

	public PathFindable getEnd() {
		return goal;
	}

	public void setEnd(PathFindable end) {
		this.goal = end;
	}
	
	private double estimate(PathFindable node1, PathFindable node2) {
		Location loc1, loc2;
		loc1 = node1.getLocation();
		loc2 = node2.getLocation();
		
		double dist = loc1.distanceTo(loc2);
		return dist;
	}
	
	private class AStarTuple implements Comparable<AStarTuple> {
		public PathFindable node, from;
		public double costToHere, totalCostToGoal; 
		public AStarTuple(PathFindable node, PathFindable from, double costToHere, double totalCostToGoal) {
			this.node = node;
			this.from = from;
			this.costToHere = costToHere;
			this.totalCostToGoal = totalCostToGoal;
		}
		@Override
		public int compareTo(AStarTuple other) {
			if(this.totalCostToGoal < other.totalCostToGoal) return -1;
			if(this.totalCostToGoal > other.totalCostToGoal) return 1;
			return 0;
		}
	}
	
	public String getDirections() {
		Stack<RoadSegment> dirs = (Stack<RoadSegment>)directions.clone();
		if(dirs.size() == 0) return "No route found";
		StringBuilder out = new StringBuilder("");
		double dist = 0;
		double time = 0;
		String lastName = "";
		double lastLen = 0;
		boolean reset = false;
		while(dirs.size() > 0) {
			RoadSegment seg = dirs.pop();
			Road r = seg.getParentRoad();
			String name = String.format("%s %s", seg.getParentRoad().getName(), seg.getParentRoad().getCity());
			if(lastName.equals("")) {
				lastName = name;
				lastLen = seg.getLength();
				//lastTime = seg.getLength() / seg.getParentRoad().getSpeed();
			}
			else if(lastName.equals(name)) {
				lastLen += seg.getLength();
				//lastTime += seg.getLength() / seg.getParentRoad().getSpeed();
			}
			else {
				out.append(String.format("%s: \t%4.2fkm\n", lastName, lastLen));
				dist += lastLen;
				//time += seg.getLength() / seg.getParentRoad().getRealSpeed();
				lastName = name;
				lastLen = seg.getLength();
				//lastTime = seg.getLength() / seg.getParentRoad().getSpeed();
			}
			time += (seg.getLength() / r.getRealSpeed());
		}
		out.append(String.format("%s: \t%4.2fkm\n", lastName, lastLen));
		dist += lastLen;
		//time += lastTime;
		out.append(String.format("Total length: %4.2fkm\n", dist));
		out.append(String.format("Total time: %4.2f hours\n", time));
		out.append(String.format("Using %s based path finding.\n", AucklandMap.ASTARTIMEBASED?"time":"distance"));
		return out.toString();
		
	}

	@Override
	public void draw(Graphics2D g, Location origin, double scale, double zoom) {
		for(RoadSegment s : path) {
			s.draw(g, origin, scale, 1.0);
		}
	}
	
	public boolean isEdgeSelected(RoadSegment s) {
		return selectedEdges.contains(s);
	}
}
