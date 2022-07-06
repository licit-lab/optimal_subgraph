import java.util.ArrayList;

public class Polygon {
	private ArrayList<Point> vertices;
	private Point centroid;
	private int nbPointsWanted = 10 ;
	private ArrayList<Point> interiorPoints;

	public Polygon() {
		super();
		this.vertices = new ArrayList<>();
		this.centroid = null;
	}

	public ArrayList<Point> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Point> vertices) {
		this.vertices = vertices;
	}

	public Point getCentroid() {
		return centroid;
	}

	public void setCentroid(Point centroid) {
		this.centroid = centroid;
	}

	public void addPoint(Point p) {
		this.vertices.add(p);
	}

	public ArrayList<Point> getInteriorPoints() {
		int nbPoints = 0;
		interiorPoints = new ArrayList<>();
		for (Point p : this.vertices) {
			if (nbPoints % nbPointsWanted  == 0) {
				double iX = (centroid.getX() + p.getX()) / 2.0;
				double iY = (centroid.getY() + p.getY()) / 2.0;
				Point i = new Point(iX, iY);
				interiorPoints.add(i);
			}
			nbPoints++;
		}
		interiorPoints.add(centroid);
		return interiorPoints;
	}
	
	public int getNbInteriorPoints() {
		return this.interiorPoints.size();
	}
}
