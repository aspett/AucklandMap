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

	public IntersectionNode(String[] line) throws NumberFormatException {
		id = Integer.parseInt(line[0]);
		location = Location.newFromLatLon(Double.parseDouble(line[1]), Double.parseDouble(line[2]));
	}


	public void draw(Graphics2D g, Location origin, double scale) {
		Point d = location.getPoint(origin, scale);
		g.setColor(Color.BLUE);
		g.drawOval(d.x, d.y, 1, 1);
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
}
