
public class Zone implements Comparable<Zone>{
	private int dcomiris;
	private int index;
	private int id;
	public int getDcomiris() {
		return dcomiris;
	}
	public void setDcomiris(int dcomiris) {
		this.dcomiris = dcomiris;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Zone [dcomiris=" + dcomiris + ", index=" + index + ", id=" + id + "]";
	}
	public Zone(int dcomiris, int index, int id) {
		super();
		this.dcomiris = dcomiris;
		this.index = index;
		this.id = id;
	}
	@Override
	public int compareTo(Zone o) {
		String lexicoO = o.getDcomiris() + "_" + o.getIndex();
		String lexicoThis = this.getDcomiris() + "_" + this.getIndex();
		return lexicoThis.compareTo(lexicoO);
	}
	
}
