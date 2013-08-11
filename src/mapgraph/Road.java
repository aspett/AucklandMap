package mapgraph;
import java.awt.Graphics2D;
import java.util.*;

import main.Location;

public class Road implements MapDrawable {

	private final Integer id;
	private final Integer type;
	private final String name;
	private final String city;
	private final Integer oneway;
	private final Integer speed;
	private final Integer roadclass;
	private final Integer notforcar;
	private final Integer notforpede;
	private final Integer notforbicy;
	private List<RoadSegment> segments;
	public static Road selectedRoad = null;
	public Road(String[] line) throws NumberFormatException {
		id = Integer.parseInt(line[0]);
		type = Integer.parseInt(line[1]);
		name = line[2];
		city = line[3];
		oneway = Integer.parseInt(line[4]);
		speed = Integer.parseInt(line[5]);
		roadclass = Integer.parseInt(line[6]);
		notforcar = Integer.parseInt(line[7]);
		notforpede = Integer.parseInt(line[8]);
		notforbicy = Integer.parseInt(line[9]);
		segments = new ArrayList<RoadSegment>();
	}

	public String toString() {
		return name;
	}

	public Integer getId() {
		return id;
	}

	public Integer getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public Integer getOneway() {
		return oneway;
	}

	public Integer getSpeed() {
		return speed;
	}

	public Integer getRoadclass() {
		return roadclass;
	}

	public Integer getNotforcar() {
		return notforcar;
	}

	public Integer getNotforpede() {
		return notforpede;
	}

	public Integer getNotforbicy() {
		return notforbicy;
	}

	public void addSegment(RoadSegment seg) {
		segments.add(seg);
	}

	public List<RoadSegment> getSegments() {
		return Collections.unmodifiableList(segments);
	}

	public void setSelected(boolean sel) {
		if(selectedRoad != null) for(RoadSegment s : selectedRoad.getSegments()) s.setSelected(false);
		this.selectedRoad = this;
		for(RoadSegment s : getSegments()) s.setSelected(sel);
	}
	public boolean isSelected() {
		return selectedRoad == this;
	}
	
	public double getLength() {
		double r = 0.0;
		for(RoadSegment s : getSegments()) {
			r += s.getLength();
		}
		return r;
	}

	@Override
	public void draw(Graphics2D g, Location origin, double scale, double zoom) {
		for(RoadSegment s : getSegments()) {
			s.draw(g,origin,scale, 1.0);
		}
	}
	
	public boolean drawableRoad() {
		if((this.type > 0 && this.type < 13) || this.type == 22 || this.type == 26) return true;
		return false;
	}
	
	public int getRealSpeed() {
		switch(getSpeed()) {
		case 0: { return 5;  }
		case 1: { return 20; }
		case 2: { return 40; }
		case 3: { return 60; }
		case 4: { return 80; }
		case 5: { return 90; }
		case 6: { return 110;}
		default: { return 40; }
		}
	}

	public float getRoadclassModifier() {
		switch(getRoadclass()) {
		case 0: return 0.87f;
		case 1: return 0.90f;
		case 2: return 0.93f;
		case 3: return 0.97f;
		case 4: return 1.0f;
		default: return 0.87f;
		}
	}
	
	public float roadSpeedWithClass() {
		return getRealSpeed() * getRoadclassModifier();
	}
}
