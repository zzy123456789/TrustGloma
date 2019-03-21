package SpectralCluster;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * <p>
 * Summary: Close the dialog. The message sender could be either a button or X
 * at the right top of respective dialog.
 * <p>
 * Author: <b>Henry</b> zhanghrswpu@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://www.fansmale.com/>Lab of Machine Learning</a>,
 * SouthWest Petroleum University, Sichuan 610500, China.<br>
 * Progress: OK. Copied from Hydrosimu.<br>
 * Written time: August 15, 2017. <br>
 * Last modify time: August 15, 2017.
 */
public class ClusterDataInfo {
	public final static int userNum = 1642;//10//1642
	
	//rating user number
	public static int rUserNum = 1508; //10//1508
	public final static int itemNum = 2071; //10 //2071
	
	/**
	 * User training set
	 */
	public static double[][] userLaplas;//L: The laplacians value for the user training
	public static double[] userTotalDegree;//D: The degrees for the user training	
	
	/**
	 * item training set
	 */
	public static double[][] itemLaplas;//L: The laplacians value for the user training
	public static double[] itemTotalDegree;//D: The degrees for the user training	
	
	
	/**
	 * 
	 * @param paraFileName
	 * @throws Exception
	 */
	public ClusterDataInfo() {
	}// Of the first constructor

	/**
	 * 
	 * @param paraFileName
	 * @throws Exception
	 */
	public void setTrustUserTrainSet(String paraFileName) throws Exception{
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
		// 读文件的起始位置
		int tempBeginIndex = 0;
		// 将读文件的开始位置移到beginIndex位置。
		tempRanFile.seek(tempBeginIndex);

		// Step 1. count the item degree
		int tempUser1Index = 0;
		int tempUser2Index = 0;
		double tempWeight = 0;
		
		userTotalDegree = new double[userNum];
		userLaplas = new double[userNum][userNum];
		
		//1. get total degree
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUser1Index = Integer.parseInt(tempStrArray[0]);
			tempUser2Index = Integer.parseInt(tempStrArray[1]); 
			tempWeight = Double.parseDouble(tempStrArray[2]); //*2
		
//			System.out.println("The user1 = " + tempUser1Index);
//			System.out.println("The user2 = " + tempUser2Index);
//			System.out.println("The weight  = " + tempWeight);
			
			if(tempUser1Index != tempUser2Index){
				userTotalDegree[tempUser1Index] += tempWeight;
				userTotalDegree[tempUser2Index] += tempWeight;
//				System.out.println("userTotalDegree[" +tempUser1Index+"] = "+ userTotalDegree[tempUser1Index]);
//				System.out.println("userTotalDegree[" +tempUser2Index+"] = "+ userTotalDegree[tempUser2Index]);			
			}//Of if
			
			/*
			 * 考虑到本身对本身有度，因此至少度的值为1 
			 */
			if(tempUser1Index == tempUser2Index){
				userTotalDegree[tempUser1Index] += 1;
			}//Of if
			
		}// Of while
	
		// 将读文件的开始位置移到beginIndex位置。
		//2. get laplacians matrix
		tempRanFile.seek(tempBeginIndex);
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUser1Index = Integer.parseInt(tempStrArray[0]);
			tempUser2Index = Integer.parseInt(tempStrArray[1]); 
			tempWeight = Double.parseDouble(tempStrArray[2]); //*2
			
