package indexstructures.quadtree;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

import mapgraph.*;
import main.*;

public class QuadTree implements MapDrawable{

	private QuadTree northWest;
	private QuadTree northEast;
	private QuadTree southWest;
	private QuadTree southEast;
	
	private static boolean k = false;

	private Set<IntersectionNode> points;

	private MapBoundingBox bounds;
	private static final int MAX_ELEMENTS = 4;

	public QuadTree(MapBoundingBox bounds) {
		this.bounds = bounds;
		this.points = new HashSet<IntersectionNode>();
		System.out.printf("Made a quadtree with bounds [x=%f,y=%f,w=%f,h=%f]\n", bounds.x, bounds.y, bounds.width, bounds.height);
	}

	public boolean add(IntersectionNode intersection) {
		Location loc = intersection.getLocation();
		if(!bounds.contains(loc.x, loc.y)) {
			//System.out.printf("Bounds = [x=%f,y=%f,w=%f,h=%f], Location: %s\n", bounds.x, bounds.y, bounds.width, bounds.height, loc);
			return false;
		}

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
		System.out.printf("Searching bounds [x=%f,y=%f,w=%f,h=%f]\n", bounds.x, bounds.y, bounds.width, bounds.height);
		//Point2D point = new Point2D.Double(loc.x, loc.y);
		if(!bounds.contains(loc.x,loc.y))
			return null;

		if(northWest != null) { //Search the children..
			System.out.println("Searching children..");
			if(northWest.getBounds().contains(loc.x,loc.y)) return northWest.find(loc);
			if(northEast.getBounds().contains(loc.x,loc.y)) return northEast.find(loc);
			if(southWest.getBounds().contains(loc.x,loc.y)) return southWest.find(loc);
			if(southEast.getBounds().contains(loc.x,loc.y)) return southEast.find(loc);
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
	public HashSet<IntersectionNode> findall(Location loc) {
		System.out.printf("Searching bounds [x=%f,y=%f,w=%f,h=%f]\n", bounds.x, bounds.y, bounds.width, bounds.height);
		//Point2D point = new Point2D.Double(loc.x, loc.y);
		if(!bounds.contains(loc.x,loc.y))
			return null;

		if(northWest != null) { //Search the children..
			System.out.println("Searching children..");
			if(northWest.getBounds().contains(loc.x,loc.y)) return northWest.findall(loc);
			if(northEast.getBounds().contains(loc.x,loc.y)) return northEast.findall(loc);
			if(southWest.getBounds().contains(loc.x,loc.y)) return southWest.findall(loc);
			if(southEast.getBounds().contains(loc.x,loc.y)) return southEast.findall(loc);
			return null; //It's not in any quadrant? It should be!
		}
		else { //Must be in here..
			return (HashSet<IntersectionNode>) points;
		}
	}

	public MapBoundingBox getBounds() { return this.bounds; }

	protected void subDivide() {
		System.out.println("Subdividing..");
		if(!k) {
			//k = true;
			northWest = new QuadTree(new MapBoundingBox(this.bounds.x, this.bounds.y, this.bounds.width/2, this.bounds.height/2));
			northEast = new QuadTree(new MapBoundingBox(this.bounds.x + this.bounds.width/2, this.bounds.y, this.bounds.width/2, this.bounds.height/2));
	
			southWest = new QuadTree(new MapBoundingBox(this.bounds.x, this.bounds.y + this.bounds.height/2, this.bounds.width/2, this.bounds.height/2));
			southEast = new QuadTree(new MapBoundingBox(this.bounds.x + this.bounds.width/2, this.bounds.y + this.bounds.height/2, this.bounds.width/2, this.bounds.height/2));
			for(IntersectionNode n : points) this.add(n);
			//points.clear();
		}
	}

	@Override
	public void draw(Graphics2D g, Location origin, double scale) {
		g.setColor(Color.GRAY);
		Location topLeft = new Location(bounds.x, bounds.y);
		Location bottomRight = new Location(topLeft.x + bounds.width, topLeft.y + bounds.height);
		Point tl = topLeft.getPoint(origin,scale);
		Point br = bottomRight.getPoint(origin, scale);
		g.drawRect(tl.x, tl.y, br.x-tl.x, br.y-tl.y);
		g.setColor(Color.BLACK);
		if(northWest != null) {
			northWest.draw(g, origin, scale);
			northEast.draw(g, origin, scale);
			southWest.draw(g, origin, scale);
			southEast.draw(g, origin, scale);
		}
	}

}
