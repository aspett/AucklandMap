package mapgraph;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;

import main.Location;
import main.MapFrame;
import main.ViewingDimensions;
import indexstructures.*;

public class Graph {
	private Map<Integer, IntersectionNode> nodes;
	private Set<RoadSegment> edges;
	private Map<Integer, Road> roads;
	private MapFrame frame;
	private double maxX, maxY, minX, minY;
	
	private double zoom = 1.0;
	private double panX = 0;
	private double panY = 0;
	double scale;
	Location origin;
	
	public RoadTrie roadTrie = new RoadTrie();
	
	


	private static final String directory = "/u/students/pettandr/COMP261/Assignments/AucklandMap/small-data";

	public Graph() {
		nodes = new HashMap<Integer, IntersectionNode>();
		edges = new HashSet<RoadSegment>();
		roads = new HashMap<Integer, Road>();
		maxX = 0;
		maxY = 0;
		minX = 0;
		minY = 0;
	}
	public Graph(MapFrame frame) {
		this();
		this.frame = frame;
	}

	public void draw(Graphics g, Dimension dimensions) {
		scale = (dimensions.getWidth()/(maxX-minX))*this.zoom;
		origin = new Location(minX-panX, maxY-panY);

		Graphics2D g2d = (Graphics2D) g;
		g.setColor(new Color(255,0,0));
		g.drawString(String.format("Scale: %f panX: %f panY:%f", scale,panX,panY), (int) (dimensions.getWidth()-300), 50);
		g.setColor(Color.BLACK);
		
		
		for(RoadSegment s : edges) {
			s.draw(g2d, origin, scale);
		}
		
		for(IntersectionNode n : nodes.values()) {
			n.draw(g2d, origin, scale);
		}
		
	}

	public void loadStructures(String directory) {
		this.loadRoads(directory);
		this.loadSegments(directory);

		

		this.buildRanges();
	}

	public void buildRanges() {
		for(RoadSegment s : edges) {
			for(Location l : s.getCoords()) {
				if(l.x > maxX) maxX = l.x;
				if(l.x < minX) minX = l.x;
				if(l.y > maxY) maxY = l.y;
				if(l.y < minY) minY = l.y;
			}
		}
	}

	public void loadRoads(String directory) {
		String roadFilename = String.format("%s/roadID-roadInfo.tab", directory);
		File roadFile = new File(roadFilename);
		try {
			Scanner scanIn = new Scanner(roadFile);
			boolean firstLine = true;
			this.outputMessage("Loading roads..");
			while(scanIn.hasNextLine()) {
				if(firstLine) { firstLine = false; scanIn.nextLine(); continue; }
				String[] lineArray = scanIn.nextLine().split("\t");
				try {
					Road road = new Road(lineArray);
					roads.put(road.getId(), road);
					roadTrie.addNode(String.format("%s %s", road.getName(), road.getCity()), road);

				} catch(NumberFormatException e) {
					continue;
				}
			}
			this.outputMessage("Completed loading roads.");
		} catch (FileNotFoundException e) {

		}
	}

	public void loadSegments(String directory) {
		String filename = String.format("%s/roadSeg-roadID-length-nodeID-nodeID-coords.tab", directory);
		File file = new File(filename);
		try {
			Scanner scanIn = new Scanner(file);
			boolean firstLine = true;
			this.outputMessage("Loading road segments");
			while(scanIn.hasNextLine()) {
				if(firstLine) { firstLine = false; scanIn.nextLine(); continue; }
				String[] lineArray = scanIn.nextLine().split("\t");
				try {
					RoadSegment seg = new RoadSegment(lineArray);
					edges.add(seg);
					Road roadOfSegment = roads.get(seg.getRoadID());
					roadOfSegment.addSegment(seg);
				}catch(NumberFormatException e) {

				}
			}
			this.outputMessage("Completed loading road segments");
		} catch(FileNotFoundException e) {

		}
	}
	
	public void zoomIn() {
		this.zoom *= 1.10;
	}
	public void zoomOut() {
		this.zoom /= 1.10;
	}
	public ViewingDimensions getViewingDimensions() {
		return new ViewingDimensions(new Location(this.panX, this.panY), this.scale, this.zoom);
	}
	public void setViewingDimensions(ViewingDimensions o) {
		this.zoom = o.getZoom();
		this.scale = o.getScale();
		this.panX = o.getOrigin().x;
		this.panY = o.getOrigin().y;
	}
	
	private void outputMessage(String str) {
		if(frame != null) frame.setLoadingMessage(str);
		else System.out.println(str);
	}
}
