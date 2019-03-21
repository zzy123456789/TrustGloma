package SpectralCluster;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import tool.Eig;
import tool.SimpleTool;

/**
 * 
 * @author ziyin
 *
 */
public class SpectralRatingUser extends Cluster {
	public static String GroupPath = new String("src/data/GroupUserFilm.txt");
	
	// Number of graphs
	int k1;	
	
	// Number of centers
	int k;
	
	//Is the cluster information changed?
	boolean clusterChanged;
	
	//after spectral  new matrix
	double[][] usersSpectral;
	
	//
	double[][] oldcenters;

	/**
	 *************** 
	 * @param paraReader
	 * @throws IOException
	 * @throws Exception
	 *************** 
	 */
	public SpectralRatingUser() throws IOException, Exception {
		super();
		blockInformation = new int[ClusterDataInfo.rUserNum];
		for (int i = 0; i < blockInformation.length; i++) {
			blockInformation[i] = -1;
		} // Of for i
	}// Of the first constructor

	
	/**
	 *************** 
	 * dimension reduction
	 **************** 
	 */
	public void cutGraph(int parak1) {
		k1 = parak1;
		
		//1.ratioCut
		//RatioCut();
		
		//2.Ncut
		NCut();
		
	//	System.out.println("after spectral, new matrix is " + Arrays.deepToString(usersSpectral));
	}//Of cutGraph
	

	/*
	 * input: L
	 */
	public void RatioCut() {
		
		usersSpectral = new double[ClusterDataInfo.rUserNum][k1];
		Eig[] tempEig = new Eig[ClusterDataInfo.rUserNum];
		
		//1.input L , get eig
		tempEig = EigMatrix.eig(ClusterDataInfo.userLaplas);
		for(int i = 0; i < k1; i++){
			System.out.println("The eigVector is " + Arrays.toString(tempEig[i].eigVector));
		}//Of for i
		
		//2. put value in new matrix
		for(int i = 0 ; i < k1; i++ ){
			for(int j = 0; j < ClusterDataInfo.rUserNum; j++){				
				usersSpectral[j][i] = tempEig[i].eigVector[j];				
			}//Of for j
		}//Of for i
		
	}//Of RatioCut
	
	/*
	 * input : D(-1/2) L D(-1/2)
	 */
	public void NCut() {
		
		double[][] normalD = new double[ClusterDataInfo.rUserNum][ClusterDataInfo.rUserNum];
		double[][] normalL = new double[ClusterDataInfo.rUserNum][ClusterDataInfo.rUserNum];
				
		usersSpectral = new double[ClusterDataInfo.rUserNum][k1];
		Eig[] tempEig = new Eig[ClusterDataInfo.rUserNum];
		
		//1.get new D, L
		normalD = ClusterDataInfo.getDegreeMatrix(ClusterDataInfo.rUserNum, ClusterDataInfo.userTotalDegree);
		normalL = EigMatrix.computeNewL(normalD, ClusterDataInfo.userLaplas);
		
		//2. input new L, get eig
		tempEig = EigMatrix.eig(normalL);
		
		//3. put value in new matrix
		for(int i = 0 ; i < k1; i++ ){
			for(int j = 0; j < ClusterDataInfo.rUserNum; j++){				
				usersSpectral[j][i] = tempEig[i].eigVector[j];				
			}//Of for j
		}//Of for i	
		
	}//Of NCut


	/**
	 *************** 
	 * Swap the elements of an array randomly.
	 **************** 
	 */
	public int[] randomSwapIndices(int paraLength) {
		int[] tempIndices = new int[paraLength];
		for (int i = 0; i < tempIndices.length; i++) {
			tempIndices[i] = i;
		} // Of for i

		Random tempRandom = new Random();
		int tempIndex1, tempIndex2, tempValue;

		for (int i = 0; i < 100; i++) {
			tempIndex1 = tempRandom.nextInt(paraLength);
			tempIndex2 = tempRandom.nextInt(paraLength);

			tempValue = tempIndices[tempIndex1];
			tempIndices[tempIndex1] = tempIndices[tempIndex2];
			tempIndices[tempIndex2] = tempValue;
		} // Of for i

		return tempIndices;
	}// Of randomSwap

	/**
	 *************** 
	 * Cluster.
	 **************** 
	 */
	public void randomSelectCenters() {
		int[] tempIndices = randomSwapIndices(ClusterDataInfo.rUserNum);

		for (int i = 0; i < k; i++) {
			for (int j = 0; j < k1; j++) {
				centers[i][j] = usersSpectral[tempIndices[i]][j];
			} // Of for j
		} // Of for i
	}// Of randomSelectCenters

