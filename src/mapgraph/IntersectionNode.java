package mapgraph;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.*;

import assignment2.PathFindable;

import main.AucklandMap;
import main.Location;

/**
 * Node representing an intersection
 * @author Andrew
 *
 */
public class IntersectionNode implements MapDrawable, PathFindable {
	private Map<PathFindable, RoadSegment> edgesOut;
	private Map<PathFindable, RoadSegment> edgesIn;
	private Location location;
	private Integer id;
	private static IntersectionNode selectedNode;
	private static IntersectionNode selectedNode2;
	private boolean visited;
	private PathFindable from;
	private double pathCost;
	
	public IntersectionNode(String[] line) throws NumberFormatException {
		edgesOut = new HashMap<PathFindable, RoadSegment>();
		edgesIn = new HashMap<PathFindable, RoadSegment>();
		id = Integer.parseInt(line[0]);
		location = Location.newFromLatLon(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
	}

	public void drawhigh(Graphics2D g, Location origin, double scale) {
		IntersectionNode sel = selectedNode;
		selectedNode = this;
		draw(g,origin,scale);
		selectedNode=sel;
	}
	public void draw(Graphics2D g, Location origin, double scale) {
		Point d = location.getPoint(origin, scale);
		if(this == selectedNode || this == selectedNode2) {
			if(this == selectedNode)
				g.setColor(Color.GREEN);
			else
				g.setColor(Color.RED);
			g.fillOval(d.x-4, d.y-4, 8, 8);
		}
		else {
			g.setColor(Color.BLUE);
			if(this.visited && AucklandMap.SHOWASTARVISITED)
				g.setColor(Color.ORANGE);
			g.fillOval(d.x-2, d.y-2, 4, 4);
		}
		g.setColor(Color.BLACK);

	}

	public Location getLocation() {
		return this.location;
	}

	public Integer getID() {
		return this.id;
	}

	public String toString() {
		return String.format("[%d, (%f, %f)]", id, location.x, location.y);
	}

	public static void setSelectedNode(IntersectionNode n) {
		IntersectionNode.selectedNode = n;
	}
	
	public static void setStartNode(IntersectionNode n) {
		IntersectionNode.selectedNode = n;
	}
	public static void setEndNode(IntersectionNode n) {
		IntersectionNode.selectedNode2 = n;
	}
	
	public static IntersectionNode getStartNode() {
		return IntersectionNode.selectedNode;
	}
	public static IntersectionNode getEndNode() {
		return IntersectionNode.selectedNode2;
	}

	public void addEdgeOut(RoadSegment s, IntersectionNode nodeTo) {
		edgesOut.put(nodeTo, s);
	}
	public void addEdgeIn(RoadSegment s, IntersectionNode nodeFrom) {
		edgesIn.put(nodeFrom, s);
	}
	private Set<String> getRoads(Map<PathFindable, RoadSegment> set) {
		HashSet<String> ret = new HashSet<String>();
		for(RoadSegment rs : set.values()) {
			Road r = rs.getParentRoad();
			String name = String.format("%s %s", r.getName(), r.getCity());
			ret.add(name);
		}
		return ret;
	}
	public Set<String> getRoadsIn() {
		return getRoads(this.edgesIn);
	}
	public Set<String> getRoadsOut() {
		return getRoads(this.edgesOut);
	}
	public Set<String> getAllRoads() {
		Map<PathFindable, RoadSegment> ret = new HashMap<PathFindable, RoadSegment>(this.edgesIn);
		ret.putAll(this.edgesOut);
		return getRoads(ret);
	}


	@Override
	public boolean isVisited() {
		return visited;
	}


	@Override
	public void setVisited(boolean visited) {
		this.visited = visited;
	}


	@Override
	public Map<PathFindable, RoadSegment> getEdgesOut() {
		return edgesOut;
	}


	@Override
	public Map<PathFindable, RoadSegment> getEdgesIn() {
		return edgesIn;
	}


	@Override
	public void setPathFrom(PathFindable from) {
		this.from = from;
		
	}


	@Override
	public PathFindable getPathFrom() {
		return this.from;
		
	}


	@Override
	public double getPathCost() {
		return this.pathCost;
	}


	@Override
	public void setPathCost(double cost) {
		this.pathCost = cost;		
	}
}
