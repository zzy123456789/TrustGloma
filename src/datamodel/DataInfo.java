package datamodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import tool.SimpleTool;

public class DataInfo {
	// User group number
	public static int userGroupNum = 3;
	// Item group number
	public static int itemGroupNum = 3;
	
	// The user indices of each group
	public static int[][] userGroup = new int[userGroupNum][];
	// The item indices of each group
	public static int[][] itemGroup = new int[itemGroupNum][];
	
	public static int userNumber = 1508;//10 //1508
    public static int itemNumber = 2071; //10 //2071
	
	
	public static int trainNumber = 28397; //30 //28397
	public static int testNumber = 7100; //16 //7100
	
	/**********************Feature Matrix***********************************/
	public static short featureNumber = 2;
	public static double[][][][] uLuLiFeature = new double[userGroupNum][itemGroupNum][][];
	public static double[][][][] iLuLiFeature = new double[userGroupNum][itemGroupNum][][];
	public static double[][][] uLuGiFeature = new double[userGroupNum][][];
	public static double[][][] iLuGiFeature = new double[userGroupNum][][];
	public static double[][][] uGuLiFeature = new double[itemGroupNum][][];
	public static double[][][] iGuLiFeature = new double[itemGroupNum][][];
	public static double[][] uGuGiFeature = new double[userNumber][featureNumber];
	public static double[][] iGuGiFeature = new double[itemNumber][featureNumber];

	/******************** Training set *******************************/
	public static Triple[] trData = new Triple[trainNumber];


	/******************** Testing set *******************************/
	public static Triple[] teData = new Triple[testNumber];
	

	/****
	 */
	public static int round = 100; 
	public static double mean_rating = 0;
	public static int[] uDegree = new int[userNumber];
	public static int[] iDegree = new int[itemNumber];
	public static double[] uAveRates = new double[userNumber];
	public static double[] iAveRates = new double[itemNumber];
	
	
	public static double gama = 0.0008;
	public static double pi_1 = 0.5;
	public static double pi_2 = 0.3;
	public static double lamda_u = 0.05;
	public static double lamda_v = 0.05;
	public static double lamda_1 = 0.05;
	public static double lamda_2 = 0.05;
	public static double lamda_3 = 0.01;
	//public static int score_record = 0;
	
	public static String trainPath = new String("src/data/FilmTrain_1.txt");
	public static String testPath = new String("src/data/FilmTest_1.txt");
	public static String userGroupPath = new String("src/data/GroupUserLittle.txt");
	public static String itemGroupPath = new String("src/data/GroupItemLittle.txt");
	static String split_Sign = new String(" ");

	static int[] userCount;
	static int[] userPot;
	
	/**
	 * 
	 * @param paraFile
	 * @throws Exception
	 */
	public DataInfo(String paraTrainPath, String paraTestPath) throws IOException{
		readTrainData(paraTrainPath);
		readTestData(paraTestPath);
		setPot();
	}//of the first constructor
	
	/**
	 * 
	 * @param paraTrainPath
	 * @throws IOException
	 */
	static void readTrainData(String paraTrainPath) throws IOException {
		File file = new File(paraTrainPath);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		double sum = 0;
		int index = 0;
		for (int i = 0; i < DataInfo.trainNumber; i++) {
			trData[i] = new Triple();
		} // Of for i
		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			int user = Integer.parseInt(parts[0]) - 1;// user id
			int item = Integer.parseInt(parts[1]) - 1;// item id
			double rating = Double.parseDouble(parts[2]);// rating
			int[] userGroup = GroupInfo.getUserGroup(user);
			int[] itemGroup = GroupInfo.getItemGroup(item);

			trData[index].i = user;
			trData[index].j = item;
			trData[index].rating = rating;
			
			trData[index].userGrp = userGroup[0];
			trData[index].userGrpPos = userGroup[1];
			trData[index].itemGrp = itemGroup[0];
			trData[index].itemGrpPos = itemGroup[1];

			uDegree[user]++;
			iDegree[item]++;
			uAveRates[user] += rating;
			iAveRates[item] += rating;
			
			index++;
			sum += rating;// total rating
		} // Of while

