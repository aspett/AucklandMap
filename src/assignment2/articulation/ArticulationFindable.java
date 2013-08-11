package assignment2.articulation;

import java.util.Set;

public interface ArticulationFindable {
	public Set<ArticulationFindable> getArticulationNeighbours();
	public void initArticulationNeighbours();
	public void setDepth(int depth);
	public int getDepth();
	public void setArticulationPoint(boolean b);
}
