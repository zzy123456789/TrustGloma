package SpectralCluster;

import java.util.Arrays;

/**
 * The clustering measures.
 * 
 * @author ziyin
 *
 */
public class ClusterMeasure {
	/**
	 * Run steps
	 */
	public static long runSteps;
	

	int[] blocks;

	int[] labels;

	/**
	 * Block vs. label. Each row corresponds to a block. Each column corresponds
	 * to a class label
	 */
	int[][] distributionMatrix;

	double ss;

	double sd;

	double ds;

	double dd;

	double jc;

	double fmi;

	double ri;

	double purity;
	
	double dbi;
	
	double di;

	int numBlocks;
	
	/**
	 * Each row of the block set may have different sizes.
	 */
	int[][] blockSet;
	
	/**
	 * The attribute value of every point.
	 */
	double[][] attributes;
	
	/**
	 * The attribute value of center.
	 */
	double[][] centers;

	/**
	 * Diameters of each block
	 */
	double[] diameters;

	/**
	 * average distance within blocks
	 */
	double[] averageDistances;

	/**
	 * minimal distances among blocks
	 */
	double[][] minDistances;

	/**
	 * minimal distances among centers
	 */
	double[][] centerDistances;
	
	/**
	 * Normalized mutual information.
	 */
	double nmi;

	/**
	 *************** 
	 * Build the measure.
	 **************** 
	 */
	public ClusterMeasure(int[] paraP, int[] paraR) {
		blocks = paraP;
		labels = paraR;

		computExternalMeasures();
	}// Of the constructor

	/**
	 *************** 
	 * Build the measure.
	 **************** 
	 */
	public ClusterMeasure(Cluster paraCluster) {
		blocks = paraCluster.blockInformation;
		labels = paraCluster.classLabels;

		computExternalMeasures();
	}// Of the constructor

	/**
	 *************** 
	 * Build the measure.
	 **************** 
	 */
	public void computExternalMeasures() {
		ss = sd = ds = dd = 0;
		for (int i = 0; i < blocks.length - 1; i++) {
			for (int j = i + 1; j < blocks.length; j++) {
				if ((blocks[i] == blocks[j]) && (labels[i] == labels[j])) {
					ss++;
				} else if ((blocks[i] == blocks[j]) && (labels[i] != labels[j])) {
					sd++;
				} else if ((blocks[i] != blocks[j]) && (labels[i] == labels[j])) {
					ds++;
				} else {
					dd++;
				}// Of if
			}// Of for j
		}// Of for i
		System.out.println("ss = " + ss + "sd = " + sd + "ds = " + ds + "dd = "
				+ dd);

		jc = (ss + 0.0) / (ss + sd + ds);
		fmi = (ss + 0.0) / Math.sqrt((ss + ds) * (ss + sd));
		ri = 2.0 * (ss + dd) / (blocks.length * (blocks.length - 1));
	}// Of computExternalMeasures

	/**
	 *************** 
	 * Build the measure.
	 **************** 
	 */
	public ClusterMeasure(int[] paraP, double[][] paraR, double[][] paraC) {
		blocks = paraP;
		attributes = paraR;
		centers = paraC;
		
	//	System.out.println("Block Information is : " + Arrays.toString(blocks));
	//	System.out.println("spectral attributes are : " + Arrays.deepToString(attributes));
	//	System.out.println("centers are : " + Arrays.deepToString(centers));
		
		
		//Compute number of blocks
		numBlocks = maximal(blocks)  + 1;
		
		//save block information
		computeBlockSet();
		
	}// Of the constructor

	/**
	 *************** 
	 * Compute the block set.
	 **************** 
	 */
	public int [][] computeBlockSet() {
		int[][] tempSet = new int[numBlocks][blocks.length];
		int[] tempBlockSizes = new int[numBlocks];
		for (int i = 0; i < blocks.length; i++) {
			tempSet[blocks[i]][tempBlockSizes[blocks[i]]] = i;
			tempBlockSizes[blocks[i]] ++;
		}//Of for i
		
		//Compress
		blockSet = new int[numBlocks][];
		for (int i = 0; i < numBlocks; i++) {
			blockSet[i] = Arrays.copyOf(tempSet[i], tempBlockSizes[i]);
		}//Of for i
		
		return blockSet;
	}//Of computeBlockSet
	
