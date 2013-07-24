package mapgraph;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.*;
import java.util.*;

import main.Location;
import indexstructures.*;

public class Graph {
	private Map<Integer, IntersectionNode> nodes;
	private Set<RoadSegment> edges;
	private Map<Integer, Road> roads;
	public RoadTrie roadTrie = new RoadTrie();
	private double maxX, maxY, minX, minY;


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

	public void draw(Graphics g, Dimension dimensions) {
		double scale = dimensions.getWidth()/(maxX-minX);
		Location origin = new Location(minX, maxY);

		Graphics2D g2d = (Graphics2D) g;
		Path2D.Double path = new Path2D.Double();

		for(RoadSegment s : edges) {
			boolean first = true;
			for(Location l : s.getCoords()) {
				if(first) {
					first = false;
					//path.moveTo()
					//TODO use .getPoint on Location
				}
			}
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
			System.out.println("Loading roads..");
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
			System.out.println("Completed loading roads.");
		} catch (FileNotFoundException e) {

		}
	}

	public void loadSegments(String directory) {
		String filename = String.format("%s/roadSeg-roadID-length-nodeID-nodeID-coords.tab", directory);
		File file = new File(filename);
		try {
			Scanner scanIn = new Scanner(file);
			boolean firstLine = true;
			System.out.println("Loading road segments");
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
			System.out.println("Completed loading road segments");
		} catch(FileNotFoundException e) {

		}
	}
}
