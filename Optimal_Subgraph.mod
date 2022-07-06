/*********************************************
 * OPL 12.10.0.0 Model
 * Author: matthieu
 * Creation Date: Nov 18, 2020 at 11:15:18 AM
 *********************************************/

 int nT = ...;
 int nU = ...;
 float Mt = ...;
 float alpha = ...;
 float pElim = ...;
 
 range nodesT = 1..nT;
 range nodesU = 1..nU;
 
 float d[nodesU][nodesT] = ...;
 float PC[nodesT][nodesT] = ...;
 float OD[nodesU][nodesU] = ...;
 float dAccess[nodesU] = ...;
 float k[nodesU] = ...;
 int acc[nodesU] = ...;
 int cantBeClosed[nodesT] = ...;
 
 dvar boolean x[nodesT];
 dvar float+ m[nodesU];
 
 minimize
 	sum(u1 in nodesU, u2 in nodesU) (m[u1] + m[u2])*OD[u1][u2];
 	
 subject to {
   
   	//(1) temps d'accès au réseau
   	forall(u in nodesU) {
    	sum(v in nodesT : d[u][v] <= k[u]*dAccess[u])x[v] >= 1;
   	}
 	
 	//(2)
 	forall(u in nodesU) {
		m[u] == min(v in nodesT : d[u][v] <= k[u]*dAccess[u]) (d[u][v] + (1-x[v])*Mt);
	}  
 	
 	//(3)
 	forall(u1 in nodesU, u2 in nodesU) {
		m[u1]+ m[u2] + PC[acc[u1]+1][acc[u2]+1] <= (1 + alpha)*(dAccess[u1] + dAccess[u2] + PC[acc[u1]+1][acc[u2]+1]);
	} 

   
   //(4)
   sum(v in nodesT)x[v] <= (1-pElim)*nT;
   
   //(5)
   forall(v in nodesT) {
     x[v] >= cantBeClosed[v];
   }
	  
   
 }
 execute DISPLAY{
    var sum = 0;
  	for (var v = 1; v <= nT; v++) {
  	  write(x[v] + " ")
  	 	sum = sum + x[v];
  	}
  	write("\n");
  	for (var w = 1; w <= nU; w++) {
  	  write(m[w] + " ");
  	}
  	write("\n");
  	write(sum);
  }    					