	/**
	 *************** 
	 * acquir max value.
	 **************** 
	 */
	public int maximal(int[] paraArray) {
		int tempMaximal = Integer.MIN_VALUE;
		for (int i = 0; i < paraArray.length; i++) {
			if (tempMaximal < paraArray[i]) {
				tempMaximal = paraArray[i];
			}// Of if
		}// Of for i

		return tempMaximal;
	}// Of maximal

	/**
	 *************** 
	 * acquir max value.
	 **************** 
	 */
	public double maximal(double[] paraArray) {
		double tempMaximal = Double.MIN_VALUE;
		for (int i = 0; i < paraArray.length; i++) {
			if (tempMaximal < paraArray[i]) {
				tempMaximal = paraArray[i];
			}// Of if
		}// Of for i

		return tempMaximal;
	}// Of maximal
	
	/**
	 *************** 
	 * Build the measure.
	 **************** 
	 */
	public double computPurity() {
		// Scan to determine the size of the distribution matrix
		distributionMatrix = new int[maximal(blocks) + 1][maximal(labels) + 1];

		// Fill the matrix
		for (int i = 0; i < blocks.length; i++) {
			distributionMatrix[blocks[i]][labels[i]]++;
		}// Of for i

		double tempPurity = 0;
		for (int i = 0; i < distributionMatrix.length; i++) {
			tempPurity += maximal(distributionMatrix[i]);
		}// Of for i

		purity = tempPurity / blocks.length;

		return purity;
	}// Of computPurity

	/**
	 *************** 
	 * Entropy.
	 **************** 
	 */
	public double entropy(int[] paraArray) {
		double tempEntropy = 0;
		int[] tempCount = new int[maximal(paraArray) + 1];
		double tempP = 0;
		
	    for (int i = 0; i < maximal(paraArray) + 1; i++) {
	    	for(int j = 0; j < paraArray.length; j++){
	    		if(paraArray[j] == i){
	    			tempCount[i]++;	    			
	    		}//Of if    	
	    	}//Of for j			
	    	tempP = (tempCount[i]++ *1.0)/ paraArray.length;
	        tempEntropy += -tempP*(1.0)* (Math.log(tempP)/Math.log(2));
//	        System.out.println("The "+i+" , entropy = " + tempEntropy + " p = " + tempP);
		}//Of for i

		return tempEntropy;
	}//Of entropy
	
	/**
	 *************** 
	 * Build the measure.
	 **************** 
	 */
	public void computeNmi() {
		nmi = 0;
		double[] tempRowSum = new double[distributionMatrix.length];
		double[] tempColumnSum = new double[distributionMatrix[0].length];
		double tempBlockH = 0;
		double tempLableH = 0;
		
		
		for (int i = 0; i < distributionMatrix.length; i++) {
			for (int j = 0; j < distributionMatrix[0].length; j++) {
				tempRowSum[i] += distributionMatrix[i][j];
				tempColumnSum[j] += distributionMatrix[i][j];
			}// Of for j
		}// Of for i

		double tempMutualInformation = 0;
		double tempValue;
		for (int i = 0; i < distributionMatrix.length; i++) {
			for (int j = 0; j < distributionMatrix[0].length; j++) {
				if (distributionMatrix[i][j] == 0) {
					continue;
				}// Of if

				tempValue = (distributionMatrix[i][j] + 0.0)
						/ labels.length
						* Math.log(labels.length * distributionMatrix[i][j]
								/ (tempRowSum[i] * tempColumnSum[j]));
				tempMutualInformation += tempValue;
			}// Of for j
		}// Of for i
		
		
		tempBlockH = entropy(blocks);
		tempLableH = entropy(labels);
		
		nmi = tempMutualInformation / Math.sqrt(tempBlockH * tempLableH);
	}// Of computeNmi

