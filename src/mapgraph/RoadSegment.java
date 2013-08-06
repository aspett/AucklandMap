package mapgraph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Path2D;
import java.util.*;

import assignment2.PathFindable;
import main.*;

public class RoadSegment implements MapDrawable {
	private static final Color SELECTEDCOLOR = Color.red;
	private Integer roadID;
	private double length;
	private Road belongsToRoad;
	private IntersectionNode nodeFrom;
	private IntersectionNode nodeTo;
	private List<Location> coords;
	private boolean selected;

	public RoadSegment(String[] line, Graph g) throws NumberFormatException{
		coords = new ArrayList<Location>();
		selected = false;

		roadID = Integer.parseInt(line[0]);
		length = Double.parseDouble(line[1]);
		int nodeFrom = Integer.parseInt(line[2]);
		this.nodeFrom = g.getNodeByID(nodeFrom);
		int nodeTo = Integer.parseInt(line[3]);
		this.nodeTo = g.getNodeByID(nodeTo);
		for(int i = 4; i < line.length; i += 2) {
			Double lat = Double.parseDouble(line[i]);
			Double lon = Double.parseDouble(line[i+1]);
			coords.add(Location.newFromLatLon(lat, lon));
		}
	}

	public void draw(Graphics2D g2d, Location origin, double scale) {
		Path2D path = new Path2D.Double();
		//System.out.printf("%d / %d\n", i++, edges.size());
		boolean first = true;
		for(Location l : getCoords()) {
			double x = l.getPoint(origin, scale).x;
			double y = l.getPoint(origin, scale).y;
			if(first) {
				first = false;
				path.moveTo(x, y);
				//path.moveTo()
				//TODO use .getPoint on Location
			}
			else {
				path.lineTo(x, y);
			}
		}
		/*Stroke str = g2d.getStroke();
		if(this.selected){
			g2d.setColor(SELECTEDCOLOR);
			g2d.setStroke(new BasicStroke(3));
		}*/

		g2d.draw(path);
		/*if(this.selected) { 
			g2d.setStroke(str);
			g2d.setColor(Color.BLACK);
		}*/
	}

	@Override
	public String toString() {
		return "RoadSegment [roadID=" + roadID + ", length=" + length
				+ ", nodeFrom=" + nodeFrom + ", nodeTo=" + nodeTo + ", coords="
				+ coords + "]";
	}

	public IntersectionNode getNodeFrom() {
		return nodeFrom;
	}

	public void setNodeFrom(IntersectionNode nodeFrom) {
		this.nodeFrom = nodeFrom;
	}

	public IntersectionNode getNodeTo() {
		return nodeTo;
	}
	
	public IntersectionNode getOtherNode(PathFindable node) {
		return nodeFrom == node ? nodeTo : nodeFrom;
	}

	public void setNodeTo(IntersectionNode nodeTo) {
		this.nodeTo = nodeTo;
	}

	public Integer getRoadID() {
		return roadID;
	}

	public double getLength() {
		return length;
	}

	public List<Location> getCoords() {
		return coords;
	}

	public void setSelected(boolean sel) {
		this.selected = sel;
	}
	public boolean isSelected() {
		return this.selected;
	}

	public void setParentRoad(Road r) {
		this.belongsToRoad = r;
	}
	
	public Road getParentRoad() {
		return this.belongsToRoad;
	}

	public void setOneWay(Integer oneway) {
		/*if(this.belongsToRoad.getName().equals("ngapawa st")) {
			System.out.printf("%s added edge in: %s\n", this.nodeTo, this);
			System.out.printf("%s added edge out: %s\n", this.nodeFrom, this);
		}*/
		this.nodeFrom.addEdgeOut(this, nodeTo);
		this.nodeTo.addEdgeIn(this, nodeFrom);
		if(oneway == 0) {
			/*if(this.belongsToRoad.getName().equals("ngapawa st")) {
				System.out.printf("%s added edge in: %s\n", this.nodeFrom, this);
				System.out.printf("%s added edge out: %s\n", this.nodeTo, this);
			}*/
			this.nodeFrom.addEdgeIn(this, nodeTo);
			this.nodeTo.addEdgeOut(this, nodeFrom);
		}
		
	}


}
