package mapgraph;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import main.Location;

public class RoadGroup implements MapDrawable{
	private String name;
	private String city;
	
	private static RoadGroup selected;
	
	private List<Road> roads;
	
	public RoadGroup(String name, String city) {
		this.name = name;
		this.city = city;
		this.roads = new ArrayList<Road>();
		
	}
	
	public boolean addRoad(Road r) {
		return roads.add(r);
	}
	
	
	
	
	
	
	
	@Override
	public void draw(Graphics2D g, Location origin, double scale, double zoom) {
		//TODO debug System.out.printf("Draw %s %s\n", this.name, this.city);
		if(selected == this) {
			Stroke str = g.getStroke();
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(3.0f));
			for(Road r : roads) {
				r.draw(g, origin, scale, 1.0);
			}
			g.setColor(c);
			g.setStroke(str);
		}
		
	}

	public static void setSelected(RoadGroup g) {
		selected = g;
	}

	public String getName() {
		return name;
	}
	
	public String getCity() {
		return city;
	}
	
	public double getLength() {
		double r = 0;
		for(Road road : roads) {
			r += road.getLength();
		}
		return r;
	}
	
	public Set<RoadSegment> getSegments() {
		Set<RoadSegment> segs = new HashSet<RoadSegment>();
		for(Road r : roads) {
			segs.addAll(r.getSegments());
		}
		return segs;
	}
	
	public static RoadGroup getSelected() {
		return selected;
	}

}
