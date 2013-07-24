package mapgraph;

import java.io.*;
import java.util.*;
import indexstructures.*;

public class Graph {
	private Map<Integer, IntersectionNode> nodes;
	private Set<RoadSegment> edges;
	private Map<Integer, Road> roads;
	public RoadTrie roadTrie = new RoadTrie();


	private static final String directory = "/u/students/pettandr/COMP261/Assignments/AucklandMap/small-data";

	public Graph() {
		nodes = new HashMap<Integer, IntersectionNode>();
		edges = new HashSet<RoadSegment>();
		roads = new HashMap<Integer, Road>();
	}

	public void loadStructures(String directory) {
		this.loadRoads(directory);
	}

	public void loadRoads(String directory) {
		//DEBUG System.out.println(directory);
		String roadFilename = String.format("%s/roadID-roadInfo.tab", this.directory);
		File roadFile = new File(roadFilename);
		try {
			Scanner scanIn = new Scanner(roadFile);
			boolean firstLine = true;
			System.out.println("Loading roads..");
			while(scanIn.hasNextLine()) {

				//if(firstLine) { firstLine = false; scanIn.nextLine(); continue; }
				//String lineArray = scanIn.nextLine();
				String[] lineArray = scanIn.nextLine().split("\t");

				try {
					Road road = new Road(lineArray);
					roads.put(road.getId(), road);
					roadTrie.addNode(String.format("%s %s", road.getName(), road.getCity()), road);

				} catch(NumberFormatException e) {
					continue;
				}



				/*DEBUGfor(Map.Entry<Integer, Road> e : roads.entrySet()) {
					System.out.printf("%s\n", e.getValue().getName());
				}*/
			}
			System.out.println("Completed loading roads.");
		} catch (FileNotFoundException e) {

		}

	}
}
