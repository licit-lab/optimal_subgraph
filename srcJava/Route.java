
public class Route {
	public String id;
	public String shortName;
	public String LongName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getLongName() {
		return LongName;
	}
	public void setLongName(String longName) {
		LongName = longName;
	}
	public Route(String id, String shortName, String longName) {
		super();
		this.id = id;
		this.shortName = shortName;
		LongName = longName;
	}
	@Override
	public String toString() {
		return "Route [id=" + id + ", shortName=" + shortName + ", LongName=" + LongName + "]";
	}
	@Override
	public boolean equals(Object obj) {
		Route objRoute = (Route)obj;
		return (this.getId().equals(objRoute.getId()));
	}
}