		// total average rating
		DataInfo.mean_rating = sum / DataInfo.trainNumber;
		// user average 
		for(int i = 0; i < userNumber; i++){
			if(uDegree[i] > 1e-4){
				uAveRates[i] /= uDegree[i];
			}//Of if
		}//Of for i
		
		//item average
		for(int i = 0; i < itemNumber; i++){
			if(iDegree[i] > 1e-4){
				iAveRates[i] /= iDegree[i];
			}//Of if
		}//Of for i
		
		for (int i = 0; i < DataInfo.trainNumber; i++) {
			double tmp = (Double) trData[i].rating + mean_rating
					- uAveRates[i] - iAveRates[i];
			trData[i].rating = tmp;// 原始评分-平均分
		} // of for i
		buffRead.close();
	}
	/**
	 * 
	 * @param paraTestPath
	 * @throws IOException
	 */
	static void readTestData(String paraTestPath) throws IOException {
		File file = new File(paraTestPath);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		int user, item;
		double rating;
		int index = 0;
		for (int i = 0; i < DataInfo.testNumber; i++) {
			teData[i] = new Triple();
		} // Of for i
		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			user = Integer.parseInt(parts[0]) - 1;
			item = Integer.parseInt(parts[1]) - 1;
			rating = Double.parseDouble(parts[2]);
			
			int[] userGroup = GroupInfo.getUserGroup(user);
			int[] itemGroup = GroupInfo.getItemGroup(item);

			teData[index].i = user;
			teData[index].j = item;
			teData[index].rating = rating;
			teData[index].userGrp = userGroup[0];
			teData[index].userGrpPos = userGroup[1];
			teData[index].itemGrp = itemGroup[0];
			teData[index].itemGrpPos = itemGroup[1];

			index++;
		} // Of while

		buffRead.close();
	}// Of readTestData
	
	/**
	 * 
	 */
	static void setPot() {
		// 增加一个是为了便于后面处理
		userCount = new int[DataInfo.userNumber + 1];// 最后一个计数为0
		userPot = new int[DataInfo.userNumber + 1];

		for (int i = 0; i < DataInfo.trainNumber; i++) {
			userCount[trData[i].i]++;
		} // Of for i

		for (int i = 1; i <= DataInfo.userNumber; i++) {
			userPot[i] = userPot[i - 1] + userCount[i - 1];
		} // Of for i
	}// Of setPot
	
	/**
	 * 
	 * @param paraUser
	 * @param paraItem
	 * @return
	 */
	static double getRating(int paraUser, int paraItem){
		int left = userPot[paraUser];
		int right = userPot[paraUser + 1];
		
		while (left < right) {
			int mid = (left + right) / 2;
			if (trData[mid].j > paraItem) {
				right = mid - 1;
			} else if (trData[mid].j < paraItem) {
				left = mid + 1;
			} else {
				return trData[mid].rating;
			} // of if
		} // of while
		return 0;
	}//Of getRating
	
	/**
	 * 
	 * @param paraUser
	 * @param paraItem
	 * @return
	 */
	static Triple getDataInfo(int paraUser, int paraItem){
		int left = userPot[paraUser];
		int right = userPot[paraUser + 1] - 1;
		
		while (left <= right) {
			int mid = (left + right) / 2;
			if (trData[mid].j > paraItem) {
				right = mid - 1;
			} else if (trData[mid].j < paraItem) {
				left = mid + 1;
			} else {
				return trData[mid];
			} // of if
		} // of while
		return null;
	}//Of getRating
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			//Step 1. Initialize the user and item group
			GroupInfo tempGroup = new GroupInfo(userGroupPath, itemGroupPath);
			//Step 2. Initialize the train and test data based on group information
			DataInfo tempData = new DataInfo(trainPath, testPath);
			
			//Step3. Test
			SimpleTool.printTriple(tempData.trData);
//			Triple tempElement = getDataInfo(9, 6);
//			SimpleTool.printTriple(tempElement);
			
		}catch(Exception e){
			e.printStackTrace();
		}//of try
	}//of main
}//Of class DataInfo
