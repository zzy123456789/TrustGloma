package Graph;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * The user graph.
 * @author ziyin
 */
public class UserGraph {
	public static String similarityPath = new String("src/data/userFilm.txt");
	
	/*
	 * similarity of user training set
	 */
	public static double[][] userSimilarity;//The similarity between user and user.
	public static double[][] trainData;
	public static int[] userDegree;
	
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

		// Step 1. count the user degree
		int tempUserIndex = 0;
		int tempItemIndex = 0;
		double tempRate = 0;
		
		trainData = new double[GraphDataInfo.rUserNum][GraphDataInfo.userNum];
		userDegree =  new int[GraphDataInfo.rUserNum];
		
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
			userDegree[tempUserIndex]++;				
		}// Of while		
		tempRanFile.close();		
	}//Of readData
	
	/**
	 * Get the user similarity's matrix.
	 * @param paraRateDis
	 */
	public static void computeSimilarity(double paraRateDis){
		
		userSimilarity = new double[GraphDataInfo.rUserNum][GraphDataInfo.rUserNum];
		
		rateDistance = paraRateDis;
		for (int i = 0; i < GraphDataInfo.rUserNum; i++) {
			for(int j = 0; j < GraphDataInfo.rUserNum; j++){
				userSimilarity[i][j] = computeSimilarity(i,j);
			}//Of for j
		}//of for i
		
	}//Of computeSimilarity
	
	/**
	 *  Compute the similarity between user1 and user2.
	 * @param paraUser1
	 * @param paraUser2
	 * @return
	 */
	public static double computeSimilarity(int paraUser1, int paraUser2) {

		double tempSimilarity = 0;
		
		if (userDegree[paraUser1] == 0) {
			tempSimilarity = 0;
		}//Of if
		if (userDegree[paraUser2] == 0) {
			tempSimilarity = 0;
		}//Of if
		
			// 1.by Manhattan-distance
			//tempSimilarity = computeMahSimilarity(paraUser1, paraUser2);

			// 2.Jccard-similarity
			tempSimilarity = computeJacSimilarity(paraUser1, paraUser2);
	
		return tempSimilarity;
	}// Of computeSimilarity

	// 1. by Manhattan-distance
	public static double computeMahSimilarity(int paraFirstUser, int paraSecondUser) {
		int tempItems = GraphDataInfo.itemNum;
		double tempDistance = 0;
		double tempSimilarity = 0;

		// Similarity after sorting
		for (int i = 0; i < tempItems; i++) {
			tempDistance += Math.abs(trainData[paraFirstUser][i] - trainData[paraSecondUser][i]);
		}

		tempSimilarity = (tempItems * rateDistance - tempDistance) / (tempItems * rateDistance);
//		System.out.println(
//				"The similarity of user-" + paraFirstUser + " and user-" + paraSecondUser + " is " + tempSimilarity);
		return tempSimilarity;
	}// Of computeMahSimilarity

	// 2. by Jaccard-similarity
	public static double computeJacSimilarity(int paraFirstUser, int paraSecondUser) {
		int tempItems = GraphDataInfo.itemNum;
		double tempSimilarity = 0;
		int tempInterCount = 0;
		int tempSum = 0;

		// Similarity after sorting
		for (int i = 0; i < tempItems; i++) {
			if (trainData[paraFirstUser][i] == 0 && trainData[paraSecondUser][i] > 0) {
				tempSum++;
			} else if (trainData[paraFirstUser][i] > 0 && trainData[paraSecondUser][i] == 0) {
				tempSum++;
			} else if (trainData[paraFirstUser][i] > 0 && trainData[paraSecondUser][i] > 0) {
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
	
		
	
	public static void main(String[] args) {
		
		try {
			
			String tempTrain = "src/data/FilmTrain_1.txt";
			readData(tempTrain);
			
			computeSimilarity(4.0 - 0.5);
			
			OutputGraphFile.putToInGrpFile(similarityPath, GraphDataInfo.rUserNum, userSimilarity);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

}//Of Graph

