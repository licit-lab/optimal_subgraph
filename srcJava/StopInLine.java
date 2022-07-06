public class StopInLine implements Comparable<StopInLine>{
	private Stop stop;
	private int order;
	@Override
	public int compareTo(StopInLine o) {
		return Integer.compare(this.order, o.order);
	}
	public StopInLine(Stop stop, int order) {
		super();
		this.stop = stop;
		this.order = order;
	}
	public Stop getStop() {
		return stop;
	}
	public void setStop(Stop stop) {
		this.stop = stop;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	public String toString() {
		String res = "[Stop:" + this.stop.getId() + " " + this.stop.getName() + ", " + this.order + "]";
		return res;
		
	}
}
