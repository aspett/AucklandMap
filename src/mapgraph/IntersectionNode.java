package mapgraph;
import java.awt.Graphics2D;
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
	@Override
	public void draw(Graphics2D g, Location origin, double scale) {
				
	}
}
