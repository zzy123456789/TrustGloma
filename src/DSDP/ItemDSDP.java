package DSDP;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import datamodel.DataInfo;

/**
 * Training Data information for item and item.
 * @author ziyin
 */

public class ItemDSDP extends Cluster{
 

	/*
	 * number of centers
	 */
	int numClusters;
	
	/*
	 * Is the cluster information changed ?
	 */
	boolean clusterChanged;
	
	
	public ItemDSDP(double paraP) throws IOException, Exception {
		// TODO Auto-generated constructor stub
		super(paraP);
		itemBlockInformation = new int[DataInfo.itemNumber];
		for (int i = 0; i < itemBlockInformation.length; i++) {
			itemBlockInformation[i] = -1;
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
		int[] tempIndices = randomSwapIndices(DataInfo.itemNumber);
		
		for (int i = 0; i < numClusters; i++) {
			for (int j = 0; j < ClusterDataInfo.CHAN_LEN; j++) {
				centers[i][j] = ClusterDataInfo.itemTraChanRate[tempIndices[i]][j];
					
			} // Of for j
		//	System.out.println("The original centers are "+ Arrays.toString(centers[i]));	
		} // Of for i
		
	}//Of randomSelectCenter
	
	
	public void clusterUsingCenters(){
		
		for(int i = 0; i < DataInfo.itemNumber; i++){
			int tempIndex = 0;
			double tempItemDis = 0;
			double tempDistance = Double.MAX_VALUE;
	
			for(int j = 0; j < numClusters; j++){
				
				tempItemDis = KLDivergenceDistance(ClusterDataInfo.itemTraChanRate[i], centers[j]);
		
				if(tempItemDis < tempDistance){
					tempDistance = tempItemDis;
					tempIndex = j;
				}//Of if
			}//Of for j
		//	System.out.println(" The min KL distance is " + tempDistance);
			
			if(itemBlockInformation[i] != tempIndex){
				itemBlockInformation[i] = tempIndex;
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
	    itemBlockSizes = new int[numClusters];
		for(int i = 0 ; i < numClusters; i++){
			itemBlockSizes[i] = 0;
			for(int j = 0; j < centers[i].length; j++){
				tempSums[i][j] = 0;
			}//Of for j
		}//Of for i
		
		//2.Scan once to obtain the total and sum
		for(int i = 0; i < DataInfo.itemNumber; i ++){
			itemBlockSizes[itemBlockInformation[i]]++;
			for(int j = 0; j < ClusterDataInfo.CHAN_LEN; j++){
				tempSums[itemBlockInformation[i]][j] += ClusterDataInfo.itemTraChannels[i][j];
				tempCenterSum[itemBlockInformation[i]] += ClusterDataInfo.itemTraChannels[i][j];
			}//OF for j
		}//Of for i
		
		//3.divide
		for(int i = 0; i < numClusters; i++){
			for(int j = 0; j < ClusterDataInfo.CHAN_LEN; j++){
			
		//		System.out.println("The center sum of " + i +" , rating = "+ j + " is " + tempSums[i][j]);		
		//		System.out.println("The all center sum of " + i + " is " + tempCenterSum[i]);				
				
				centers[i][j] = tempSums[i][j] / tempCenterSum[i];
				
		//		System.out.println("The new center of "+i+" is " + centers[i][j] +"\n");
			
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
	



	public static void itemCluster() throws Exception{
		
		ClusterDataInfo.buildDataModel();
		
		int tempGroupNum = 5;
		ItemDSDP tempItemDSDP = new ItemDSDP(tempGroupNum);
		
		tempItemDSDP.cluster(tempGroupNum);
		
	//	System.out.println("The item-cluster blockInformation is :\n" +Arrays.toString(tempItemDSDP.itemBlockInformation));
		System.out.println("The item-cluster blockSize is :\n" + Arrays.toString(tempItemDSDP.itemBlockSizes));
	
		System.out.println("Start to compute cluster measure !");
		ClusterMeasure tempMeasure = new ClusterMeasure(tempItemDSDP.itemBlockInformation, ClusterDataInfo.itemTraChanRate, tempItemDSDP.centers);
			
	    tempMeasure.computeDbi();
		tempMeasure.computeDi();
		System.out.println("The DBI is : " + tempMeasure.getDbi());
		System.out.println("The DI is : " + tempMeasure.getDi());
	}//Of ItemCluster
	
	
	public static void main(String[] args) {
		try {
			System.out.println("ItemDSDP test is starting ��");
			itemCluster();
			System.out.println("ItemDSDP test is end ��");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}//Of ItemDSDP
