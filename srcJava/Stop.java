import java.util.ArrayList;

public class Stop implements Comparable{
	private int id;
	private boolean open;
	private int index;
	private String name;
	private double lon;
	private double lat;
	private RoadNode closestRoadNode;
	private ArrayList<BusSequence> lines;
	private double distToClosestNode;
	private IrisZone iris;
	private int degree;
	private double area;
	public int getId() {
		return id;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}


	public void setIndex(int index) {
		this.index = index;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public RoadNode getClosestRoadNode() {
		return closestRoadNode;
	}
	public void setClosestRoadNode(RoadNode closestRoadNode) {
		this.closestRoadNode = closestRoadNode;
	}
	public ArrayList<BusSequence> getLines() {
		return lines;
	}
	public void setLines(ArrayList<BusSequence> lines) {
		this.lines = lines;
	}
	public double getDistToClosestNode() {
		return distToClosestNode;
	}
	public void setDistToClosestNode(double distToClosestNode) {
		this.distToClosestNode = distToClosestNode;
	}
	public IrisZone getIris() {
		return iris;
	}
	public void setIris(IrisZone iris) {
		this.iris = iris;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public Stop(int id, int index, String name, double lon, double lat, RoadNode closestRoadNode, double distToClosestNode,
			IrisZone iris, int degree, double area) {
		super();
		this.id = id;
		this.open = false;
		this.index = index;
		this.name = name;
		this.lon = lon;
		this.lat = lat;
		this.lines = new ArrayList<>();
		this.closestRoadNode = closestRoadNode;
		this.distToClosestNode = distToClosestNode;
		this.iris = iris;
		this.degree = degree;
		this.area = area;
	}
	
	
	public void addBusSequence(BusSequence bS) {
		this.lines.add(bS);
	}
	@Override
	public int compareTo(Object o) {
		Stop stop = (Stop)o;
		return Integer.compare(this.id, stop.id);
	}
	
	
	@Override
	public String toString() {
		String res =  "Stop [id=" + id + ", name=" + name + ", lon=" + lon + ", lat=" + lat + ", closestRoadNode="
				+ closestRoadNode + ", distToClosestNode=" + distToClosestNode + ", iris=" + iris
				+ ", degree=" + degree + ", area=" + area + "]\n";
		for (BusSequence bS : this.lines) {
			res = res + "\t" + bS.toString() + "\n";
		}
		return res;
	}
}
