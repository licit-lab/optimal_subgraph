import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Instance {
	private int nT;
	private int nU;
	private int nTram;
	private int nMetro;
	private Node[] nodesT;
	private IrisZone[] nodesU;
	private double mT;
	private double mU;
	private double alpha;
	private double pElim;
	private double tHDemand;
	private double pourcDegree;
	private double speedBus = 170.0/36.0;
	private double speedFoot = 30.0/36.0;
	private double pourcOpenPerIris = 0.5;

	private int[] degrees;
	private double[][] d;
	private double[][] pC;
	private int[][] oD;
	private double[] dAccess;
	private double[] k;
	private int[] acc;
	private int[] cantBeClosed;
	private int[][] isIn;

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

	public Instance(String pathToInstance, double alpha, double pElim, double kUniv, double tHDemand, double pourcDegree) throws IOException {
		this.alpha = alpha;
		this.pElim = pElim;
		this.tHDemand = tHDemand;

		BufferedReader reader = new BufferedReader(new FileReader(pathToInstance + "size.csv"));
		reader.readLine();
		this.nT = Integer.parseInt(reader.readLine().split(",")[1]);
		this.nU = Integer.parseInt(reader.readLine().split(",")[1]);
		this.nTram = Integer.parseInt(reader.readLine().split(",")[1]);
		this.nMetro = Integer.parseInt(reader.readLine().split(",")[1]);

		reader.close();
		System.out.println(nT + " " + nU);

		this.d = new double[nU][nT];
		this.pC = new double[nT][nT];
		this.oD = new int[nU][nU];
		this.dAccess = new double[nU];
		this.nodesT = new Node[nT];
		this.nodesU = new IrisZone[nU];
		this.k = new double[nU];
		this.acc = new int[nU];
		this.cantBeClosed = new int[nT];
		this.degrees = new int[nT];
		this.isIn = new int[nU][nT];

		
		

		reader = new BufferedReader(new FileReader(pathToInstance + "node_iris.csv"));
		reader.readLine();
		for (int i = 0; i < this.nU; i++) {
			String[] line = reader.readLine().split(",");
			int id = Integer.parseInt(line[0]);
			double coordX = Double.parseDouble(line[1].substring(2, line[1].length()));
			double coordY = Double.parseDouble(line[2].substring(0, line[2].length() - 2));
			int dCom = Integer.parseInt(line[3]);
			double surface = Double.parseDouble(line[4]);
			//this.nodesU[i] = new IrisZone(id, coordX, coordY, dCom, surface);
			System.out.println(nodesU[i]);
		}
		reader.close();
		
		reader = new BufferedReader(new FileReader(pathToInstance + "node_bus.csv"));
		reader.readLine();
		for (int i = 0; i < this.nT; i++) {
			String[] line = reader.readLine().split(",");
			int id = Integer.parseInt(line[0]);
			int geo = i;
			double coordX = Double.parseDouble(line[1].substring(2, line[1].length()));
			double coordY = Double.parseDouble(line[2].substring(0, line[2].length() - 2));
			int degree = Integer.parseInt(line[5]);
			int dcomiris = Integer.parseInt(line[6]);
			this.nodesT[i] = new Node(id, geo, coordX, coordY, degree, dcomiris, nodesU);
		}
		reader.close();
		
		ArrayList<Node> aN = new ArrayList<Node>();
		for (int i = 0; i < nT; i++) {
			aN.add(nodesT[i]);
		}
		aN.sort(null);
		int idNode = 0;
		for (Node n : aN) {
			n.setGeocenter(idNode);
			idNode++;
			//System.out.println(n);
		}
		for (int i = 0; i < this.nodesT.length; i++) {
			nodesT[i] = aN.get(i);
		}
		

		reader = new BufferedReader(new FileReader(pathToInstance + "demand.csv"));
		reader.readLine();
		String line;
		while ((line = reader.readLine()) != null) {
			String[] split = line.split(",");
			double sum = 0;
			for (int i = 3; i < 27; i++) {
				sum += Double.parseDouble(split[i]);
			}
			this.oD[(int) Double.parseDouble(split[27])][(int) Double.parseDouble(split[28])] = (int) Math.round(sum);
		}
		reader.close();

		reader = new BufferedReader(new FileReader(pathToInstance + "length_bus.csv"));
		reader.readLine();
		for (int i = 0; i < this.nT - 1; i++) {
			String[] lineBusToBus = reader.readLine().split(",");
			for (int j = 0; j < this.nT-1; j++) {
				double res;
				if (lineBusToBus[j+1] == "") {
					res = 0;
				} else {
					res = Double.parseDouble(lineBusToBus[j+1]);
				}
				//System.out.println(i + " " + j + " " + res);
				//System.out.println(res);
				pC[i][j] = res/speedBus;

			}
		}
		String[] lineBusToBus = reader.readLine().split(",");
		for (int j = 0; j < this.nT - 1; j++) {
			double res;
			res = Double.parseDouble(lineBusToBus[j]);
			pC[nT - 1][j] = res/speedBus;
		}
		pC[nT - 1][nT - 1] = 0;

		reader.close();

		for (int i = 0; i < nT; i++) {
			for (int j = 0; j < nT; j++) {
				//System.out.print(pC[i][j] + " ");
			}
			//System.out.println("");
		}

		reader = new BufferedReader(new FileReader(pathToInstance + "dist_iris_bus.csv"));
		reader.readLine();
		for (int i = 0; i < this.nU; i++) {
			String[] lineZoneToBus = reader.readLine().split(",");
			for (int j = 0; j < this.nT; j++) {
				double res;
				if (lineZoneToBus[j + 1] == "") {
					res = 0;
				} else {
					res = Double.parseDouble(lineZoneToBus[j + 1]);
				}
				d[i][j] = res/speedFoot;

			}
		}
		reader.close();
		this.mT = 0;
		for (int i = 0; i < nT; i++) {
			for (int j = 0; j < nT; j++) {
				if (this.mT < pC[i][j]) {
					this.mT = pC[i][j];
				}
			}
		}

		this.mU = 0;
		for (int i = 0; i < nU; i++) {
			for (int j = 0; j < nT; j++) {
				if (this.mU < d[i][j]) {
					this.mU = d[i][j];
				}
			}
		}

		for (int i = 0; i < nU; i++) {
			double min = Double.MAX_VALUE;
			int argmin = -1;
			for (int j = 0; j < nT; j++) {
				if (d[i][j] < min) {
					min = d[i][j];
					argmin = j;
				}
			}
			this.acc[i] = argmin;
			this.dAccess[i] = min;
		}

		for (int i = 0; i < nU; i++) {
			k[i] = kUniv;
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
				System.out.println("We force node " + nodesT[acc[i]].getId() + " for demand");
			}
		}
		for (int i = 0; i < nU; i++) {
			float demandTo = 0;
			for (int j = 0; j < nU; j++) {
				demandTo += oD[j][i];
			}
			if (demandTo > tHDemand * demandTot) {
				cantBeClosed[acc[i]] = 1;
				System.out.println("We force node " + nodesT[acc[i]].getId() + " for demand");
			}
		}
		
		//we set to 1 if the degree is "too" large
		int totalDegree = 0;
		for (Node n : this.nodesT) {
			totalDegree += n.getDegree();
		}
		for (Node n : this.nodesT) {
			if (n.getDegree() >= pourcDegree*totalDegree) {
				cantBeClosed[n.getGeocenter()] = 1;
				System.out.println("We force node " + n.getId() + " for degrees");
			}
		}
		
		for (IrisZone iris : nodesU) {
			for (Node v : nodesT) {
				if (v.getIris().equals(iris)) {
					isIn[iris.getId()][v.getGeocenter()] = 1;
				} else {
					isIn[iris.getId()][v.getGeocenter()] = 0;
				}
			}
		}
		

	}

	public void createDatFile(String outputName) throws FileNotFoundException {

		PrintWriter writer = new PrintWriter(outputName);

		writer.write("nT = " + this.nT + ";\n");

		writer.write("nU = " + this.nU + ";\n");

		writer.write("Mu = " + this.mU + ";\n");

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
					writer.write(this.d[i][j] + ", ");
				} else {
					writer.write(this.d[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nT - 1; j++) {
			writer.write(this.d[nU - 1][j] + ", ");
		}
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
		
		writer.write("nbActivPerZone= \n [");		
		for (int i = 0; i < nU; i++) {
			int sumIris = 0;
			for (int j = 0; j < nT; j++) {
				sumIris += isIn[i][j];
			}
			if (i < nU - 1) {
				writer.write(Math.round(sumIris*pourcOpenPerIris) + ",");
				//writer.write(Math.round(nodesU[i].getSurface()*nbImposedBykm2) + ",");
			} else {
				writer.write(Math.round(Math.round(sumIris*pourcOpenPerIris)) + "");
			}
		}
		writer.write("];\n");
		
		writer.write("isIn =  \n");
		writer.write("[");
		for (int i = 0; i < nU - 1; i++) {
			writer.write("[");
			for (int j = 0; j < nT; j++) {
				if (j != nT - 1) {
					writer.write(this.isIn[i][j] + ", ");
				} else {
					writer.write(this.isIn[i][j] + "");
				}
			}
			writer.write("], \n");
		}
		writer.write("[");
		for (int j = 0; j < nT - 1; j++) {
			writer.write(this.isIn[nU - 1][j] + ", ");
		}
		writer.write(this.isIn[nU - 1][nT - 1] + "]];\n");

		writer.close();
	}

	public void outputNodes(String resX, String nameOutput) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(nameOutput);
		for (int i = 1; i < resX.length(); i += 2) {
			if (resX.charAt(i) == '1' && i != resX.length() - 1) {
				writer.write(this.nodesT[i / 2].getId() + "\n");
			} else if (resX.charAt(i) == '1' && i == resX.length()) {
				writer.write(this.nodesT[i / 2].getId());
			}
		}
		writer.close();
	}
	
	public double getRealDelay(String resX, String outPutR) throws FileNotFoundException {
		try (PrintWriter writer = new PrintWriter(outPutR)) {
			//x as a vector of boolean and not of string
			int x[] = new int[nT];
			int indiceX = 0;
			for (int i = 1; i < resX.length(); i += 2) {
				if (resX.charAt(i) == '1' && i != resX.length() - 1) {
					x[indiceX] = 1;
				} else if (resX.charAt(i) == '1' && i == resX.length()) {
					x[indiceX] = 0;
				}
				indiceX++;
			}
			
			
			double realTAccess[] = new double[nU];
			int realAcc[] = new int[nU];
			for (int i = 0; i < nU; i++) {
				double minAccess = Double.MAX_VALUE;
				int indexMin = -1;
				for (int j = 0; j < nT; j++) {
					if (this.d[i][j] < minAccess && x[j] == 1) {
						minAccess = this.d[i][j];
						indexMin = j;
					}
				}
				realTAccess[i] = minAccess;
				realAcc[i] = indexMin;
			}
			double falseRes = 0;
			double costOPL = 0; 
			/*int sumX = 0;
			for (int i = 0; i < nT; i++) {
				sumX += x[i];
			}*/
			
			//System.out.println(res + " " + falseRes);
			//System.out.println("the error is: " + error + "(" + pourcError*100 + "%)");
			//System.out.println("cost OPL: " + costOPL);
			//System.out.println("The network has: " + sumX + " nodes");
			double res = 0;
			double previousRes = 0;
			for (int u1 = 0; u1 < nU; u1++) {
				for (int u2 = 0; u2 < nU; u2++) {
					double argminDv1 = -1;
					double argminDv2 = -1;
					double argminPC = -1;
					double min = Double.MAX_VALUE;
					double minPrevious = Double.MAX_VALUE;
					for (int v1 = 0; v1 < nT; v1++) {
						for (int v2 = 0; v2 < nT; v2++) {
							if (x[v1] == 1 && x[v2] == 1) {
								double traj = d[u1][v1] + pC[v1][v2] + d[u2][v2];
								if (traj < min) {
									min = traj;
									argminDv1 = d[u1][v1];
									argminDv2 = d[u2][v2];
									argminPC = pC[v1][v2];
								}
							}
							double trajPrevious = d[u1][v1] + pC[v1][v2] + d[u2][v2];
							if (trajPrevious < minPrevious) {
								minPrevious = trajPrevious;
							}
						}
					}
					falseRes += this.oD[u1][u2] * (realTAccess[u1] + realTAccess[u2] + this.pC[this.acc[u1]][this.acc[u2]]);
					costOPL += (this.oD[u1][u2] * (realTAccess[u1] + realTAccess[u2]));
				
					res += (oD[u1][u2] * min);
					previousRes += oD[u1][u2]*(minPrevious);

					if (min < realTAccess[u1] + realTAccess[u2] + this.pC[this.acc[u1]][this.acc[u2]] - 0.0000001) {
						System.out.println("u1 -> v1 : " + argminDv1/60.0 + " " + realTAccess[u1]/60.0);
						System.out.println("v1 -> v2 : " + argminPC/60.0 + " " + this.pC[this.acc[u1]][this.acc[u2]]/60.0);
						System.out.println("v2 -> u2 : " + argminDv2/60.0 + " " + realTAccess[u2]/60.0);
						double walkMore = argminDv1 + argminDv2 - realTAccess[u1] -realTAccess[u2];
						System.out.println("Walk : +" + walkMore/60.0);
						writer.write(walkMore/60.0 + " " + (realTAccess[u1] + realTAccess[u2] + this.pC[this.acc[u1]][this.acc[u2]] - min)/60.0 + "\n");
						System.out.println("-----------------");
					}
				}
			}
			System.out.println("The cost from OPL is : " + costOPL);
			System.out.println("The real cost is : " + res);
			System.out.println("The previous cost is : " + previousRes);
			System.out.println("Our false res : " + falseRes);
			writer.close();
			return res;
		}
	}
	

}
