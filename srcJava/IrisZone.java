
public class IrisZone {
	private int id;
	private int depCom;
	private String nomCom;
	private int iris;
	private int dComiris;
	private String nomIris;
	private String typeIris;
	private double area;
	private double lon2154;
	private double lat2154;
	private double lon;
	private double lat;
	private Polygon poly;
	
	public Polygon getPoly() {
		return poly;
	}
	public void setPoly(Polygon poly) {
		this.poly = poly;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDepCom() {
		return depCom;
	}
	public void setDepCom(int depCom) {
		this.depCom = depCom;
	}
	public String getNomCom() {
		return nomCom;
	}
	public void setNomCom(String nomCom) {
		this.nomCom = nomCom;
	}
	public int getIris() {
		return iris;
	}
	public void setIris(int iris) {
		this.iris = iris;
	}
	public int getdComiris() {
		return dComiris;
	}
	
	public void setdComiris(int dComiris) {
		this.dComiris = dComiris;
	}
	public String getNomIris() {
		return nomIris;
	}
	public void setNomIris(String nomIris) {
		this.nomIris = nomIris;
	}
	public String getTypeIris() {
		return typeIris;
	}
	public void setTypeIris(String typeIris) {
		this.typeIris = typeIris;
	}
	public double getArea() {
		return area;
	}
	public void setArea(double area) {
		this.area = area;
	}
	public double getLon2154() {
		return lon2154;
	}
	public void setLon2154(double lon2154) {
		this.lon2154 = lon2154;
	}
	public double getLat2154() {
		return lat2154;
	}
	public void setLat2154(double lat2154) {
		this.lat2154 = lat2154;
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
	public IrisZone(int id, int depCom, String nomCom, int iris, int dComiris, String nomIris, String typeIris,
			double area, double lon2154, double lat2154, double lon, double lat, Polygon poly) {
		super();
		this.id = id;
		this.depCom = depCom;
		this.nomCom = nomCom;
		this.iris = iris;
		this.dComiris = dComiris;
		this.nomIris = nomIris;
		this.typeIris = typeIris;
		this.area = area;
		this.lon2154 = lon2154;
		this.lat2154 = lat2154;
		this.lon = lon;
		this.lat = lat;
		this.poly = poly;
	}
	
	@Override
	public String toString() {
		return "IrisZone [id=" + id + ", depCom=" + depCom + ", nomCom=" + nomCom + ", iris=" + iris + ", dComiris="
				+ dComiris + ", nomIris=" + nomIris + ", typeIris=" + typeIris + ", area=" + area + ", lon2154="
				+ lon2154 + ", lat2154=" + lat2154 + ", lon=" + lon + ", lat=" + lat + "]";
	}
	
}
