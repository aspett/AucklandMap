package indexstructures.quadtree;

public class MapBoundingBox {
	public double x;
	public double y;
	public double width;
	public double height;
	
	public MapBoundingBox(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public boolean contains(double x, double y) {
		if(x >= this.x && x <= (this.x+this.width)) {
			//System.out.println(1);
			if(y <= this.y && y >= (this.y+this.height)) {
				//System.out.println(2);
				return true;
			}
		}
		//System.out.println(3);
		return false;

	}

}
