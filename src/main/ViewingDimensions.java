package main;

public class ViewingDimensions {
	private Location origin;
	private Location pan;
	private double scale;
	private double zoom;
	
	public ViewingDimensions(Location origin, Location pan, double scale, double zoom) {
		super();
		this.pan = pan;
		this.scale = scale;
		this.zoom = zoom;
		this.origin = origin;
	}
	public Location getOrigin() {
		return origin;
	}
	public Location getPan() {
		return pan;
	}

	public void setPan(Location pan) {
		this.pan = pan;
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
	
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	

}