	/**
	 *************** 
	 * Get the JC measure.
	 **************** 
	 */
	public double getJc() {
		return jc;
	}// Of getJc

	/**
	 *************** 
	 * Get the JC measure.
	 **************** 
	 */
	public double getPurity() {
		return purity;
	}// Of getPurity

	/**
	 *************** 
	 * Get the FMI measure.
	 **************** 
	 */
	public double getFmi() {
		return fmi;
	}// Of getFmi

	/**
	 *************** 
	 * Get the RI measure.
	 **************** 
	 */
	public double getRi() {
		return ri;
	}// Of getRi
	
	/**
	 *************** 
	 * Get the NMI measure.
	 **************** 
	 */
	public double getNmi() {
		return nmi;
	}// Of getRi
	
	/**
	 *************** 
	 * Get the dbi measure.
	 **************** 
	 */
	public double getDbi() {
		return dbi;
	}// Of getDbi
	
	/**
	 *************** 
	 * Get the DI measure.
	 **************** 
	 */
	public double getDi() {
		return di;
	}// Of getDi
	
	/**
	 *************** 
	 * Compute the DBI. value high is better.
	 **************** 
	 */
	public void computeDbi() {
		
        double tempDis = 0;	
		double[] tempArray = new double[numBlocks];
		
		computeAverageDistance();
		computeCenterDistance();
			
		for(int i = 0 ; i < numBlocks; i++){
			for(int j = 0; j < numBlocks; j++){
				if(i == j){
					continue;
				}//Of if
				
				else{
					tempArray[j] = (averageDistances[i] + averageDistances[j])
							/ centerDistances[i][j];
			//		System.out.println("arryay[ "+j+" ] = " + tempArray[j]);
				}//Of else			
			}//Of for j
			
			tempDis += maximal(tempArray);
		//	System.out.println("Dis is " + tempDis);
		}//Of for i
		
		dbi = tempDis / numBlocks;
	
	}//Of computeDbi
	
	/**
	 *************** 
	 * Compute the DI. value low is better.
	 **************** 
	 */
	public void computeDi() {
		double maxDiameter;
		double tempDis = 0;
		double tempDi = Double.MAX_VALUE;	
		
		computeDiameter();
		computeMinDistances();
		
		maxDiameter = maximal(diameters);
		
		for(int i = 0; i < numBlocks; i++){
			for(int j = 0; j < numBlocks; j++){			
				if(i == j){
					continue;
				}//Of if
				
				else{
				    tempDis = minDistances[i][j] / maxDiameter;
				}
			//	System.out.println("block - "+ i + " , " + j +" , distance is " + tempDis);
			//	System.out.println("before : Di = " + tempDi);
				if(tempDis < tempDi){
					tempDi = tempDis;
				}//Of 
			//	System.out.println("after : Di = " + tempDi);
			}//Of for j
			
		}//Of for i
		
		di = tempDi;
	}//Of computeDi
	
	
	/**
	 *************** 
	 * Compute the Average Distance of each cluster.
	 **************** 
	 */
	public void computeAverageDistance() {
		double tempDistance;
		int tempCount;
		averageDistances = new double[numBlocks];

//		System.out.println("************** Average Distance *****************");
		for (int i = 0; i < numBlocks; i++) {
			tempDistance = 0;
			tempCount = 0;
			for (int j = 0; j < blockSet[i].length - 1; j++) {
				for (int k = j + 1; k < blockSet[i].length; k++) {
					tempDistance += Cluster.manhattanDistance(
							attributes[blockSet[i][j]], attributes[blockSet[i][k]]);
					tempCount++;
//					System.out.println(" point pair are : " + blockSet[i][j] +" , "+ blockSet[i][k]);
//					System.out.println("block - " + i +" , distance = " + tempDistance + " , count = " + tempCount);
					
				} // Of for k
			} // Of for j
			averageDistances[i] = tempDistance / tempCount;
	//  	System.out.println("block - " + i + " averageDis is " + averageDistances[i]);
		} // Of for i

	}// Of computeAverageDis

