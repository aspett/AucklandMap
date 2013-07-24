package mapgraph;

public class Road {
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
}
