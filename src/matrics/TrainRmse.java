package matrics;

import datamodel.DataInfo;
import datamodel.GroupInfo;
import tool.SimpleTool;
import algorithm.*;

/**
 * Compute the RMSE in train.
 * @author ziyin
 */
public class TrainRmse {
	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseLUGI(int paraUserGrpIdx) {
		double rmse = 0;
		int tempCount = 0;

		for (int i = 0; i < DataInfo.trainNumber; i++) {
			// Step 1. Compute the prediction error
			int tempUserIndex = DataInfo.trData[i].i;
			int tempItemIndex = DataInfo.trData[i].j;
			int tempUserGroupIndex = DataInfo.trData[i].userGrp;
			int tempUserGroupPos = DataInfo.trData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.trData[i].itemGrp;
			int tempItemGroupPos = DataInfo.trData[i].itemGrpPos;
			
			double tempRate = DataInfo.trData[i].rating;
			
			if(paraUserGrpIdx == tempUserGroupIndex){
				double prediction = GloMA.predict(tempUserGroupPos, tempItemIndex, DataInfo.featureNumber,
						DataInfo.uLuGiFeature[tempUserGroupIndex], DataInfo.iLuGiFeature[tempUserGroupIndex]);
			//	System.out.println(" the predict is " + prediction);
				if (prediction < 0) {
					prediction = 0;
				} // Of if
				if (prediction - DataInfo.mean_rating + DataInfo.uAveRates[i]
						+ DataInfo.iAveRates[i] > 5) {
					
					prediction = 5 + DataInfo.mean_rating - DataInfo.uAveRates[i]
							- DataInfo.iAveRates[i];
					
				} // of if

				prediction = tempRate - prediction ;
				rmse += prediction * prediction;
				
				tempCount ++;
			}//of if
		}//of for i
		
		if(tempCount > 1e-6){
			rmse = Math.sqrt(rmse / tempCount);
		}//Of if

		return rmse;
	}// Of eval

	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseGULI(int paraItemGrpIdx) {
		double rmse = 0;
		int tempCount = 0;

		for (int i = 0; i < DataInfo.trainNumber; i++) {
			// Step 1. Compute the prediction error
			int tempUserIndex = DataInfo.trData[i].i;
			int tempItemIndex = DataInfo.trData[i].j;
			int tempUserGroupIndex = DataInfo.trData[i].userGrp;
			int tempUserGroupPos = DataInfo.trData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.trData[i].itemGrp;
			int tempItemGroupPos = DataInfo.trData[i].itemGrpPos;
			
			double tempRate = DataInfo.trData[i].rating;
			
			if(paraItemGrpIdx == tempItemGroupIndex){
				double prediction = GloMA.predict(tempUserIndex, tempItemGroupPos, DataInfo.featureNumber,
						DataInfo.uGuLiFeature[tempItemGroupIndex], DataInfo.iGuLiFeature[tempItemGroupIndex]);
				
				if (prediction < 0) {
					prediction = 0;
				} // Of if
				if (prediction - DataInfo.mean_rating + DataInfo.uAveRates[i]
						+ DataInfo.iAveRates[i] > 5) {
					
					prediction = 5 + DataInfo.mean_rating - DataInfo.uAveRates[i]
							- DataInfo.iAveRates[i];
					
				} // of if

				prediction = tempRate - prediction;
				rmse += prediction * prediction;
				
				tempCount ++;
			}//of if
		}//of for i
		
		if(tempCount > 1e-6){
			rmse = Math.sqrt(rmse / tempCount);
		}//Of if

		return rmse;
	}// Of eval
	
	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseLULI(int paraUserGrpIdx, int paraItemGrpIdx) {
		double rmse = 0;
		int tempCount = 0;

		for (int i = 0; i < DataInfo.trainNumber; i++) {
			// Step 1. Compute the prediction error
			int tempUserIndex = DataInfo.trData[i].i;
			int tempItemIndex = DataInfo.trData[i].j;
			int tempUserGroupIndex = DataInfo.trData[i].userGrp;
			int tempUserGroupPos = DataInfo.trData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.trData[i].itemGrp;
			int tempItemGroupPos = DataInfo.trData[i].itemGrpPos;
			
			double tempRate = DataInfo.trData[i].rating;
			
			if((paraUserGrpIdx == tempUserGroupIndex) && (paraItemGrpIdx == tempItemGroupIndex)){
				double prediction = GloMA.predict(tempUserGroupPos, tempItemGroupPos, DataInfo.featureNumber,
						DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex],
						DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex]);
				
				if (prediction < 0) {
					prediction = 0;
				} // Of if
				if (prediction - DataInfo.mean_rating + DataInfo.uAveRates[i]
						+ DataInfo.iAveRates[i] > 5) {
					
					prediction = 5 + DataInfo.mean_rating - DataInfo.uAveRates[i]
							- DataInfo.iAveRates[i];
					
				} // of if

				prediction = tempRate - prediction;
				rmse += prediction * prediction;
				
				tempCount ++;
			}//of if
		}//of for i
		
		if(tempCount > 1e-6){
			rmse = Math.sqrt(rmse / tempCount);
		}//Of if

		return rmse;
	}// Of eval

	/**
	 * Compute the RMSE
	 * 
	 * @return
	 */
	public static double rmseGUGI() {
		double rmse = 0;

		for (int i = 0; i < DataInfo.trainNumber; i++) {
			// Step 1. Compute the prediction error
			int tempUserIndex = DataInfo.trData[i].i;
			int tempItemIndex = DataInfo.trData[i].j;
			int tempUserGroupIndex = DataInfo.trData[i].userGrp;
			int tempUserGroupPos = DataInfo.trData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.trData[i].itemGrp;
			int tempItemGroupPos = DataInfo.trData[i].itemGrpPos;
			
			double tempRate = DataInfo.trData[i].rating;
			
			double prediction = GloMA.predict(tempUserIndex, tempItemIndex, DataInfo.featureNumber,
					DataInfo.uGuGiFeature, DataInfo.iGuGiFeature)
					+ DataInfo.mean_rating;

			if (prediction < 0) {
				prediction = 0;
			} // Of if
			if (prediction - DataInfo.mean_rating + DataInfo.uAveRates[i]
					+ DataInfo.iAveRates[i] > 5) {
				
				prediction = 5 + DataInfo.mean_rating - DataInfo.uAveRates[i]
						- DataInfo.iAveRates[i];
				
			} // of if

			prediction = tempRate - prediction;
			rmse += prediction * prediction;
			
		}//Of for i

		return Math.sqrt(rmse / DataInfo.trainNumber);
	}// Of eval
}// Of Class Rmse