	/**
	 *************** 
	 * Compute the distance of each center.
	 **************** 
	 */
	public void computeCenterDistance(){

		centerDistances = new double[numBlocks][numBlocks];
	//	System.out.println("************** Center Distance *****************");
		
		for(int i = 0; i < numBlocks - 1; i++){
			for(int j = i + 1; j < numBlocks; j++){
				
					centerDistances[i][j] = Cluster.manhattanDistance(
							centers[i], centers[j]);					
					centerDistances[j][i] = centerDistances[i][j];	
					
				//	System.out.println("the distance of "+i+" , and "+ j+" is " + centerDistances[i][j]);
				//	System.out.println("the distance of "+j+" , and "+ i+" is " + centerDistances[j][i]);
			}//Of for j		
		}//OF for i		
		
	}//Of computeAverageDistance
		
	/**
	 *************** 
	 * Compute the diameter.
	 **************** 
	 */
	public void computeDiameter() {		
		double tempDiameter;
		double tempDistance = 0;
		
		diameters = new double[numBlocks];
	//	System.out.println("************ Diameter ******************");
		for(int i = 0 ; i < numBlocks; i++){
			tempDiameter = 0;
			for(int j = 0; j < blockSet[i].length - 1; j++){
				for(int k = j + 1; k < blockSet[i].length; k++){
					tempDistance = Cluster.manhattanDistance(
							attributes[blockSet[i][j]], attributes[blockSet[i][k]]);
				
		//			System.out.println(" point pair are : " + blockSet[i][j] +" , "+ blockSet[i][k]);
    	//			System.out.println("block - " + i +" , distance = " + tempDistance);	
    	//			System.out.println("before : diameter = " + tempDiameter);
					if (tempDiameter < tempDistance) {
						tempDiameter = tempDistance;
					}//Of if
		//			System.out.println("after : diameter = " + tempDiameter);
				}//Of for k				
			}//OF for j
			diameters[i] = tempDiameter;
		//	System.out.println("diameter[" + i +"] = " + diameters[i]);
		}//Of for i

	}//Of computeDiameter

	/**
	 *************** 
	 * Compute the minimal distance between clusters.
	 **************** 
	 */
	public void computeMinDistances() {
		minDistances = new double[numBlocks][numBlocks];
		double tempDistance = 0;
		double minDistance;
	//	System.out.println("************ Min Distance ******************");
		for(int i = 0; i < numBlocks - 1; i++){
			for(int j = i + 1; j < numBlocks; j++){
				minDistance = Double.MAX_VALUE;
				
				for(int k = 0; k < blockSet[i].length; k++){
					for(int l = 0; l < blockSet[j].length; l++){
						tempDistance = Cluster.manhattanDistance(
								attributes[blockSet[i][k]], attributes[blockSet[j][l]]);
						
//						System.out.println(" point pair are : " + blockSet[i][k] +" , "+ blockSet[j][l]);
//				    	System.out.println("block - " + i +" to block - " + j +" , distance = " + tempDistance);
//						System.out.println("before : minDistance = " + minDistance);
						
						if (minDistance > tempDistance) {
							minDistance = tempDistance;
						}//Of if
				//		System.out.println("after : minDistance = " + minDistance);
					}//Of for l
				}//Of for k
				
				minDistances[i][j] = minDistance;	
				minDistances[j][i] = minDistance;
		//		System.out.println("minDistances[" + i + "][" + j +"] = " + minDistances[i][j]);
		//		System.out.println("minDistances[" + j + "][" + i +"] = " + minDistances[j][i]);
				
			}//Of for j
		}//Of for i
		
	}//Of computeMinDistances
	
	
}// Of class ClusterMeasure
