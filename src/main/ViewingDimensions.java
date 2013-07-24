package main;

public class ViewingDimensions {
	private Location origin;
	private double scale;
	private double zoom;
	
	public ViewingDimensions(Location origin, double scale, double zoom) {
		super();
		this.origin = origin;
		this.scale = scale;
		this.zoom = zoom;
	}

	public Location getOrigin() {
		return origin;
	}

	public void setOrigin(Location origin) {
		this.origin = origin;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public double getZoom() {
		return this.zoom;
	}
	

}
