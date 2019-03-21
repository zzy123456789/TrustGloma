package DSDP;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import datamodel.DataInfo;

/**
 * Training Data information for user and item.<br>
 * @author ziyin
 */

public class UserDSDP extends Cluster{
 

	/*
	 * number of centers
	 */
	int numClusters;
	
	/*
	 * Is the cluster information changed ?
	 */
	boolean clusterChanged;
	
	
	public UserDSDP(double paraP) throws IOException, Exception {
		// TODO Auto-generated constructor stub
		super(paraP);
		userBlockInformation = new int[DataInfo.userNumber];
		for (int i = 0; i < userBlockInformation.length; i++) {
			userBlockInformation[i] = -1;
		}//Of for i
	}//Of the first constructor


	public int[] randomSwapIndices(int paraLength){
		int[] tempIndices = new int[paraLength];
		
		for(int i = 0; i < tempIndices.length; i ++){
			tempIndices[i] = i;
		}//Of for i 
		
		Random tempRandom = new Random();
		int tempIndex1, tempIndex2, tempValue;
		
		for(int i = 0; i < 100; i++){
			tempIndex1 = tempRandom.nextInt(paraLength);
			tempIndex2 = tempRandom.nextInt(paraLength);
			
			tempValue = tempIndices[tempIndex1];
			tempIndices[tempIndex1] = tempIndices[tempIndex2];
			tempIndices[tempIndex2] = tempValue;
		}//Of for i
		
		return tempIndices;
	}//Of randomSwapIndices
	
	
	public void randomSelectCenter(){
		int[] tempIndices = randomSwapIndices(DataInfo.userNumber);
		
		for (int i = 0; i < numClusters; i++) {
			for (int j = 0; j < ClusterDataInfo.CHAN_LEN; j++) {
				centers[i][j] = ClusterDataInfo.userTraChanRate[tempIndices[i]][j];
					
			} // Of for j
		//	System.out.println("The original centers are "+ Arrays.toString(centers[i]));	
		} // Of for i
		
	}//Of randomSelectCenter
	
	
	public void clusterUsingCenters(){
		
		for(int i = 0; i < DataInfo.userNumber; i++){
			int tempIndex = 0;
			double tempUserDis = 0;
			double tempDistance = Double.MAX_VALUE;
		
			for(int j = 0; j < numClusters; j++){			
				tempUserDis = KLDivergenceDistance(ClusterDataInfo.userTraChanRate[i], centers[j]);
		
				if(tempUserDis < tempDistance){
					tempDistance = tempUserDis;
					tempIndex = j;
				}//Of if
			}//Of for j
		//	System.out.println(" The KL distance is " + tempDistance);
			
			if(userBlockInformation[i] != tempIndex){
				userBlockInformation[i] = tempIndex;
				clusterChanged = true;
			}//Of if
			
		}//Of for i
		
		
	}//Of clusterUsingCenters
	
	/*
	 * Compute new centers using the mean value of each block.
	 */
	private void meanAsCenters() {
		double[][] tempSums = new double[numClusters][ClusterDataInfo.CHAN_LEN];
	    double[] tempCenterSum = new double[numClusters];
		
		//1.Initialize
	    userBlockSizes = new int[numClusters];
		for(int i = 0 ; i < numClusters; i++){
			userBlockSizes[i] = 0;
			for(int j = 0; j < centers[i].length; j++){
				tempSums[i][j] = 0;
			}//Of for j
		}//Of for i
		
		//2.Scan once to obtain the total and sum
		for(int i = 0; i < DataInfo.userNumber; i ++){
			userBlockSizes[userBlockInformation[i]]++;
			for(int j = 0; j < ClusterDataInfo.CHAN_LEN; j++){
				tempSums[userBlockInformation[i]][j] += ClusterDataInfo.userTraChannels[i][j];
				tempCenterSum[userBlockInformation[i]] += ClusterDataInfo.userTraChannels[i][j];			
			}//OF for j
		}//Of for i
		
		//3.divide
		for(int i = 0; i < numClusters; i++){
			for(int j = 0; j < ClusterDataInfo.CHAN_LEN; j++){
			
			//	System.out.println("The center sum of " + i +" , rating = "+ j + " is " + tempSums[i][j]);		
			//	System.out.println("The all center sum of " + i + " is " + tempCenterSum[i]);				
				
				centers[i][j] = tempSums[i][j] / tempCenterSum[i];
				
			//	System.out.println("The new center of "+i+" is " + centers[i][j] +"\n");
			
			}//Of for j
		}//Of for i
		
		
	}//Of meanAsCenters
	
	
	/*
	 * cluster
	 */
	public void cluster(int paraK){
	
		
		numClusters = paraK;
		centers = new double[numClusters][ClusterDataInfo.CHAN_LEN];
		
		clusterChanged = true;		
		
		//1. random as the original center
		randomSelectCenter();
		
		//2.Cluster and mean
		while(true){
			clusterChanged = false;
			
			//2.1 Cluster
			clusterUsingCenters();
			
			if(!clusterChanged){
				
				break;
			}//Of if
			
			//Mean
			meanAsCenters();
			
		
		}//Of while		
		
	}//Of cluster
	



	public static void userCluster() throws Exception{
		
		ClusterDataInfo.buildDataModel();
		int tempGroupNum = 5;
		UserDSDP tempUserDSDP = new UserDSDP(tempGroupNum);
		
		tempUserDSDP.cluster(tempGroupNum);
		
	//	System.out.println("The user-cluster blockInformation is :\n" +Arrays.toString(tempUserDSDP.userBlockInformation));
		System.out.println("The user-cluster blockSize is :\n" + Arrays.toString(tempUserDSDP.userBlockSizes));
	
		System.out.println("Start to compute cluster measure !");
		ClusterMeasure tempMeasure = new ClusterMeasure(tempUserDSDP.userBlockInformation, ClusterDataInfo.userTraChanRate, tempUserDSDP.centers);
			
	    tempMeasure.computeDbi();
		tempMeasure.computeDi();
		System.out.println("The DBI is : " + tempMeasure.getDbi());
		System.out.println("The DI is : " + tempMeasure.getDi());
	}//Of test
	
	
	public static void main(String[] args) {
		try {
			System.out.println("UserDSDP test is starting ��");
			userCluster();
			System.out.println("UserDSDP test is end ��");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}//Of UserDSDP
