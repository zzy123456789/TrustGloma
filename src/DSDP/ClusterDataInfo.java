package DSDP;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Reader;

import datamodel.DataInfo;


/**
 * Training Data information for user and item.
 * @author ziyin
 */
@SuppressWarnings("serial")
public class ClusterDataInfo{
	
	public final static int userNum = 1508;//1508//10
	public final static int itemNum = 2071;//2071//10
	public final static int CHAN_LEN = 4;//4//5

	/*
	 * User training set
	 */
	public static int[][] userTraRatings;// The rating matrix for user training
	public static int[][] userTraRateInds;// The rating indices for user training
	public static int[] userTraDegree;// The degrees for user training
	public static double[] userTraTolRating;// The total ratings for user training
	public static double[] userTraAveRating;// The average ratings for user training
	public static double[][] userTraChannels = new double[DataInfo.userNumber][CHAN_LEN]; 
	public static double[][] userTraChanRate = new double[DataInfo.userNumber][CHAN_LEN];
	public static int[][] userTrRateInds = new int[DataInfo.userNumber][CHAN_LEN];
	/*
	 * Item training set
	 */
	public static int[][] itemTraRatings;// The rating matrix for item training
	public static int[][] itemTraRateInds;// The rating indices for item training
	public static int[] itemTraDegree;// The degrees for item training
	public static double[] itemTraTolRating;// The total ratings for item training
	public static double[] itemTraAveRating;// The average ratings for item training
	public static double[][] itemTraChannels = new double[DataInfo.itemNumber][CHAN_LEN]; 
	public static double[][] itemTraChanRate = new double[DataInfo.itemNumber][CHAN_LEN];
	public static int[][] itemTrRateInds = new int[DataInfo.itemNumber][CHAN_LEN];

	/*
	 * set user training set
	 */
	public static void setUserTrainSet(String paraFileName) throws Exception{
		File tempFile = null;
		String tempString = null;
		String[] tempStrArray = null;

		// Compute values of arrays
		tempFile = new File(paraFileName);
		if (!tempFile.exists()) {
			System.out.println("File is not exist!");
			return;
		}// Of if

		RandomAccessFile tempRanFile = new RandomAccessFile(tempFile, "r");
		// ���ļ�����ʼλ��
		int tempBeginIndex = 0;
		// �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
		tempRanFile.seek(tempBeginIndex);

		// Step 1. count the item degree
		int tempUserIndex = 0;
		int tempItemIndex = 0;
		double tempRating = 0;
		
		userTraDegree = new int[userNum];
		userTraRatings = new int[userNum][];
		userTrRateInds = new int[userNum][];
		userTraTolRating = new double[userNum];
		userTraAveRating = new double[userNum];
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUserIndex = Integer.parseInt(tempStrArray[0]) - 1;

			userTraDegree[tempUserIndex] ++;
		}// Of while
		
		for(int i = 0; i < userTraDegree.length; i ++){
			userTraRatings[i] = new int[userTraDegree[i]];
			userTrRateInds[i] = new int[userTraDegree[i]];
			
			if(userTraDegree[i] > 1e-6){
				userTraAveRating[i] = 
						userTraTolRating[i] / userTraDegree[i];
			}//Of if
			
			userTraDegree[i] = 0;
		}//Of for i
		
		// �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
		tempRanFile.seek(tempBeginIndex);
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUserIndex = Integer.parseInt(tempStrArray[0]) - 1;
			tempItemIndex = Integer.parseInt(tempStrArray[1]) - 1; 
			tempRating = Double.parseDouble(tempStrArray[2]); //*2
			
			userTraRatings[tempUserIndex][userTraDegree[tempUserIndex]] = (int) tempRating;
			userTrRateInds[tempUserIndex][userTraDegree[tempUserIndex]] = tempItemIndex;	
			userTraTolRating[tempUserIndex] += tempRating;
			userTraChannels[tempUserIndex][(int) Math.ceil(tempRating - 1)] ++;
			userTraDegree[tempUserIndex] ++;
		}// Of while
		
		
		for(int i = 0; i < userTraChannels.length; i ++){
			for(int j = 0; j < userTraChannels[0].length; j ++){
				userTraChanRate[i][j] = userTraChannels[i][j] / userTraDegree[i];
			}//Of for j
		}//Of for i
		
		tempRanFile.close();
		
	}//Of setUserTrainSet
	
	
	/*
	 * set item training set
	 */
	public static void setItemTrainSet(String paraFileName) throws Exception{
		File tempFile = null;
		String tempString = null;
		String[] tempStrArray = null;

		// Compute values of arrays
		tempFile = new File(paraFileName);
		if (!tempFile.exists()) {
			System.out.println("File is not exist!");
			return;
		}// Of if

		RandomAccessFile tempRanFile = new RandomAccessFile(tempFile, "r");
		// ���ļ�����ʼλ��
		int tempBeginIndex = 0;
		// �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
		tempRanFile.seek(tempBeginIndex);

		// Step 1. count the item degree
		int tempUserIndex = 0;
		int tempItemIndex = 0;
		double tempRating = 0;
		
		itemTraDegree = new int[itemNum];
		itemTraRatings = new int[itemNum][];
		itemTrRateInds = new int[itemNum][];
		itemTraTolRating = new double[itemNum];
		itemTraAveRating = new double[itemNum];
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempItemIndex = Integer.parseInt(tempStrArray[1]) - 1;

			itemTraDegree[tempItemIndex] ++;
		}// Of while
		
		for(int i = 0; i < itemTraDegree.length; i ++){
			itemTraRatings[i] = new int[itemTraDegree[i]];
			itemTrRateInds[i] = new int[itemTraDegree[i]];
			
			if(itemTraDegree[i] > 1e-6){
				itemTraAveRating[i] = 
						itemTraTolRating[i] / itemTraDegree[i];
			}//Of if
			
			itemTraDegree[i] = 0;
		}//Of for i
		
		// �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
		tempRanFile.seek(tempBeginIndex);
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUserIndex = Integer.parseInt(tempStrArray[0]) - 1;
			tempItemIndex = Integer.parseInt(tempStrArray[1]) - 1; 
			tempRating = Double.parseDouble(tempStrArray[2]); //*2
			
			itemTraRatings[tempItemIndex][itemTraDegree[tempItemIndex]] = (int) tempRating;
			itemTrRateInds[tempItemIndex][itemTraDegree[tempItemIndex]] = tempUserIndex;	
			itemTraTolRating[tempItemIndex] += tempRating;
			itemTraChannels[tempItemIndex][(int) Math.ceil(tempRating - 1)] ++;
			itemTraDegree[tempItemIndex] ++;
		}// Of while
		
		
		for(int i = 0; i < itemTraChannels.length; i ++){
			for(int j = 0; j < itemTraChannels[0].length; j ++){
				itemTraChanRate[i][j] = itemTraChannels[i][j] / itemTraDegree[i];
			}//Of for j
		}//Of for i
		
		tempRanFile.close();
		
	}//Of setUserTrainSet
	
	
	
	/**
	 ********************************** 
	 * build the Data Model.
	 * @throws Exception 
	 **********************************
	 */
	public static void buildDataModel() throws Exception {

	//	System.out.println(" start to set User Trainset !");
		setUserTrainSet(DataInfo.trainPath);
	//	System.out.println(" start to set Item Trainset !");
		setItemTrainSet(DataInfo.trainPath);
		
	}// Of buildDataModel

	/**
	 * Test the rating table.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		System.out.println("Testing ClusterDataInfo!");

		try {
			buildDataModel();
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		System.out.println("Testing end!");

	}// Of main

}
