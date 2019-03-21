package Graph;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The item graph.
 * @author ziyin
 */
public class ItemGraph {
	public static String similarityPath = new String("src/data/itemFilm.txt");
	
	/*
	 * similarity of item training set
	 */
	public static double[][] itemSimilarity;//The similarity between item and item.
	public static double[][] trainData;
	public static int[] itemDegree;
	
	public static double rateDistance;

	/**
	 * 
	 * @param paraFileName
	 * @throws IOException
	 */
	public static void readData(String paraFileName) throws IOException {
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
		double tempRate = 0;
		
		trainData = new double[GraphDataInfo.rUserNum][GraphDataInfo.itemNum];
		itemDegree =  new int[GraphDataInfo.itemNum];
		
		//1. get total degree
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUserIndex = Integer.parseInt(tempStrArray[0]) - 1;
			tempItemIndex = Integer.parseInt(tempStrArray[1]) - 1; 
			tempRate = Double.parseDouble(tempStrArray[2]); //*2
		
//			System.out.println("The user1 = " + tempUser1Index);
//			System.out.println("The user2 = " + tempUser2Index);
//			System.out.println("The weight  = " + tempWeight);
			
			trainData[tempUserIndex][tempItemIndex] = tempRate;
			itemDegree[tempItemIndex]++;				
		}// Of while		
		tempRanFile.close();		
	}//Of readData
	
	/**
	 * Get the item similarity's matrix.
	 * @param paraRateDis
	 */
	public static void computeSimilarity(double paraRateDis){
		
		itemSimilarity = new double[GraphDataInfo.itemNum][GraphDataInfo.itemNum];
		
		rateDistance = paraRateDis;
		for (int i = 0; i < GraphDataInfo.itemNum; i++) {
			for(int j = 0; j < GraphDataInfo.itemNum; j++){
				itemSimilarity[i][j] = computeSimilarity(i,j);
			}//Of for j
		}//of for i
		
	}//Of computeSimilarity
	
	/**
	 *  Compute the similarity between item1 and item2.
	 * @param paraItem1
	 * @param paraItem2
	 * @return
	 */
	public static double computeSimilarity(int paraItem1, int paraItem2) {

		double tempSimilarity = 0;
		
		if (itemDegree[paraItem1] == 0) {
			tempSimilarity = 0;
		}//Of if
		if (itemDegree[paraItem2] == 0) {
			tempSimilarity = 0;
		}//Of if
		
			// 1.by Manhattan-distance
			//tempSimilarity = computeMahSimilarity(paraItem1, paraItem2);

			// 2.Jccard-similarity
			tempSimilarity = computeJacSimilarity(paraItem1, paraItem2);
	
		return tempSimilarity;
	}// Of computeSimilarity

	// 1. by Manhattan-distance
	public static double computeMahSimilarity(int paraFirstItem, int paraSecondItem) {
		int tempUsers = GraphDataInfo.rUserNum;
		double tempDistance = 0;
		double tempSimilarity = 0;

		// Similarity after sorting
		for (int i = 0; i < tempUsers; i++) {
			tempDistance += Math.abs(trainData[i][paraFirstItem] - trainData[i][paraSecondItem]);
		}

		tempSimilarity = (tempUsers * rateDistance - tempDistance) / (tempUsers * rateDistance);
//		System.out.println(
//				"The similarity of user-" + paraFirstUser + " and user-" + paraSecondUser + " is " + tempSimilarity);
		return tempSimilarity;
	}// Of computeMahSimilarity

	// 2. by Jaccard-similarity
	public static double computeJacSimilarity(int paraFirstItem, int paraSecondItem) {
		int tempUsers = GraphDataInfo.rUserNum;
		double tempSimilarity = 0;
		int tempInterCount = 0;
		int tempSum = 0;

		// Similarity after sorting
		for (int i = 0; i < tempUsers; i++) {
			if (trainData[i][paraFirstItem] == 0 && trainData[i][paraSecondItem] > 0) {
				tempSum++;
			} else if (trainData[i][paraFirstItem] > 0 && trainData[i][paraSecondItem] == 0) {
				tempSum++;
			} else if (trainData[i][paraFirstItem] > 0 && trainData[i][paraSecondItem] > 0) {
				tempInterCount++;
				tempSum++;
			}
		} // Of while

		if (Math.abs(tempSum) < 1e-4) {
			tempSimilarity = 0;
		} // Of if
		else {
			tempSimilarity = (tempInterCount * 1.0) / (tempSum);
		}
	//	System.out.println("inter = " + tempInterCount + " , sum = " + tempSum);
	//	System.out.println(
	//			"The similarity of user-" + paraFirstUser + " and user-" + paraSecondUser + " is " + tempSimilarity);
		return tempSimilarity;
	}// Of computeJacSimilarity
	
		
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			String tempTrain = "src/data/FilmTrain_1.txt";
			readData(tempTrain);
			
			computeSimilarity(4.0 - 0.5);
			
			OutputGraphFile.putToInGrpFile(similarityPath, GraphDataInfo.itemNum, itemSimilarity);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}//Of Graph

