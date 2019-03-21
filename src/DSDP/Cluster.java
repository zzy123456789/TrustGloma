package DSDP;

import java.util.Arrays;

import datamodel.DataInfo;

/**
 * The general clustering algorithm.
 * @author ziyin
 * 2017/9/18 20:21
 */
public class Cluster {

	/*
	 * Cluster information of user
	 */
	public static int[] userBlockInformation;
	
	/*
	 * Cluster information of item
	 */
	public static int[] itemBlockInformation;
	
	/*
	 * Class Labels
	 */
	int[] classLabels;
	
	/*
	 * Centers
	 */
	double[][] centers;
	
	/*
	 * The size of each user block
	 */
	public static int[] userBlockSizes;
	
	/*
	 * The size of each user block
	 */
	public static int[] itemBlockSizes;
	
	/*
	 * The diameter of the data
	 */
	double diameter;
	
	/*
	 * Distance measure
	 */
	double p;
	
	public Cluster(double paraP) {
		// TODO Auto-generated constructor stub
		p = paraP;
	}

	/*
	 * compute the distance
	 */
	public double KLDivergenceDistance(double[] paraRate, double[] centers){
		
		double tempDistance = 0;
		
		int tempLen = paraRate.length;
	
		//Ϊ0˵�����ֲַ������鳤��Ϊ0����û�����֣��������֮��ľ��뿴���ܴ�
		//ʵ���Ϸֲ����ȶ���5
		if(tempLen == 0){
			return 9999;
		}//of if
		
	//	System.out.println("The itemRate is " + Arrays.toString(paraRate));
	//	System.out.println("The centers is " + Arrays.toString(centers));
		for(int i = 0; i < ClusterDataInfo.CHAN_LEN; i++){
			//&& (paraRate[i] / centers[i] >= 1) 
			if((paraRate[i] != 0) && (centers[i] != 0) ){
	//			System.out.println("The distance is " + tempDistance);
				tempDistance += paraRate[i] * Math.log(paraRate[i] / centers[i]);
			    
			}//Of if
		}//Of for i
		
	// 	System.out.println("The final distance is " + tempDistance);
		return tempDistance;
		
	}//Of KLDivergenceDistance
	
	
	/*
	 * compute the distance
	 */
	public double KLDivergenceSymDistance(double[] paraRate, double[] centers){
		
		double tempOneDistance = 0;	
		double tempTwoDistance = 0;
		double tempFinalDistance = 0;
	
		
	//	System.out.println("The itemRate is " + Arrays.toString(paraRate));
	//	System.out.println("The centers is " + Arrays.toString(centers));
		
		//1. first distance
		for(int i = 0; i < ClusterDataInfo.CHAN_LEN; i++){
			//&& (paraRate[i] / centers[i] >= 1) 
			if((paraRate[i] != 0) && (centers[i] != 0) ){
				tempOneDistance += paraRate[i] * Math.log(paraRate[i] / centers[i]);
			    
			}//Of if
		}//Of for i
	//	System.out.println("The first distance is " + tempOneDistance);
		
		//2. second distance
		for(int i = 0; i < ClusterDataInfo.CHAN_LEN; i++){
			//&& (paraRate[i] / centers[i] >= 1) 
			if((paraRate[i] != 0) && (centers[i] != 0) ){
	//			System.out.println("The distance is " + tempDistance);
				tempTwoDistance += centers[i] * Math.log(centers[i] / paraRate[i]);
			}//Of if
		}//Of for i
	//	System.out.println("The second distance is " + tempTwoDistance);
		
		tempFinalDistance = (tempOneDistance + tempTwoDistance) / 2;
		
	// 	System.out.println("The final distance is " + tempFinalDistance);
		return tempFinalDistance;
		
	}//Of KLDivergenceSymDistance
	
	
	/**
	 *************** 
	 * Compute the distance.
	 **************** 
	 */
	public static double manhattanDistance(double[] paraOldCenters, double[] paraNewCenters) {
		double tempDistance = 0;

		int tempILen = paraOldCenters.length;
		
		for(int i = 0; i < tempILen; i ++){
			tempDistance += Math.abs(paraOldCenters[i] - paraNewCenters[i]);
		}//Of for i
		
		return tempDistance / tempILen;
	}// Of distance
	
	
	
	
}
