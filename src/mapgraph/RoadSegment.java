package mapgraph;
import java.util.*;
import main.*;

public class RoadSegment {
	private Integer roadID;
	private double length;
	private IntersectionNode nodeFrom;
	private IntersectionNode nodeTo;
	private List<Location> coords;

	public RoadSegment(String[] line) throws NumberFormatException{
		coords = new ArrayList<Location>();

		roadID = Integer.parseInt(line[0]);
		length = Double.parseDouble(line[1]);
		//TODO convert to IntersectionNode objects.
		int nodeFrom = Integer.parseInt(line[2]);
		int nodeTo = Integer.parseInt(line[3]);
		for(int i = 4; i < line.length; i += 2) {
			Double lat = Double.parseDouble(line[i]);
			Double lon = Double.parseDouble(line[i+1]);
			coords.add(Location.newFromLatLon(lat, lon));
		}
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


}
