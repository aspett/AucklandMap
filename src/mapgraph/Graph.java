package mapgraph;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;

import javax.swing.JFrame;

import assignment2.PathFinder;

import main.*;
import main.autosuggester.*;
import indexstructures.quadtree.MapBoundingBox;
import indexstructures.quadtree.QuadTree;
import indexstructures.roadtrie.*;

public class Graph {
	private Map<Integer, IntersectionNode> nodes;
	private Set<RoadSegment> edges;
	private Map<Integer, Road> roads;
	private Map<String, RoadGroup> roadGroups;
	private MapFrame frame;
	private AucklandMap owner;
	public QuadTree intersectionQuad;
	private double maxX, maxY, minX, minY;
	
	private PathFinder path;

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
		roadGroups = new HashMap<String, RoadGroup>();

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

		//DEBUG
		/*g.setColor(new Color(255,0,0));
		g.drawString(String.format("Scale: %f panX: %f panY:%f", scale,panX,panY), (int) (dimensions.getWidth()-300), 50);
		g.setColor(Color.BLACK);*/

		//if(intersectionQuad != null) intersectionQuad.draw(g2d, origin, scale);

		for(IntersectionNode n : nodes.values()) {
			n.draw(g2d, origin, scale);
		}
		for(RoadSegment s : edges) {
			if(s.getParentRoad().drawableRoad())
				s.draw(g2d, origin, scale);
		}
		if(RoadGroup.getSelected() != null)
			RoadGroup.getSelected().draw(g2d, origin, scale);

		if(path != null) {
			Stroke str = g2d.getStroke();
			Color col = g2d.getColor();
			g2d.setStroke(new BasicStroke(3.0f));
			g2d.setColor(Color.CYAN);
			path.draw(g2d, origin, scale);
			g2d.setStroke(str);
			g2d.setColor(col);
		}




	}

	public void loadStructures(String directory) {
		boolean cont = true;
		if(cont && !this.loadRoads(directory)) cont = false;
		if(cont && !this.loadIntersections(directory)) cont = false;
		if(cont && !this.loadSegments(directory)) cont = false;

		if(cont) {
			this.buildRanges();
			this.buildQuadTree();
		}
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
		for(IntersectionNode n : nodes.values()) {
			Location l = n.getLocation();
			if(l.x > maxX) maxX = l.x;
			if(l.x < minX) minX = l.x;
			if(l.y > maxY) maxY = l.y;
			if(l.y < minY) minY = l.y;
		}
	}

	public void buildQuadTree() {
		System.out.println("Building Quad Tree....");
		intersectionQuad = new QuadTree(new MapBoundingBox(minX, maxY, maxX-minX, minY-maxY));
		for(IntersectionNode n : nodes.values()) {
			intersectionQuad.add(n);
		}
	}

	public boolean loadRoads(String directory) {
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
					
					String groupName = String.format("%s %s", road.getName(), road.getCity());
					RoadGroup group = null;
					if(!roadGroups.containsKey(groupName)) {
						group = new RoadGroup(road.getName(), road.getCity());
						roadGroups.put(groupName, group);
					}
					else {
						group = roadGroups.get(groupName);
					}
					group.addRoad(road);
					roadTrie.addNode(groupName, group);

				} catch(NumberFormatException e) {
					continue;
				}
			}
			this.outputMessage("Completed loading roads.");
		} catch (FileNotFoundException e) {
			outputMessage("File path does not exist. Please open a new directory from the File menu.");
			return false;
		}
		return true;
	}

	public boolean loadIntersections(String directory) {
		String filename = String.format("%s/nodeID-lat-lon.tab", directory);
		File file = new File(filename);
		try {
			Scanner scanIn = new Scanner(file);
			this.outputMessage("Loading intersections");
			while(scanIn.hasNextLine()) {
				String[] lineArray = scanIn.nextLine().split("\t");
				try {
					IntersectionNode node = new IntersectionNode(lineArray);
					nodes.put(node.getID(), node);

				}catch(NumberFormatException e) {

				}
			}
			this.outputMessage("Completed loading road segments");
		} catch(FileNotFoundException e) {
			outputMessage("File path does not exist. Please open a new directory from the File menu.");
			return false;
		}
		return true;
	}

	public boolean loadSegments(String directory) {
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
					RoadSegment seg = new RoadSegment(lineArray, this);
					edges.add(seg);
					Road roadOfSegment = roads.get(seg.getRoadID());
					roadOfSegment.addSegment(seg);
					seg.setParentRoad(roadOfSegment);
					seg.setOneWay(roadOfSegment.getOneway());
				}catch(NumberFormatException e) {

				}
			}
			this.outputMessage("Completed loading intersections");
		} catch(FileNotFoundException e) {
			outputMessage("File path does not exist. Please open a new directory from the File menu.");
			return false;
		}
		return true;
	}




	public void zoomIn() {
		this.zoom *= 1.10;
	}
	public void zoomOut() {
		this.zoom /= 1.10;
	}
	public ViewingDimensions getViewingDimensions() {
		return new ViewingDimensions(origin, new Location(this.panX, this.panY), this.scale, this.zoom);
	}
	public void setViewingDimensions(ViewingDimensions o) {
		this.zoom = o.getZoom();
		this.scale = o.getScale();
		this.panX = o.getPan().x;
		this.panY = o.getPan().y;
	}

	private void outputMessage(String str) {
		if(this.owner != null) this.owner.outputText(str);
		System.out.println(str);
	}

	public IntersectionNode getNodeByID(Integer id) {
		return nodes.get(id);
	}

	public void selectRoad(String roadName) {
		RoadGroup r = roadTrie.getRoad(roadName);
		RoadGroup.setSelected(r);
	}
	public void selectRoad(Road road) {
		road.setSelected(true);
	}
	
	public void attachAutoSuggestor(AutoSuggestionTextField f) {
		f.setAutoSuggestor(new RoadAutoSuggestor(roadTrie));
	}
	
	public void setOwner(AucklandMap map) {
		this.owner = map;
	}
	
	public Map<Integer, IntersectionNode> getNodes() {
		return nodes;
	}
	
	public void setPath(PathFinder pf) {
		this.path = pf;
	}
}
