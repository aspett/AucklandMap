package assignment2.unionfind;

public interface UnionFindable {
	public UnionFindable getKruskParent();
	public void setKruskParent(UnionFindable parent);
	public int getKruskRank();
	public void setKruskRank(int rank);
	public void resetUnionFind();
	public UnionFindable findKruskParent();
}
