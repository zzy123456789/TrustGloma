package matrics;

import datamodel.DataInfo;
import datamodel.GroupInfo;
import tool.SimpleTool;
import algorithm.*;

/**
 * Compute the RMSE in test.
 * @author ziyin
 */
public class TestRmse {
	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseLUGI() {
		double rmse = 0;

		for (int i = 0; i < DataInfo.testNumber; i++) {
			int tempUserIndex = DataInfo.teData[i].i;
			int tempItemIndex = DataInfo.teData[i].j;
			int tempUserGroupIndex = DataInfo.teData[i].userGrp;
			int tempUserGroupPos = DataInfo.teData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.teData[i].itemGrp;
			int tempItemGroupPos = DataInfo.teData[i].itemGrpPos;
			
			double tempRate = DataInfo.teData[i].rating;

			double prediction = GloMA.predict(tempUserGroupPos, tempItemIndex, DataInfo.featureNumber,
					DataInfo.uLuGiFeature[tempUserGroupIndex], DataInfo.iLuGiFeature[tempUserGroupIndex])
					- DataInfo.mean_rating + DataInfo.uAveRates[i] + DataInfo.iAveRates[i]; 

			if (prediction < 0) {
				prediction = 0;
			} // Of if
			if (prediction > 5) {
				prediction = 5;
			} // of if

			prediction = tempRate - prediction;
			rmse += prediction * prediction;
		} // Of for i

		return Math.sqrt(rmse / DataInfo.testNumber);
	}// Of eval

	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseGULI() {
		double rmse = 0;

		for (int i = 0; i < DataInfo.testNumber; i++) {
			int tempUserIndex = DataInfo.teData[i].i;
			int tempItemIndex = DataInfo.teData[i].j;
			int tempUserGroupIndex = DataInfo.teData[i].userGrp;
			int tempUserGroupPos = DataInfo.teData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.teData[i].itemGrp;
			int tempItemGroupPos = DataInfo.teData[i].itemGrpPos;
			double tempRate = DataInfo.teData[i].rating;

			double prediction = GloMA.predict(tempUserIndex, tempItemGroupPos, DataInfo.featureNumber,
					DataInfo.uGuLiFeature[tempItemGroupIndex], DataInfo.iGuLiFeature[tempItemGroupIndex])
					- DataInfo.mean_rating + DataInfo.uAveRates[i] + DataInfo.iAveRates[i]; 

			if (prediction < 0) {
				prediction = 0;
			} // Of if
			if (prediction > 5) {
				prediction = 5;
			} // of if

	//		System.out.println("rmseGULI prediction " + prediction);
			prediction = tempRate - prediction;

			rmse += prediction * prediction;
		} // Of for i

	//	System.out.println("rmseGULI rmse "+ rmse);
		return Math.sqrt(rmse / DataInfo.testNumber);
	}// Of eval
	
	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseLULI() {
		double rmse = 0;

		for (int i = 0; i < DataInfo.testNumber; i++) {
			int tempUserIndex = DataInfo.teData[i].i;
			int tempItemIndex = DataInfo.teData[i].j;
			int tempUserGroupIndex = DataInfo.teData[i].userGrp;
			int tempUserGroupPos = DataInfo.teData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.teData[i].itemGrp;
			int tempItemGroupPos = DataInfo.teData[i].itemGrpPos;
			double tempRate = DataInfo.teData[i].rating;

			double prediction = GloMA.predict(tempUserGroupPos, tempItemGroupPos, DataInfo.featureNumber,
					DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex], 
					DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex])
					- DataInfo.mean_rating + DataInfo.uAveRates[i] + DataInfo.iAveRates[i]; 

			if (prediction < 0) {
				prediction = 0;
			} // Of if
			if (prediction > 5) {
				prediction = 5;
			} // of if

			prediction = tempRate - prediction;
			rmse += prediction * prediction;
		} // Of for i

		return Math.sqrt(rmse / DataInfo.testNumber);
	}// Of eval

	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseGUGI() {
		double rmse = 0;

		for (int i = 0; i < DataInfo.testNumber; i++) {
			int tempUserIndex = DataInfo.teData[i].i;
			int tempItemIndex = DataInfo.teData[i].j;
			double tempRate = DataInfo.teData[i].rating;

			double prediction = GloMA.predict(tempUserIndex, tempItemIndex, DataInfo.featureNumber,
					DataInfo.uGuGiFeature, DataInfo.iGuGiFeature)
					- DataInfo.mean_rating + DataInfo.uAveRates[i] + DataInfo.iAveRates[i]; 

			if (prediction < 0) {
				prediction = 0;
			} // Of if
			if (prediction > 5) {
				prediction = 5;
			} // of if

			prediction = tempRate - prediction;
			rmse += prediction * prediction;
		} // Of for i

		return Math.sqrt(rmse / DataInfo.testNumber);
	}// Of eval
}// Of Class Rmse
