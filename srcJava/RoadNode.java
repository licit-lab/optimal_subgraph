import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RoadNode implements Comparable<RoadNode>{
	
	private long id;
	private double lon;
	private double lat;
	private ArrayList<Stop> stopsDeserved;
	
	public long getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	public RoadNode(long id, double lon, double lat) {
		super();
		this.id = id;
		this.lon = lon;
		this.lat = lat;
		this.stopsDeserved = new ArrayList<>();
	}
	
	@Override
	public int compareTo(RoadNode o) {
		RoadNode oNode = (RoadNode)o;
		return Long.compare(this.id, oNode.getId());
	}
	@Override
	public String toString() {
		return "RoadNode [id=" + id + ", lon=" + lon + ", lat=" + lat + "]";
	}
	public void addStop(Stop s) {
		this.stopsDeserved.add(s);
	}
	
	public void uniqueStop() {
		 HashSet<Stop> set = new HashSet<>() ;
	     set.addAll(this.stopsDeserved) ;
	     this.stopsDeserved = new ArrayList<Stop>(set) ;
	}
	
	public String displayStops() {
		String res = "[";
		for (Stop s : stopsDeserved) {
			res += s.getIndex() + " ";
		}
		res += "]";
		return res;
	}
	
	public int nbStopDeserves() {
		return stopsDeserved.size();
	}
	
	public ArrayList<Stop> getStopDeserved() {
		return this.stopsDeserved;
	}
}
