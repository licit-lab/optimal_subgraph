
public class Node implements Comparable<Node>{
	private int id;
	private int geocenter;
	
	public int degree;
	
	public void setDegree(int degree) {
		this.degree = degree;
	}
	public void setGeocenter(int geocenter) {
		this.geocenter = geocenter;
	}
	private double x;
	private double y;
	private IrisZone iris;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public IrisZone getIris() {
		return iris;
	}
	public void setIris(IrisZone iris) {
		this.iris = iris;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getGeocenter() {
		return geocenter;
	}
	public int getDegree() {
		return degree;
	}
	public Node(int id, int geocenter, double x, double y, int degree, int dcomiris, IrisZone[] nodesU) {
		super();
		this.id = id;
		this.geocenter = geocenter;
		this.x = x;
		this.y = y;
		this.degree = degree;
		for (IrisZone ir : nodesU) {
			if (ir.getdComiris() == dcomiris) {
				this.iris = ir;
			}
		}
	}
	@Override
	public String toString() {
		return "Node [id=" + id + ", geocenter=" + geocenter + ", x=" + x + ", y=" + y + "]" + " degree = " + this.degree ;
	}
	@Override
	public int compareTo(Node o) {
		Node oNode = (Node)o;
		return Integer.compare(this.id, oNode.getId());
	}
	
}
