package assignment2;

import main.Location;
import mapgraph.*;
import java.util.*;

public interface PathFindable {
	public boolean isVisited();
	public void setVisited(boolean visited);
	public Location getLocation();
	public Map<PathFindable, RoadSegment> getEdgesOut();
	public Map<PathFindable, RoadSegment> getEdgesIn();
	public void setPathFrom(PathFindable from);
	public PathFindable getPathFrom();
	public double getPathCost();
	public void setPathCost(double cost);
}
