import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class NewInstance {
	private int nT;
	private int nU;
	private int nR;
	private IrisZone[] iriZones;
	private RoadNode[] roads;
	private Stop[] stops;
	private ArrayList<Zone> zones;
	private double mT;
	private double mTD;
	private double mU;
	private double alpha;
	private double pElim;
	private double tHDemand;
	private double pourcDegree = 0.05;
	private double speedBus = 170.0 / 36.0;
	private double speedFoot = 30.0 / 36.0;
	private double pourcOpenPerIris = 0.5;
	private ArrayList<Route> routes;
	private int nUD;
	private double dD[][];

	private int[] degrees;
	private double[][] d;
	private double[][] pC;
	private int[][] oD;
	private int[][] oDD;
	private double[] dAccess;
	private double[] dAccessD;
	private double[] k;
	private double[] kD;
	private int[] acc;
	private int[] accD;
	private int[] cantBeClosed;
	private int[] cantBeClosedD;
	private int[][] isIn;
	private double doubleBigEnough = 2000.0;
	private int ligneMin = 10;
	private double amplificationOD = 3000.0;

	public int[] getCantBeClosed() {
		return cantBeClosed;
	}

	public void setCantBeClosed(int[] canBeClosed) {
		this.cantBeClosed = canBeClosed;
	}

	public int getnT() {
		return nT;
	}

	public void setnT(int nT) {
		this.nT = nT;
	}

	public int getnU() {
		return nU;
	}

	public void setnU(int nU) {
		this.nU = nU;
	}

	public double getmT() {
		return mT;
	}

	public void setmT(double mT) {
		this.mT = mT;
	}

	public int[] getDegrees() {
		return degrees;
	}

	public void setDegrees(int[] degrees) {
		this.degrees = degrees;
	}

	public double getmU() {
		return mU;
	}

	public void setmU(double mU) {
		this.mU = mU;
	}

	public double getAlpha() {
		return alpha;
	}

	public void settMax(double alpha) {
		this.alpha = alpha;
	}

	public double getpElim() {
		return pElim;
	}

	public void setpElim(double pElim) {
		this.pElim = pElim;
	}

	public double[][] getD() {
		return d;
	}

	public void setD(double[][] d) {
		this.d = d;
	}

	public double[][] getpC() {
		return pC;
	}

	public void setpC(double[][] pC) {
		this.pC = pC;
	}

	public int[][] getoD() {
		return oD;
	}

	public void setoD(int[][] oD) {
		this.oD = oD;
	}

	public double[] getdAccess() {
		return dAccess;
	}

	public void setdAccess(double[] dAccess) {
		this.dAccess = dAccess;
	}

	public double[] getK() {
		return k;
	}

	public void setK(double[] k) {
		this.k = k;
	}

	public int[] getAcc() {
		return acc;
	}

	public void setAcc(int[] acc) {
		this.acc = acc;
	}

	public NewInstance(String pathToInstance, double alpha, double pElim, double tHDemand, double pourcDegree)
			throws IOException {
		this.alpha = alpha;
		this.pElim = pElim;
		this.tHDemand = tHDemand;

		// On récupère la taille des données
		Scanner sc = new Scanner(new File(pathToInstance + "size.csv"));
		sc.useDelimiter("\n");
		sc.next();
		String[] line = sc.next().split(";");
		this.nT = Integer.parseInt(line[0]);
		this.nU = Integer.parseInt(line[1]);
		this.nR = Integer.parseInt(line[2]);

		System.out.println(nT + " " + nU + " " + nR);

		// On alloue les tableaux
		this.d = new double[nU][nT];
		this.pC = new double[nT][nT];
		this.oD = new int[nU][nU];
		this.dAccess = new double[nU];
		this.stops = new Stop[nT];
		this.roads = new RoadNode[nR];
		this.iriZones = new IrisZone[nU];
		this.k = new double[nU];
		this.acc = new int[nU];
		this.cantBeClosed = new int[nT];
		this.degrees = new int[nT];
		this.isIn = new int[nU][nT];

		// On parse les zones iris
		sc = new Scanner(new File(pathToInstance + "iris_data_lyon_original.csv"));
		sc.useDelimiter("\n");
		sc.next();
		for (int i = 0; i < nU; i++) {
			line = sc.next().split(";");
			int depcom = Integer.parseInt(line[0]);
			String nomCom = line[1];
			int iris = Integer.parseInt(line[2]);
			int dcomiris = Integer.parseInt(line[3]);
			String nomIris = line[4];
			String typeIris = line[5];
			double area = Double.parseDouble(line[6]);
			String polygon = line[7];
			// System.out.println(polygon);
			String points[] = polygon.split(",");
			Polygon poly = new Polygon();
			for (int p = 0; p < points.length; p++) {
				String strPoint = points[p];
				if (p == 0) {
					strPoint = strPoint.substring(9);
				} else if (p == points.length - 1) {
					strPoint = strPoint.substring(0, strPoint.length() - 2);
				}
				strPoint = strPoint.substring(1);
				String coordinates[] = strPoint.split(" ");
				Point vertex = new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
				poly.addPoint(vertex);
			}
			String centroid = line[8];
			centroid = centroid.substring(7);
			String cent[] = centroid.split(" ");
			cent[1] = cent[1].substring(0, cent[1].length() - 1);
			Point center = new Point(Double.parseDouble(cent[0]), Double.parseDouble(cent[1]));
			poly.setCentroid(center);
			int id = Integer.parseInt(line[9]);
			double lon2154 = Double.parseDouble(line[10]);
			double lat2154 = Double.parseDouble(line[11]);
			double lon = Double.parseDouble(line[12]);
			double lat = Double.parseDouble(line[13]);
			this.iriZones[i] = new IrisZone(i, depcom, nomCom, iris, dcomiris, nomIris, typeIris, area, lon2154,
					lat2154, lon, lat, poly);
			// System.out.println(this.nodesU[i]);
		}
		sc.close();

		// on établie la demande
		BufferedReader reader = new BufferedReader(new FileReader(pathToInstance + "demand.csv"));
		reader.readLine();
		String lineS;
		while ((lineS = reader.readLine()) != null) {
			String[] split = lineS.split(",");
			double sum = 0;
			for (int i = 3; i < 27; i++) {
				sum += Double.parseDouble(split[i]);
			}
			this.oD[(int) Double.parseDouble(split[27])][(int) Double.parseDouble(split[28])] = (int) Math.round(sum);
		}
		reader.close();

		// On construit les routes
		sc = new Scanner(new File(pathToInstance + "roads.csv"));
		sc.useDelimiter("\n");
		sc.next();
		ArrayList<RoadNode> rN = new ArrayList<>();
		for (int i = 0; i < nR; i++) {
			line = sc.next().split(";");
			long id = Long.parseLong(line[0]);
			double lon = Double.parseDouble(line[1]);
			double lat = Double.parseDouble(line[2]);
			rN.add(new RoadNode(id, lon, lat));
		}
		rN.sort(null);
		int compt = 0;
		for (RoadNode r : rN) {
			roads[compt] = r;
			compt++;
			// System.out.println(r);
		}
		sc.close();

		this.routes = new ArrayList<>();

		// On construit les arrêts de bus
		sc = new Scanner(new File(pathToInstance + "tc_info_lyon.csv"));
		sc.useDelimiter("\n");
		sc.next();
		ArrayList<Route> routesTemp = new ArrayList<>();
		ArrayList<Stop> currStops = new ArrayList<>();
		int indexStop = 0;
		while (sc.hasNext()) {
			line = sc.next().split(";");
			int stopId = Integer.parseInt(line[0]);
			String stopName = line[1];
			int sequence = Integer.parseInt(line[2]);
			double lon = Double.parseDouble(line[3]);
			double lat = Double.parseDouble(line[4]);
			String routeId = line[5];
			String routeShortName = line[6];
			String routeLongName = line[7];
			long idClosestNode = Long.parseLong(line[8]);
			double distToClosestNode = Double.parseDouble(line[9]);
			int dcomiris = Integer.parseInt(line[10]);
			double surface = Double.parseDouble(line[11]);
			int degree = Integer.parseInt(line[12]);
			Route route = new Route(routeId, routeShortName, routeLongName);
			routesTemp.add(route);
			BusSequence busSeq = new BusSequence(sequence, route);
			RoadNode closestNode = getClosestNode(idClosestNode, this.roads);
			IrisZone irisZoneIn = getInIrisZone(dcomiris, this.iriZones);
			Stop currStop = null;
			currStop = getStopIfExists(stopId, currStops);
			if (currStop == null) {
				currStop = new Stop(stopId, indexStop, stopName, lon, lat, closestNode, distToClosestNode, irisZoneIn,
						degree, surface);
				currStops.add(currStop);
				indexStop++;
			}
			currStop.addBusSequence(busSeq);
			closestNode.addStop(currStop);
		}
		ArrayList<String> idArray = new ArrayList<>();
		for (Route r : routesTemp) {
			if (!idArray.contains(r.getId())) {
				if (Collections.frequency(routesTemp, r) > ligneMin) {
					routes.add(r);
					idArray.add(r.getId());
				}
			}
		}
		HashSet<Route> set = new HashSet<>(routes);
		routes = new ArrayList<Route>(set);

		sc.close();

		int totalStop = 0;
		for (RoadNode rN2 : roads) {
			rN2.uniqueStop();
			totalStop += rN2.nbStopDeserves();
			// System.out.println(rN2.displayStops());
		}
		System.out.println("total stop : " + totalStop);

		currStops.sort(null);
		int comptStops = 0;
		for (Stop s : currStops) {
			s.getLines().sort(null);
			this.stops[comptStops] = s;
			// System.out.println(s);
			comptStops++;
		}

		for (int i = 0; i < stops.length; i++) {
			if (stops[i] == null) {
				System.out.println("problem " + i);
			}
		}

		for (int i = 0; i < nT; i++) {
			stops[i].setIndex(i);
		}

		// On lit le fichier des plus courts chemins, et on remplit la matrice P
		sc = new Scanner(new File(pathToInstance + "sp_matrix_lyon.csv"));
		sc.useDelimiter("\n");
		sc.next();
		while (sc.hasNext()) {
			line = sc.next().split(";");
			long idFrom = Long.parseLong(line[0]);
			long idTo = Long.parseLong(line[1]);
			double distance = Double.parseDouble(line[2]);
			RoadNode from = this.getClosestNode(idFrom, roads);
			RoadNode to = this.getClosestNode(idTo, roads);
			for (Stop sFrom : from.getStopDeserved()) {
				for (Stop sTo : to.getStopDeserved()) {
					pC[sFrom.getIndex()][sTo.getIndex()] = distance / this.speedBus;
				}
			}
		}
		sc.close();

		// on nettoie les données
		int nbProb = 0;
		int nbBigProb = 0;
		for (int i = 0; i < nT; i++) {
			for (int j = 0; j < nT; j++) {
				if (pC[i][j] < 0) {
					nbProb++;
					if (pC[j][i] >= 0) {
						pC[i][j] = pC[j][i];
					} else {
						pC[i][j] = doubleBigEnough;
						nbBigProb++;
					}
				}
			}
		}
		System.out.println("Nb Path A->B = -1     " + nbProb);
		System.out.println("Nb Path A->B AND B->A = -1      " + nbBigProb);

		// On lit les distances entre les centres de zones IRIS et les arrêts de bus
		sc = new Scanner(new File(pathToInstance + "bus_stop_to_iris_distances.csv"));
		sc.useDelimiter("\n");
		sc.next();
		while (sc.hasNext()) {
			line = sc.next().split(";");
			int stopId = Integer.parseInt(line[0]);
			long closestNodeId = Long.parseLong(line[1]);
			int dComiris = Integer.parseInt(line[2]);
			double dStopIris = Double.parseDouble(line[3]);
			double dClosestNodeIris = Double.parseDouble(line[4]);
			for (int i = 0; i < nU; i++) {
				for (int j = 0; j < nT; j++) {
					if (iriZones[i].getdComiris() == dComiris && stops[j].getId() == stopId) {
						// On doit faire un choix ici. Quel est la distance d'accession ?
						// d[i][j] = dStopIris;
						d[i][j] = dClosestNodeIris / this.speedFoot;
					}
				}
			}
		}
		sc.close();

		// On lit les distances entre les centres de zones et les arrêts de bus
		/*
		 * sc = new Scanner(new File(pathToInstance +
		 * "newZoneIris_with_distances.csv")); sc.useDelimiter("\n"); sc.next(); this.dD
		 * = new double[nUD][nT]; while (sc.hasNext()) { line = sc.next().split(",");
		 * String zoneId = line[0]; int stopId = Integer.parseInt(line[1]); double
		 * distToBusStop = Double.parseDouble(line[3]); double distToClosestNode =
		 * Double.parseDouble(line[4]); int dcomiris =
		 * Integer.parseInt(zoneId.split("_")[0]); int index =
		 * Integer.parseInt(zoneId.split("_")[1]); for (int i = 0; i < nUD; i++) { for
		 * (int j = 0; j < nT; j++) { if (this.zones.get(i).getDcomiris() == dcomiris &&
		 * this.zones.get(i).getIndex() == index && stops[j].getId() == stopId) { // on
		 * doit faire un choix ici dD[i][j] = distToClosestNode; } } }
		 * 
		 * } sc.close();
		 */

		for (int i = 0; i < nU; i++) {
			double min = Double.MAX_VALUE;
			int argmin = -1;
			for (int j = 0; j < nT; j++) {
				if (d[i][j] < min) {
					min = d[i][j];
					argmin = j;
				}
			}
			if (argmin == -1) {
				System.out.println("Problem in the min");
			}
			this.acc[i] = argmin;
			this.dAccess[i] = min;
		}

		float demandTot = 0;
		for (int i = 0; i < nU; i++) {
			for (int j = 0; j < nU; j++) {
				demandTot += this.oD[i][j];
			}
		}
		for (int i = 0; i < nU; i++) {
			float demandFrom = 0;
			for (int j = 0; j < nU; j++) {
				demandFrom += oD[i][j];
			}
			if (demandFrom > tHDemand * demandTot) {
				cantBeClosed[acc[i]] = 1;
				System.out.println("We force node " + stops[acc[i]].getId() + " for demand");
			}
		}
		for (int i = 0; i < nU; i++) {
			float demandTo = 0;
			for (int j = 0; j < nU; j++) {
				demandTo += oD[j][i];
			}
			if (demandTo > tHDemand * demandTot) {
				cantBeClosed[acc[i]] = 1;
				System.out.println("We force node " + stops[acc[i]].getId() + " for demand");
			}
		}

		// we set to 1 if the degree is "too" large
		int totalDegree = 0;
		for (Stop n : this.stops) {
			// System.out.println(n);
			totalDegree += n.getDegree();
		}

		for (int i = 0; i < nT; i++) {
			if (stops[i].getDegree() >= pourcDegree * totalDegree) {
				cantBeClosed[i] = 1;
				System.out.println("We force node " + stops[i].getId() + " for degrees");
			}
		}
		double maxD = Double.MIN_VALUE;
		for (int i = 0; i < nU; i++) {
			for (int j = 0; j < nT; j++) {
				if (d[i][j] > maxD) {
					maxD = d[i][j];
				}
			}
		}
		this.mTD = maxD;

		System.out.println("Done.");
	}

	private Zone getZoneFromId(int dcomiris, int index) {
		for (Zone z : zones) {
			if (z.getDcomiris() == dcomiris && z.getIndex() == index) {
				return z;
			}
		}
		return null;
	}

	private Stop getStopIfExists(int stopId, ArrayList<Stop> currStops) {
		for (Stop s : currStops) {
			if (s.getId() == stopId) {
				return s;
			}
		}
		return null;
	}

	private Stop getStopIfExistsArray(int stopId, Stop[] currStops) {
		for (Stop s : currStops) {
			if (s.getId() == stopId) {
				return s;
			}
		}
		return null;
	}

	private IrisZone getInIrisZone(int dcomiris, IrisZone[] nodesU2) {
		for (IrisZone iZ : nodesU2) {
			if (iZ.getdComiris() == dcomiris) {
				return iZ;
			}
		}
		System.err.println("Caution: unknown iris zone of dcomiris: " + dcomiris);
		return null;
	}

	private RoadNode getClosestNode(long idClosestNode, RoadNode[] roads2) {
		for (RoadNode rN : roads2) {
			if (rN.getId() == idClosestNode) {
				return rN;
			}
		}
		System.err.println("Caution: unknonw roadnode of id: " + idClosestNode);
		return null;
	}

	public void createDatFile(String outputName) throws FileNotFoundException {

		PrintWriter writer = new PrintWriter(outputName);

		writer.write("nT = " + this.nT + ";\n");

		// writer.write("nU = " + this.nU + ";\n");
		writer.write("nU = " + this.nUD + ";\n");

		writer.write("Mt = " + this.mTD + ";\n");

		writer.write("alpha = " + this.alpha + ";\n");

		writer.write("pElim = " + this.pElim + ";\n");

		// d
		writer.write("d =  \n");
		writer.write("[");
		for (int i = 0; i < nUD - 1; i++) {
			writer.write("[");
			for (int j = 0; j < nT; j++) {
				if (j != nT - 1) {
					// writer.write(this.d[i][j] + ", ");
					writer.write(this.dD[i][j] + ", ");
				} else {
					// writer.write(this.d[i][j] + "");
					writer.write(this.dD[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nT - 1; j++) {
			// writer.write(this.d[nU - 1][j] + ", ");
			writer.write(this.dD[nUD - 1][j] + ", ");
		}
		// writer.write(this.d[nU - 1][nT - 1] + "]];\n");
		writer.write(this.dD[nUD - 1][nT - 1] + "]];\n");

		// PC
		writer.write("PC =  \n");
		writer.write("[");
		for (int i = 0; i < nT - 1; i++) {
			writer.write("[");
			for (int j = 0; j < nT; j++) {
				if (j != nT - 1) {
					writer.write(this.pC[i][j] + ", ");
				} else {
					writer.write(this.pC[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nT - 1; j++) {
			writer.write(this.pC[nT - 1][j] + ", ");
		}
		writer.write(this.pC[nT - 1][nT - 1] + "]];\n");

		// OD
		writer.write("OD =  \n");
		writer.write("[");
		for (int i = 0; i < nUD - 1; i++) {
			writer.write("[");
			for (int j = 0; j < nUD; j++) {
				if (j != nUD - 1) {
					writer.write(this.oDD[i][j] + ", ");
				} else {
					writer.write(this.oDD[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nUD - 1; j++) {
			writer.write(this.oDD[nUD - 1][j] + ", ");
		}
		writer.write(this.oDD[nUD - 1][nUD - 1] + "]];\n");

		// dAccess
		writer.write("dAccess= \n [");
		for (int i = 0; i < nUD; i++) {
			if (i < nUD - 1) {
				writer.write(dAccessD[i] + ",");
			} else {
				writer.write(dAccessD[i] + "");
			}
		}
		writer.write("];\n");

		// k
		writer.write("k= \n [");
		for (int i = 0; i < nUD; i++) {
			if (i < nUD - 1) {
				writer.write(kD[i] + ",");
			} else {
				writer.write(kD[i] + "");
			}
		}
		writer.write("];\n");

		// k
		writer.write("acc= \n [");
		for (int i = 0; i < nUD; i++) {
			if (i < nUD - 1) {
				writer.write(accD[i] + ",");
			} else {
				writer.write(accD[i] + "");
			}
		}
		writer.write("];\n");

		// cantBeClosed
		writer.write("cantBeClosed= \n [");
		for (int i = 0; i < nT; i++) {
			if (i < nT - 1) {
				writer.write(cantBeClosed[i] + ",");
			} else {
				writer.write(cantBeClosed[i] + "");
			}
		}
		writer.write("];\n");

		/*
		 * writer.write("nbActivPerZone= \n ["); for (int i = 0; i < nU; i++) { int
		 * sumIris = 0; for (int j = 0; j < nT; j++) { sumIris += isIn[i][j]; } if (i <
		 * nU - 1) { writer.write(Math.round(sumIris*pourcOpenPerIris) + ",");
		 * //writer.write(Math.round(nodesU[i].getSurface()*nbImposedBykm2) + ","); }
		 * else { writer.write(Math.round(Math.round(sumIris*pourcOpenPerIris)) + ""); }
		 * } writer.write("];\n");
		 * 
		 * writer.write("isIn =  \n"); writer.write("["); for (int i = 0; i < nU - 1;
		 * i++) { writer.write("["); for (int j = 0; j < nT; j++) { if (j != nT - 1) {
		 * writer.write(this.isIn[i][j] + ", "); } else { writer.write(this.isIn[i][j] +
		 * ""); } } writer.write("], \n"); } writer.write("["); for (int j = 0; j < nT -
		 * 1; j++) { writer.write(this.isIn[nU - 1][j] + ", "); }
		 * writer.write(this.isIn[nU - 1][nT - 1] + "]];\n");
		 */
		writer.close();
	}

	public void createAncientDatFile(String outputName) throws FileNotFoundException {

		PrintWriter writer = new PrintWriter(outputName);

		writer.write("nT = " + this.nT + ";\n");

		// writer.write("nU = " + this.nU + ";\n");
		writer.write("nU = " + this.nU + ";\n");

		writer.write("Mt = " + this.mT + ";\n");

		writer.write("alpha = " + this.alpha + ";\n");

		writer.write("pElim = " + this.pElim + ";\n");

		// d
		writer.write("d =  \n");
		writer.write("[");
		for (int i = 0; i < nU - 1; i++) {
			writer.write("[");
			for (int j = 0; j < nT; j++) {
				if (j != nT - 1) {
					// writer.write(this.d[i][j] + ", ");
					writer.write(this.d[i][j] + ", ");
				} else {
					// writer.write(this.d[i][j] + "");
					writer.write(this.d[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nT - 1; j++) {
			// writer.write(this.d[nU - 1][j] + ", ");
			writer.write(this.d[nU - 1][j] + ", ");
		}
		// writer.write(this.d[nU - 1][nT - 1] + "]];\n");
		writer.write(this.d[nU - 1][nT - 1] + "]];\n");

		// PC
		writer.write("PC =  \n");
		writer.write("[");
		for (int i = 0; i < nT - 1; i++) {
			writer.write("[");
			for (int j = 0; j < nT; j++) {
				if (j != nT - 1) {
					writer.write(this.pC[i][j] + ", ");
				} else {
					writer.write(this.pC[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nT - 1; j++) {
			writer.write(this.pC[nT - 1][j] + ", ");
		}
		writer.write(this.pC[nT - 1][nT - 1] + "]];\n");

		// OD
		writer.write("OD =  \n");
		writer.write("[");
		for (int i = 0; i < nU - 1; i++) {
			writer.write("[");
			for (int j = 0; j < nU; j++) {
				if (j != nU - 1) {
					writer.write(this.oD[i][j] + ", ");
				} else {
					writer.write(this.oD[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nU - 1; j++) {
			writer.write(this.oD[nU - 1][j] + ", ");
		}
		writer.write(this.oD[nU - 1][nU - 1] + "]];\n");

		// dAccess
		writer.write("dAccess= \n [");
		for (int i = 0; i < nU; i++) {
			if (i < nU - 1) {
				writer.write(dAccess[i] + ",");
			} else {
				writer.write(dAccess[i] + "");
			}
		}
		writer.write("];\n");

		// k
		writer.write("k= \n [");
		for (int i = 0; i < nU; i++) {
			if (i < nU - 1) {
				writer.write(k[i] + ",");
			} else {
				writer.write(k[i] + "");
			}
		}
		writer.write("];\n");

		// k
		writer.write("acc= \n [");
		for (int i = 0; i < nU; i++) {
			if (i < nU - 1) {
				writer.write(acc[i] + ",");
			} else {
				writer.write(acc[i] + "");
			}
		}
		writer.write("];\n");

		// cantBeClosed
		writer.write("cantBeClosed= \n [");
		for (int i = 0; i < nT; i++) {
			if (i < nT - 1) {
				writer.write(cantBeClosed[i] + ",");
			} else {
				writer.write(cantBeClosed[i] + "");
			}
		}
		writer.write("];\n");

		/*
		 * writer.write("nbActivPerZone= \n ["); for (int i = 0; i < nU; i++) { int
		 * sumIris = 0; for (int j = 0; j < nT; j++) { sumIris += isIn[i][j]; } if (i <
		 * nU - 1) { writer.write(Math.round(sumIris*pourcOpenPerIris) + ",");
		 * //writer.write(Math.round(nodesU[i].getSurface()*nbImposedBykm2) + ","); }
		 * else { writer.write(Math.round(Math.round(sumIris*pourcOpenPerIris)) + ""); }
		 * } writer.write("];\n");
		 * 
		 * writer.write("isIn =  \n"); writer.write("["); for (int i = 0; i < nU - 1;
		 * i++) { writer.write("["); for (int j = 0; j < nT; j++) { if (j != nT - 1) {
		 * writer.write(this.isIn[i][j] + ", "); } else { writer.write(this.isIn[i][j] +
		 * ""); } } writer.write("], \n"); } writer.write("["); for (int j = 0; j < nT -
		 * 1; j++) { writer.write(this.isIn[nU - 1][j] + ", "); }
		 * writer.write(this.isIn[nU - 1][nT - 1] + "]];\n");
		 */
		writer.close();
	}

	public void outputNodes(String resX, String nameOutput) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(nameOutput);
		for (int i = 1; i < resX.length(); i += 2) {
			if (resX.charAt(i) == '1' && i != resX.length() - 1) {
				writer.write(this.stops[i / 2].getId() + "\n");
			} else if (resX.charAt(i) == '1' && i == resX.length()) {
				writer.write(this.stops[i / 2].getId());
			}
		}
		writer.close();
	}

	public double getRealDelay(String resX, String trajBeforeR, String trajAfterR) throws FileNotFoundException {
		PrintWriter writerBefore = new PrintWriter(trajBeforeR);
		PrintWriter writerAfter = new PrintWriter(trajAfterR);

			// x as a vector of boolean and not of string
			int x[] = new int[nT];
			int indiceX = 0;
			for (int i = 0; i < resX.length(); i += 2) {
				if (resX.charAt(i) == '1' && i != resX.length() - 1) {
					x[indiceX] = 1;
				} else if (resX.charAt(i) == '1' && i == resX.length() - 1) {
					x[indiceX] = 1;
				}
				indiceX++;
			}

			double realTAccess[] = new double[nUD];
			int realAcc[] = new int[nUD];
			for (int i = 0; i < nUD; i++) {
				double minAccess = Double.MAX_VALUE;
				int indexMin = -1;
				for (int j = 0; j < nT; j++) {
					if (this.dD[i][j] < minAccess && x[j] == 1) {
						minAccess = this.dD[i][j];
						indexMin = j;
					}
				}
				realTAccess[i] = minAccess;
				realAcc[i] = indexMin;
			}
			for (int u1 = 0; u1 < nUD; u1++){
				System.out.println(u1 + "/1464");
				for (int u2 = 0; u2 < nUD; u2++) {
					double trajBefore = this.dAccessD[u1] + this.pC[accD[u1]][accD[u2]] + this.dAccessD[u2];
					writerBefore.write(trajBefore + " ");
					double trajAfter = realTAccess[u1] + this.pC[realAcc[u1]][realAcc[u2]] + realTAccess[u2];
					writerAfter.write(trajAfter + " ");

				}
			}
			/*
			 * int sumX = 0; for (int i = 0; i < nT; i++) { sumX += x[i]; }
			 */

			// System.out.println(res + " " + falseRes);
			// System.out.println("the error is: " + error + "(" + pourcError*100 + "%)");
			// System.out.println("cost OPL: " + costOPL);
			// System.out.println("The network has: " + sumX + " nodes");
			
			
			/*double res = 0;
			double previousRes = 0;
			for (int u1 = 0; u1 < nUD; u1++) {
				System.out.println(u1 + "/1464");
				for (int u2 = 0; u2 < nUD; u2++) {
					double argminDv1 = -1;
					double argminDv2 = -1;
					double argminPC = -1;
					double min = Double.MAX_VALUE;
					double minPrevious = Double.MAX_VALUE;
					for (int v1 = 0; v1 < nT; v1++) {
						for (int v2 = 0; v2 < nT; v2++) {
							if (x[v1] == 1 && x[v2] == 1) {
								double traj = dD[u1][v1] + pC[v1][v2] + dD[u2][v2];
								if (traj < min) {
									min = traj;
									argminDv1 = dD[u1][v1];
									argminDv2 = dD[u2][v2];
									argminPC = pC[v1][v2];
								}
							}
							double trajPrevious = dD[u1][v1] + pC[v1][v2] + dD[u2][v2];
							if (trajPrevious < minPrevious) {
								minPrevious = trajPrevious;
							}
						}
					}
					falseRes += this.oDD[u1][u2]
							* (realTAccess[u1] + realTAccess[u2] + this.pC[this.accD[u1]][this.accD[u2]]);
					costOPL += (this.oDD[u1][u2] * (realTAccess[u1] + realTAccess[u2]));

					res += (oDD[u1][u2] * min);
					previousRes += oDD[u1][u2] * (minPrevious);

					if (min < realTAccess[u1] + realTAccess[u2] + this.pC[this.accD[u1]][this.accD[u2]] - 0.0000001) {
						// System.out.println("u1 -> v1 : " + argminDv1/60.0 + " " +
						// realTAccess[u1]/60.0);
						// System.out.println("v1 -> v2 : " + argminPC/60.0 + " " +
						// this.pC[this.acc[u1]][this.acc[u2]]/60.0);
						// System.out.println("v2 -> u2 : " + argminDv2/60.0 + " " +
						// realTAccess[u2]/60.0);
						double walkMore = argminDv1 + argminDv2 - realTAccess[u1] - realTAccess[u2];
						// System.out.println("Walk : +" + walkMore/60.0);
						writer.write(walkMore / 60.0 + " "
								+ (realTAccess[u1] + realTAccess[u2] + this.pC[this.accD[u1]][this.accD[u2]] - min) / 60.0
								+ "\n");
						// System.out.println("-----------------");
					}
				}
			}
			System.out.println("The cost from OPL is : " + costOPL);
			System.out.println("The real cost is : " + res);
			System.out.println("The previous cost is : " + previousRes);
			System.out.println("Our false res : " + falseRes);
			*/
			writerBefore.close();
			writerAfter.close();
			return 0;
		
	}

	public void getAnalysis(String output, int[] resX) {

	}

	public int[] arrayFromString(String resX, String name) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(name);
		int x[] = new int[nT];
		int indiceX = 0;
		for (int i = 0; i < resX.length(); i += 2) {
			if (resX.charAt(i) == '1' && i != resX.length() - 1) {
				x[indiceX] = 1;
				this.stops[i / 2].setOpen(true);
			} else if (resX.charAt(i) == '1' && i == resX.length() - 1) {
				x[indiceX] = 1;
				this.stops[i / 2].setOpen(true);
			}
			indiceX++;

		}
		int nbOpenStops = 0;
		for (int i = 0; i < x.length; i++) {
			if (x[i] == 1) {
				writer.write(stops[i].getId() + " ");
				nbOpenStops++;
			}
		}
		System.out.println("Nb Open Stops :" + nbOpenStops);
		writer.close();
		return x;
	}

	public ArrayList<StopInLine> modifiedLine(String routeID) {
		ArrayList<StopInLine> res = new ArrayList<StopInLine>();
		Route r = getRouteFromId(routeID);
		for (Stop s : this.stops) {
			for (BusSequence bS : s.getLines()) {
				if (bS.getRoute().getId().equals(routeID)) {
					res.add(new StopInLine(s, bS.getSequence()));
				}
			}
		}
		res.sort(null);
		return res;
	}

	public Route getRouteFromId(String routeID) {
		for (Route r : routes) {
			if (routeID.equals(r.getId())) {
				return r;
			}
		}
		System.err.println("Caution: unknown route of id: " + routeID);
		return null;
	}

	public void createPointsIrisDivided(String pathToInstance, String regex) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("newZoneIris.csv");
		writer.write("DCOMIRIS,index,lambert_lon,lamber_lat\n");

		PrintWriter writerNbInteriorPoints = new PrintWriter("nbInteriorPoints.txt");
		ArrayList<Zone> zonesArray = new ArrayList<>();
		int nbIrisDivided = 0;
		for (IrisZone iZ : iriZones) {
			int index = 0;
			ArrayList<Point> iZnteriorPoints = iZ.getPoly().getInteriorPoints();
			for (Point iP : iZnteriorPoints) {
				writer.write(iZ.getdComiris() + ",");
				writer.write(index + ",");
				writer.write(iP.getX() + ",");
				writer.write(iP.getY() + "\n");
				zonesArray.add(new Zone(iZ.getdComiris(), index, nbIrisDivided));
				index++;
				nbIrisDivided++;
			}
		}
		this.nUD = nbIrisDivided;
		this.oDD = new int[nUD][nUD];
		this.dAccessD = new double[nUD];
		this.kD = new double[nUD];
		this.accD = new int[nUD];
		this.zones = zonesArray;
		this.zones.sort(null);

		// On lit les distances entre les centres de zones et les arrêts de bus
		Scanner sc = new Scanner(new File(pathToInstance + "newZoneIris" + regex + "_with_distances_sorted.csv"));
		sc.useDelimiter("\n");
		this.dD = new double[nUD][nT];
		int nbIter = 0;
		int iterJ = 0;
		int iterI = 0;
		int dcomirisPrev = 0;
		while (sc.hasNext() && iterJ < nT && iterI < nUD) {
			String[] line = sc.next().split(",");
			String zoneId = line[0];
			int stopId = Integer.parseInt(line[1]);
			double distToBusStop = Double.parseDouble(line[3]);
			// System.out.println(distToBusStop);
			double distToClosestNode = Double.parseDouble(line[4]);
			int dcomiris = Integer.parseInt(zoneId.split("_")[0]);
			dcomirisPrev = dcomiris;
			int index = Integer.parseInt(zoneId.split("_")[1]);
			dD[iterI][iterJ] = distToBusStop / speedFoot;
			nbIter++;
			iterJ++;
			if (iterJ == nT) {
				iterI++;
				iterJ = 0;
			}
		}
		sc.close();

		double oDTemp[][] = new double[nU][nU];
		for (int i = 0; i < nU; i++) {
			for (int j = 0; j < nU; j++) {
				oDTemp[i][j] = amplificationOD * oD[i][j];
			}
		}
		int nbIterODD = 0;
		for (IrisZone iZFrom : this.iriZones) {
			int nbInteriorPointsFrom = iZFrom.getPoly().getNbInteriorPoints();
			writerNbInteriorPoints.write(nbInteriorPointsFrom + " ");
			for (IrisZone iZTo : this.iriZones) {
				int nbInteriorPointsTo = iZTo.getPoly().getNbInteriorPoints();
				for (int i = 0; i < nbInteriorPointsFrom; i++) {
					for (int j = 0; j < nbInteriorPointsTo; j++) {
						// System.out.println(oDTemp[iZFrom.getId()][iZTo.getId()]);
						int newDemand = ((int) (Math.round(((double) oDTemp[iZFrom.getId()][iZTo.getId()])
								/ ((double) nbInteriorPointsFrom * nbInteriorPointsTo))));
						oDD[nbIterODD / nUD][nbIterODD % nUD] = newDemand;
						// System.out.println((nbIterODD/nUD) + " " + (nbIterODD%nUD));
						// System.out.println(i + " " + j + " " + newDemand);

						nbIterODD++;
					}

				}
			}
		}

		writerNbInteriorPoints.close();
		writer.close();
		for (int i = 0; i < nUD; i++) {
			double minDD = Double.MAX_VALUE;
			int argmin = -1;
			for (int j = 0; j < nT; j++) {
				if (dD[i][j] < minDD) {
					minDD = dD[i][j];
					argmin = j;
				}
			}
			if (argmin == -1) {
				System.out.println("Problem in the min");
			}
			this.accD[i] = argmin;
			this.dAccessD[i] = minDD;

		}

		float demandTot = 0;
		for (int i = 0; i < nUD; i++) {
			for (int j = 0; j < nUD; j++) {
				demandTot += this.oDD[i][j];
			}
		}
		for (int i = 0; i < nUD; i++) {
			float demandFrom = 0;
			for (int j = 0; j < nUD; j++) {
				demandFrom += oDD[i][j];
			}
			if (demandFrom > tHDemand * demandTot) {
				cantBeClosed[accD[i]] = 1;
				System.out.println("We force node " + stops[accD[i]].getId() + " for demand");
			}
		}
		for (int i = 0; i < nUD; i++) {
			float demandTo = 0;
			for (int j = 0; j < nUD; j++) {
				demandTo += oDD[j][i];
			}
			if (demandTo > tHDemand * demandTot) {
				cantBeClosed[accD[i]] = 1;
				System.out.println("We force node " + stops[accD[i]].getId() + " for demand");
			}
		}

		// we set to 1 if the degree is "too" large
		int totalDegree = 0;
		for (Stop n : this.stops) {
			// System.out.println(n);
			totalDegree += n.getDegree();
		}

		for (int i = 0; i < nT; i++) {
			if (stops[i].getDegree() >= pourcDegree * totalDegree) {
				cantBeClosed[i] = 1;
				System.out.println("We force node " + stops[i].getId() + " for degrees");
			}
		}
		double maxD = Double.MIN_VALUE;
		for (int i = 0; i < nUD; i++) {
			for (int j = 0; j < nT; j++) {
				if (dD[i][j] > maxD) {
					maxD = dD[i][j];
				}
			}
		}
		this.mTD = maxD;
		for (int u = 0; u < nUD; u++) {
			kD[u] = 2.0;
			/*
			 * if (dAccessD[u] < 5.0 * 60.0) { kD[u] = 6.0; } else if (dAccessD[u] < 10.0 *
			 * 60.0) { kD[u] = 3.0; } else if (dAccessD[u] < 15.0 * 60.0) { kD[u] = 2.0; }
			 * else { kD[u] = 1.0; }
			 */
		}
	}

	public int getnUD() {
		return nUD;
	}

	public void setnUD(int nUD) {
		this.nUD = nUD;
	}

	public double[][] getdD() {
		return dD;
	}

	public void setdD(double[][] dD) {
		this.dD = dD;
	}

	public void plotLines() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter("pourc.txt");
		for (Route r : routes) {
			System.out.println(r.getId());
			double nbOpen = 0.0;
			double nbTot = 0.0;
			for (Stop s : this.stops) {
				for (BusSequence bS : s.getLines()) {
					if (bS.getRoute().getId().equals(r.getId())) {
						if (s.isOpen()) {
							System.out.print("O --> ");
							nbOpen++;
						} else {
							System.out.print("X --> ");
						}
						nbTot++;
					}
				}
			}
			double pourcOuvert = (nbOpen / nbTot) * 100;
			writer.write(String.valueOf(pourcOuvert));
			writer.write("\n");
			System.out.println("Pourcentage ouverts : " + pourcOuvert + "%");
			System.out.println("");
		}
		writer.close();
	}

	public ArrayList<Integer> getUnfeasibleIrisZones(double treshold) {
		int nbClose = 0;
		ArrayList<Integer> res = new ArrayList<>();
		int nbRouteClosed = 0;
		for (Route r : routes) {
			// System.out.println(r.getId());
			double nbOpen = 0.0;
			double nbTot = 0.0;
			for (Stop s : this.stops) {
				for (BusSequence bS : s.getLines()) {
					if (bS.getRoute().getId().equals(r.getId())) {
						if (s.isOpen()) {
							// System.out.print("O --> ");
							nbOpen++;
						} else {
							// System.out.print("X --> ");
						}
						nbTot++;
					}
				}
			}
			double pourcOuvert = (nbOpen / nbTot);
			// System.out.println(pourcOuvert);
			if (pourcOuvert < treshold) {
				nbRouteClosed++;
				for (Stop s : this.stops) {
					for (BusSequence bS : s.getLines()) {
						if (bS.getRoute().getId().equals(r.getId())) {
							// System.out.println(s.getId());
							if (s.isOpen()) {
								nbClose++;
							}
							s.setOpen(false);
						}
					}
				}
			}
		}

		for (int u = 0; u < nUD; u++) {
			double newAccess = Double.MAX_VALUE;
			for (int v = 0; v < this.stops.length; v++) {
				if (stops[v].isOpen() && dD[u][v] < newAccess) {
					newAccess = dD[u][v];
				}
			}
			// System.out.println(newAccess + " " );
			if (newAccess > kD[u] * dAccessD[u]) {
				res.add(u);
			}
		}
		System.out.println("We closed stops " + nbClose);
		System.out.println("We closed routes " + nbRouteClosed);
		return res;
	}

	public int getNRouteClosed(double treshold) throws FileNotFoundException {
		int nbClose = 0;
		ArrayList<Integer> res = new ArrayList<>();
		int nbRouteClosed = 0;
		for (Route r : routes) {
			// System.out.println(r.getId());
			double nbOpen = 0.0;
			double nbTot = 0.0;
			for (Stop s : this.stops) {
				for (BusSequence bS : s.getLines()) {
					if (bS.getRoute().getId().equals(r.getId())) {
						if (s.isOpen()) {
							// System.out.print("O --> ");
							nbOpen++;
						} else {
							// System.out.print("X --> ");
						}
						nbTot++;
					}
				}
			}
			double pourcOuvert = (nbOpen / nbTot);
			// System.out.println(pourcOuvert);
			if (pourcOuvert < treshold) {
				nbRouteClosed++;
				for (Stop s : this.stops) {
					for (BusSequence bS : s.getLines()) {
						if (bS.getRoute().getId().equals(r.getId())) {
							// System.out.println(s.getId());
							if (s.isOpen()) {
								nbClose++;
							}
							s.setOpen(false);
						}
					}
				}
			} else {
				System.out.print("\'" + r.id  + "\'"+ ", ");
			}
		}
		PrintWriter writer = new PrintWriter("hist_Huf_" + treshold + ".txt");
		for (int u = 0; u < nUD; u++) {
			double newAccess = Double.MAX_VALUE;
			for (int v = 0; v < this.stops.length; v++) {
				if (stops[v].isOpen() && dD[u][v] < newAccess) {
					newAccess = dD[u][v];
				}
			}
			// System.out.println(newAccess + " " );
			if (newAccess > (kD[u] * dAccessD[u])) {
				double d = newAccess - (kD[u] * dAccessD[u]);
				writer.write(d + " ");
				res.add(u);
			}
		}
		System.out.println("Nb violated = " + res.size());
		System.out.println("We closed stops " + nbClose);
		System.out.println("We closed routes " + nbRouteClosed);
		writer.close();
		return nbRouteClosed;
	}

	public void writeNbRouteClosed(String name) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(name);
		for (double i = 0.0; i <= 1.0; i += 0.1) {
			writer.write(this.getNRouteClosed(i) + "\n");
		}
		writer.close();
	}
}