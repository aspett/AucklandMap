package indexstructures.quadtree;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import mapgraph.*;
import main.*;

public class QuadTree {

	private QuadTree northWest;
	private QuadTree northEast;
	private QuadTree southWest;
	private QuadTree southEast;

	private Set<IntersectionNode> points;

	private Rectangle2D.Double bounds;
	private static final int MAX_ELEMENTS = 4;

	public QuadTree(Rectangle2D.Double bounds) {
		this.bounds = bounds;
		this.points = new HashSet<IntersectionNode>();
		System.out.printf("Made a quadtree with bounds [x=%f,y=%f,w=%f,h=%f]\n", bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public boolean add(IntersectionNode intersection) {
		Location loc = intersection.getLocation();
		if(!bounds.contains(new Point2D.Double(loc.x, loc.y)))
			return false;

		if(points.size() < MAX_ELEMENTS) {
			points.add(intersection);
			return true;
		}

		if(northWest == null)
			subDivide();

		if(northWest.add(intersection)) return true;
		if(northEast.add(intersection)) return true;
		if(southWest.add(intersection)) return true;
		if(southEast.add(intersection)) return true;

		return false;
	}

	public IntersectionNode find(Location loc) {
		Point2D point = new Point2D.Double(loc.x, loc.y);
		if(!bounds.contains(point))
			return null;

		if(northWest != null) { //Search the children..
			if(northWest.getBounds().contains(point)) return northWest.find(loc);
			if(northEast.getBounds().contains(point)) return northEast.find(loc);
			if(southWest.getBounds().contains(point)) return southWest.find(loc);
			if(southEast.getBounds().contains(point)) return southEast.find(loc);
			return null; //It's not in any quadrant? It should be!
		}
		else { //Must be in here..
			IntersectionNode closest = null;
			double closestDist = 0.0;
			for(IntersectionNode n : points) {
				Location intLocation = n.getLocation();
				double distance = loc.distanceTo(intLocation);
				if(distance < closestDist || closest == null) {
					closest = n;
					closestDist = distance;
				}
			}
			return closest;
		}
	}

	public Rectangle2D.Double getBounds() { return this.bounds; }

	protected void subDivide() {
		System.out.println("Subdividing..");
		northWest = new QuadTree(new Rectangle2D.Double(this.bounds.x, this.bounds.y, this.bounds.width/2, this.bounds.height/2));
		northEast = new QuadTree(new Rectangle2D.Double(this.bounds.width/2, this.bounds.y, this.bounds.width/2, this.bounds.height/2));

		southWest = new QuadTree(new Rectangle2D.Double(this.bounds.x, this.bounds.height/2, this.bounds.width/2, this.bounds.height/2));
		southEast = new QuadTree(new Rectangle2D.Double(this.bounds.width/2, this.bounds.height/2, this.bounds.width/2, this.bounds.height/2));
	}

}
