
public class BusSequence implements Comparable<BusSequence>{
	private int sequence;
	private Route route;
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public Route getRoute() {
		return route;
	}
	public void setRouteId(Route route) {
		this.route = route;
	}
	public BusSequence(int sequence, Route route) {
		super();
		this.sequence = sequence;
		this.route = route;
	}
	@Override
	public String toString() {
		return "BusSequence [sequence=" + sequence + ", routeId=" + route + "]";
	}
	@Override
	public int compareTo(BusSequence o) {
		return Integer.compare(this.sequence, o.sequence);
	}
}