			if(tempUser1Index != tempUser2Index){
				userLaplas[tempUser1Index][tempUser2Index] = 0 - tempWeight;
				userLaplas[tempUser2Index][tempUser1Index] = 0 - tempWeight;
			}//Of if
			else if(tempUser1Index == tempUser2Index){
				userLaplas[tempUser1Index][tempUser2Index] = userTotalDegree[tempUser1Index] - tempWeight;
			}
		}// Of while
	
		
		tempRanFile.close();
	}//Of setUserTrainSet
	
	
	public void setUserTrainSet(String paraFileName) throws Exception{
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
		// 读文件的起始位置
		int tempBeginIndex = 0;
		// 将读文件的开始位置移到beginIndex位置。
		tempRanFile.seek(tempBeginIndex);

		// Step 1. count the item degree
		int tempUser1Index = 0;
		int tempUser2Index = 0;
		double tempWeight = 0;
		
		userTotalDegree = new double[rUserNum];
		userLaplas = new double[rUserNum][rUserNum];
		
		//1. get total degree
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUser1Index = Integer.parseInt(tempStrArray[0]);
			tempUser2Index = Integer.parseInt(tempStrArray[1]); 
			tempWeight = Double.parseDouble(tempStrArray[2]); //*2
		
//			System.out.println("The user1 = " + tempUser1Index);
//			System.out.println("The user2 = " + tempUser2Index);
//			System.out.println("The weight  = " + tempWeight);
			
			if(tempUser1Index != tempUser2Index){
				userTotalDegree[tempUser1Index] += tempWeight;
				userTotalDegree[tempUser2Index] += tempWeight;
//				System.out.println("userTotalDegree[" +tempUser1Index+"] = "+ userTotalDegree[tempUser1Index]);
//				System.out.println("userTotalDegree[" +tempUser2Index+"] = "+ userTotalDegree[tempUser2Index]);			
			}//Of if
			
			/*
			 * 考虑到本身对本身有度，因此至少度的值为1 
			 */
			if(tempUser1Index == tempUser2Index){
				userTotalDegree[tempUser1Index] += 1;
			}//Of if
			
		}// Of while
	
		// 将读文件的开始位置移到beginIndex位置。
		//2. get laplacians matrix
		tempRanFile.seek(tempBeginIndex);
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempUser1Index = Integer.parseInt(tempStrArray[0]);
			tempUser2Index = Integer.parseInt(tempStrArray[1]); 
			tempWeight = Double.parseDouble(tempStrArray[2]); //*2
			
			if(tempUser1Index != tempUser2Index){
				userLaplas[tempUser1Index][tempUser2Index] = 0 - tempWeight;
				userLaplas[tempUser2Index][tempUser1Index] = 0 - tempWeight;
			}//Of if
			else if(tempUser1Index == tempUser2Index){
				userLaplas[tempUser1Index][tempUser2Index] = userTotalDegree[tempUser1Index] - tempWeight;
			}
		}// Of while
	
		
		tempRanFile.close();
	}//Of setUserTrainSet
	
	public void setItemTrainSet(String paraFileName) throws Exception{
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
		// 读文件的起始位置
		int tempBeginIndex = 0;
		// 将读文件的开始位置移到beginIndex位置。
		tempRanFile.seek(tempBeginIndex);

		// Step 1. count the item degree
		int tempItem1Index = 0;
		int tempItem2Index = 0;
		double tempWeight = 0;
		
		itemTotalDegree = new double[itemNum];
		itemLaplas = new double[itemNum][itemNum];
		
		//1. get total degree
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempItem1Index = Integer.parseInt(tempStrArray[0]);
			tempItem2Index = Integer.parseInt(tempStrArray[1]); 
			tempWeight = Double.parseDouble(tempStrArray[2]); //*2
		
//			System.out.println("The user1 = " + tempUser1Index);
//			System.out.println("The user2 = " + tempUser2Index);
//			System.out.println("The weight  = " + tempWeight);
			
			if(tempItem1Index != tempItem2Index){
				itemTotalDegree[tempItem1Index] += tempWeight;
				itemTotalDegree[tempItem2Index] += tempWeight;
//				System.out.println("userTotalDegree[" +tempUser1Index+"] = "+ userTotalDegree[tempUser1Index]);
//				System.out.println("userTotalDegree[" +tempUser2Index+"] = "+ userTotalDegree[tempUser2Index]);			

			}//Of if
			
			/*
			 * 考虑到本身对本身有度，因此至少度的值为1 
			 */
			if(tempItem1Index == tempItem2Index){
				itemTotalDegree[tempItem1Index] += 1;
			}//Of if
			
		}// Of while
	
		// 将读文件的开始位置移到beginIndex位置。
		//2. get laplacians matrix
		tempRanFile.seek(tempBeginIndex);
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempItem1Index = Integer.parseInt(tempStrArray[0]);
			tempItem2Index = Integer.parseInt(tempStrArray[1]); 
			tempWeight = Double.parseDouble(tempStrArray[2]); //*2
			
			if(tempItem1Index != tempItem2Index){
				itemLaplas[tempItem1Index][tempItem2Index] = 0 - tempWeight;
				itemLaplas[tempItem2Index][tempItem1Index] = 0 - tempWeight;
			}//Of if
			else if(tempItem1Index == tempItem2Index){
				itemLaplas[tempItem1Index][tempItem2Index] = itemTotalDegree[tempItem1Index] - tempWeight;
			}
		}// Of while
	
		
		tempRanFile.close();
	}//Of setItemTrainSet
	
	public static double[][] getDegreeMatrix(int paraNum, double[] paraDegree){
		double[][] degree = new double[paraNum][paraNum];
		
		for(int i = 0 ; i < paraNum; i++){
			for(int j = 0; j < paraNum; j++){
				if(i == j){
					degree[i][j] = paraDegree[i];
				}//Of if
				else{
					degree[i][j] = 0;	
				}		
			}//Of for j
		}//Of for i
		
		return degree;
	}//Of getDegreeMatrix
	
	
	/**
	 * 
	 * @throws Exception
	 */
	public void buildUserDataModel() throws Exception{
		String tempTrust = "src/data/userFilmIn.txt";
		String tempRating = "src/data/userFilm.txt";
	//	setTrustUserTrainSet(tempTrust);
		setUserTrainSet(tempRating);
		
	//	System.out.println("The degree is " + Arrays.toString(userTotalDegree));
	//	System.out.println("The Laplas is " + Arrays.deepToString(userLaplas));
	}//of buildDataModel
	
	public void buildItemDataModel() throws Exception{
		String tempTrain = "src/data/itemFilm.txt";
		setItemTrainSet(tempTrain);

	//	System.out.println("The degree is " + Arrays.toString(userTotalDegree));
	//	System.out.println("The Laplas is " + Arrays.deepToString(userLaplas));
	}//of buildDataModel
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {	
			ClusterDataInfo tempSoc = new ClusterDataInfo();
			tempSoc.buildUserDataModel();
			tempSoc.buildItemDataModel();
		} catch (Exception ee) {
			ee.printStackTrace();
		} // Of try
	}// Of main
}// Of Class SocialDataModel
