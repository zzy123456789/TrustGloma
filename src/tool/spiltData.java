package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import datamodel.Triple;

public class spiltData {
	
	public static int userNum = 1508;//10//1508
	public static int itemNum = 2071;//10//2071	
	public static int dataNum = 35497;//30
	
	public static int trainNum;
	public static int testNum;
	
	/**
	 * data set
	 */
	public static Triple[] Data; //The original data 
	public static Triple[] TrainData;//The training	data
	public static Triple[] TestData;//The testing data
	
	public static String dataPath = new String("src/data/FilmRatings.txt");
	public static String trainPath = new String("src/data/FilmTrain_1.txt");
	public static String testPath = new String("src/data/FilmTest_1.txt");

	static String split_Sign = new String(" ");

	
	static void readData(String paraFileName, double paraRatio) throws IOException {
		trainNum = (int) (dataNum * paraRatio);
		testNum = (int) (dataNum  - trainNum);
		System.out.println("Thr trainNum is " + trainNum);
		System.out.println("The testNum is " + testNum);
		Data = new Triple[dataNum];
	
		File file = new File(paraFileName);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		double sum = 0;
		int index = 0;
		//1. initial
		for (int i = 0; i < dataNum; i++) {
			Data[i] = new Triple();
		} // Of for i
				
		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			int user = Integer.parseInt(parts[0]) - 1;// user id
			int item = Integer.parseInt(parts[1]) - 1;// item id
			double rating = Double.parseDouble(parts[2]);// rating

			Data[index].i = user;
			Data[index].j = item;
			Data[index].rating = rating;
		
			index++;
		
		} // Of while
		buffRead.close();
		
	}//Of readData
	
	
	/**
	 *************** 
	 * Swap the elements of an array randomly.
	 **************** 
	 */
	public static int[] randomSwapIndices(int paraLength) {
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
	public static void randomSelect() {
		int[] tempIndices = randomSwapIndices(dataNum);
		
		TrainData = new Triple[trainNum];
		TestData = new Triple[testNum];

		for (int i = 0; i < trainNum; i++) {
			TrainData[i] = new Triple();
		} // Of for i
		for (int i = 0; i < testNum; i++) {
			TestData[i] = new Triple();
		} // Of for i
		
		//training set
		for (int i = 0; i < trainNum; i++) {		
			
			TrainData[i].i = Data[tempIndices[i]].i;
			TrainData[i].j = Data[tempIndices[i]].j;
			TrainData[i].rating = Data[tempIndices[i]].rating;			
		} // Of for i
		
		//testing set
		for (int i = trainNum; i < dataNum; i++) {		
	
			TestData[i - trainNum].i = Data[tempIndices[i]].i;
			TestData[i - trainNum].j = Data[tempIndices[i]].j;
			TestData[i - trainNum].rating = Data[tempIndices[i]].rating;			
		} // Of for i
		
	}// Of randomSelect

	
	
	public static void main(String[] args) {
		try {
			readData(dataPath,0.8);
					
			randomSelect();
			
			Output.putTrainFile(trainPath, TrainData);
			Output.putTestFile(testPath, TestData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}//Of spiltData