	/**
	 *************** 
	 * clusterUsingCenters.
	 **************** 
	 */
	public void clusterUsingCenters() {
		for (int i = 0; i < ClusterDataInfo.rUserNum; i++) {
			int tempIndex = 0;
			double tempDistance = Double.MAX_VALUE;
			// ÿһ��ʵ�������������Ľ��м�����룬����ʵ�����ൽ������С������
			for (int j = 0; j < centers.length; j++) {
				
				double tempUserDist = manhattanDistance(usersSpectral[i], centers[j]);
			//	System.out.println("tempUserDist: " + tempUserDist);
				if (tempUserDist < tempDistance) {
					tempDistance = tempUserDist;
					tempIndex = j;
				} // Of if
			} // Of for j

			if (blockInformation[i] != tempIndex) {
				clusterChanged = true;
				blockInformation[i] = tempIndex;
			} // Of if
	
		} // Of for i
	}// Of clusterUsingCenters

	/**
	 *************** 
	 * Compute new centers using the mean value of each block.
	 **************** 
	 */
	public void meanAsCenters() {
		// Initialize
		oldcenters = new double[k][k1];
		blockSizes = new int[k];
		for (int i = 0; i < centers.length; i++) {
			blockSizes[i] = 0;
			for (int j = 0; j < centers[i].length; j++) {
				oldcenters[i][j] = centers[i][j];
				centers[i][j] = 0;
			} // Of for j
		} // Of for i

		// Scan all instances and sum
		for (int i = 0; i < ClusterDataInfo.rUserNum; i++) {
			blockSizes[blockInformation[i]]++;
			for (int j = 0; j < k1; j++) {
				centers[blockInformation[i]][j] += usersSpectral[i][j];
			} // Of for j
		} // Of for i

		// Divide
		for (int i = 0; i < centers.length; i++) {
			for (int j = 0; j < centers[i].length; j++) {
				centers[i][j] /= blockSizes[i];
			} // Of for j
		} // Of for i
	}// Of meanAsCenters

	/**
	 *************** 
	 * Compute the distance between the old and new centers.
	 **************** 
	 */
	public double diffOfOldAndNewCenters() {
		// Compute the distance between the old and new centers
		double tempMax = 0;
		for (int i = 0; i < centers.length; i++) {
			
			double tempDistance = manhattanDistance(oldcenters[i], centers[i]);
			if (tempDistance > tempMax) {
				tempMax = tempDistance;
			} // Of if
		} // Of for i

		System.out.println("tempDistance: " + tempMax);
		return tempMax;
	}//of diffOfOldAndNewCenters

	/**
	 *************** 
	 * Cluster.
	 **************** 
	 */
	public void cluster(int paraK) {
		// Initialize
		k = paraK;
		centers = new double[k][k1];
		clusterChanged = true;

		// Select centers
		randomSelectCenters();
		//SimpleTool.printMatrix(centers);
		
		// Cluster and mean
		while (true) {
			clusterChanged = false;
			
			// Cluster
			clusterUsingCenters();
			

			if(!clusterChanged){
				break;
			}//Of if

			// Mean
			meanAsCenters();
			

			//System.out.println(Arrays.toString(blockInformation));
		}// Of while
	}// Of cluster

	/**
	 *************** 
	 * Test the program
	 **************** 
	 */
	public static void test() throws Exception {
		//1.bulid data
		ClusterDataInfo tempSoc = new ClusterDataInfo();
		tempSoc.buildUserDataModel();		
		System.out.println("bulid dataModel !");
		
		SpectralRatingUser tempSpectral = new SpectralRatingUser();
	//	System.out.println(tempSpectral);

		//2.spectral cluster to dimension reduction
		tempSpectral.cutGraph(2);
		System.out.println("cut is done !");
		
		//3.k-means cluster for new matrix
		int tempGroupNum = 5;
		tempSpectral.cluster(tempGroupNum);
	//	System.out.println(Arrays.toString(tempSpectral.blockInformation));
	 	System.out.println(Arrays.toString(tempSpectral.blockSizes));
		
		OutputGroupFile.outToGrpFile(GroupPath, tempGroupNum, tempSpectral.blockInformation);
		System.out.println("Start to compute cluster measure !");
		ClusterMeasure tempMeasure = new ClusterMeasure(tempSpectral.blockInformation, tempSpectral.usersSpectral, tempSpectral.centers);
			
	    tempMeasure.computeDbi();
		tempMeasure.computeDi();
		System.out.println("The DBI is : " + tempMeasure.getDbi());
		System.out.println("The DI is : " + tempMeasure.getDi());
	}// Of test


	/**
	 * The main function.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			test();
		} catch (Exception ee) {
			ee.printStackTrace();
		} // Of try
	}// Of main

}// Of class KMeans
