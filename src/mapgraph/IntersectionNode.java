package mapgraph;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.*;

import main.Location;

/**
 * Node representing an intersection
 * @author Andrew
 *
 */
public class IntersectionNode implements MapDrawable {
	private Set<RoadSegment> edgesOut;
	private Set<RoadSegment> edgesIn;
	private Location location;
	private Integer id;
	private static IntersectionNode selectedNode;

	public IntersectionNode(String[] line) throws NumberFormatException {
		edgesOut = new HashSet<RoadSegment>();
		edgesIn = new HashSet<RoadSegment>();
		id = Integer.parseInt(line[0]);
		location = Location.newFromLatLon(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
	}


	public void draw(Graphics2D g, Location origin, double scale) {
		Point d = location.getPoint(origin, scale);
		if(this == selectedNode) {
			g.setColor(Color.RED);
			g.fillOval(d.x-3, d.y-3, 6, 6);
		}
		else {
			g.setColor(Color.BLUE);
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

	public void addEdgeOut(RoadSegment s) {
		edgesOut.add(s);
	}
	public void addEdgeIn(RoadSegment s) {
		edgesIn.add(s);
	}
	private Set<String> getRoads(Set<RoadSegment> set) {
		HashSet<String> ret = new HashSet<String>();
		for(RoadSegment rs : set) {
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
		HashSet<RoadSegment> ret = new HashSet<RoadSegment>(this.edgesIn);
		ret.addAll(this.edgesOut);
		return getRoads(ret);
	}
}
