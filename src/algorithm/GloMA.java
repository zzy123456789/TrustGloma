package algorithm;

import datamodel.*;
import matrics.*;
import tool.SimpleTool;

/**
 * 
 * @author ziyin
 */
public class GloMA {
	/**
	 * R_{i,j} = \sigma_{l \in [0, k]} U_{i,l} * V_{l, j}
	 * 
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public static double predict(int userId, int itemId, int featureNumber, double[][] userFeature,
			double[][] itemFeature) {
		double pre = 0;
		for (int i = 0; i < featureNumber; i++) {
			// User����������Item�������ĳ˻�
			// ���ִ洢��ʽ�ǳ�����
			pre += userFeature[userId][i] * itemFeature[itemId][i];
		} // of for i
		return pre;
	}// Of predict

	/**
	 * ���л��ھ���ֽ�ķ������ı仯����update_one��
	 */
	public static void update_one() {
		for (int i = 0; i < DataInfo.trainNumber; i++) {
			// Step 1. Compute the prediction error
		
			int tempUserIndex = DataInfo.trData[i].i;
			int tempItemIndex = DataInfo.trData[i].j;
			int tempUserGroupIndex = DataInfo.trData[i].userGrp;
			int tempUserGroupPos = DataInfo.trData[i].userGrpPos;
			int tempItemGroupIndex = DataInfo.trData[i].itemGrp;
			int tempItemGroupPos = DataInfo.trData[i].itemGrpPos;

			double rate = DataInfo.trData[i].rating;

			// Step 1.1. Compute the prediction error for LULI(Local User Local Item)
			double delta0 = rate - predict(tempUserGroupPos, tempItemGroupPos, DataInfo.featureNumber,
					DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex],
					DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex]) ;// �в�
			/*
			System.out.println("update_one rate: " + rate + " delta0: " + delta0);
			if(Double.isNaN(delta0) || Double.isInfinite(delta0)){
				SimpleTool.printMatrix(DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex]);
				SimpleTool.printMatrix(DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex]);
				System.exit(0);
			}//Of if
			*/
			
			// Step 1.2 Compute the prediction error for LUGI(Local User Global Item)
			double delta1 = predict(tempUserGroupPos, tempItemIndex, DataInfo.featureNumber,
					DataInfo.uLuGiFeature[tempUserGroupIndex], DataInfo.iLuGiFeature[tempUserGroupIndex]) - rate;// �в�

			// Step 1.3. Compute the prediction error for GULI(Global User Local Item)
			double delta2 = predict(tempUserIndex, tempItemGroupPos, DataInfo.featureNumber,
					DataInfo.uGuLiFeature[tempItemGroupIndex], DataInfo.iGuLiFeature[tempItemGroupIndex]) - rate;// �в�
			//Step 1.4 Compute teh prediction error for GUGI
			double delta3 = rate - predict(tempUserIndex, tempItemIndex, DataInfo.featureNumber,
					DataInfo.uGuGiFeature, DataInfo.iGuGiFeature);
			
			// Step 2. Compute RMSE
			// Step 2.1 LULI
			double sigma0 = TrainRmse.rmseLULI(tempUserGroupIndex, tempItemGroupIndex);
			// Step 2.2 LUGI
			double sigma1 = TrainRmse.rmseLUGI(tempUserGroupIndex);
			// Step 2.3 GULI
			double sigma2 = TrainRmse.rmseGULI(tempItemGroupIndex);

			// Step 3. update
			//Step 3.1 GUGI for user
			for(int j = 0; j < DataInfo.featureNumber; j++){
				double tmp = 0;
				tmp = delta3 * DataInfo.iGuGiFeature[tempItemIndex][j] + 
						DataInfo.lamda_3 * DataInfo.uGuGiFeature[tempUserIndex][j];
			
				DataInfo.uGuGiFeature[tempUserIndex][j] -= DataInfo.gama * tmp;				
			}//Of for j
			
			// GUGI for item
			for(int j = 0; j < DataInfo.featureNumber; j++){
				double tmp = 0;
				tmp = delta3 * DataInfo.uGuGiFeature[tempUserIndex][j] + 
						DataInfo.lamda_3 * DataInfo.iGuGiFeature[tempItemIndex][j];
			
				DataInfo.iGuGiFeature[tempUserIndex][j] -= DataInfo.gama * tmp;				
			}//Of for j
					
			
			// LULI
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				double tmp = 0;
				if (sigma0 != 0 && sigma1 != 0) {
					tmp = 1 / sigma0 * delta0
							* DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempItemGroupPos][j]
							+ 1 / sigma1 * DataInfo.pi_1 * delta1
									* DataInfo.iLuGiFeature[tempUserGroupIndex][tempUserGroupPos][j]
							- DataInfo.lamda_u
									* DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempUserGroupPos][j];
				} // Of if

				DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempUserGroupPos][j] += DataInfo.gama
						* tmp;
			} // of for j

		//	System.out.println("The usergroup is " + tempUserGroupIndex +" , pos is " + tempItemGroupPos);
			// LULI
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				double tmp = 0;
				if (sigma0 != 0 && sigma2 != 0) {
					tmp = 1 / sigma0 * delta0
							* DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempUserGroupPos][j]
							+ 1 / sigma2 * DataInfo.pi_2 * delta2
									* DataInfo.uGuLiFeature[tempItemGroupIndex][tempItemGroupPos][j]
							- DataInfo.lamda_v
									* DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempItemGroupPos][j];
				} // of if
				DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempItemGroupPos][j] += DataInfo.gama
						* tmp;
			} // of for j

			//GULI
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				double tmp = 0;
				if (sigma2 != 0) {
					tmp = 1 / sigma2 * DataInfo.pi_2 * delta2
							* DataInfo.iLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempItemGroupPos][j]
							- DataInfo.lamda_2 * DataInfo.uGuLiFeature[tempItemGroupIndex][tempItemGroupPos][j];
				} // of if

				DataInfo.uGuLiFeature[tempItemGroupIndex][tempItemGroupPos][j] += DataInfo.gama * tmp;
			} // of for j

			//LUGI
			for (int j = 0; j < DataInfo.featureNumber; j++) {
				double tmp = 0;
				if (sigma1 != 0) {
					tmp = 1 / sigma1 * DataInfo.pi_1 * delta1
							* DataInfo.uLuLiFeature[tempUserGroupIndex][tempItemGroupIndex][tempUserGroupPos][j]
							- DataInfo.lamda_1 * DataInfo.iLuGiFeature[tempUserGroupIndex][tempUserGroupPos][j];
				} // of if

				DataInfo.iLuGiFeature[tempUserGroupIndex][tempUserGroupPos][j] += DataInfo.gama * tmp;
			} // of for j
		} // of for i
	}// Of update_one

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			GroupInfo tempGroup = new GroupInfo(GroupInfo.userGroupPath, GroupInfo.itemGroupPath);
			DataInfo tempData = new DataInfo(DataInfo.trainPath, DataInfo.testPath);
			
			FeatureMatrix.initFeatureMatrix();
			//SimpleTool.printMatrix(DataInfo.uGuGiFeature);
			System.out.println("Begin Training ! ! !");

			for (int i = 0; i < DataInfo.round; i++) {
				System.out.println("round:  " + (i + 1));
				update_one();

				double loss1 = TestRmse.rmseGUGI();
				System.out.println("loss1: " + loss1);
				double loss2 = TestRmse.rmseGULI();
				System.out.println("loss2: " + loss2);
				double loss3 = TestRmse.rmseLUGI();
				System.out.println("loss3: " + loss3);
				double loss4 = TestRmse.rmseLULI();
				System.out.println("loss4: " + loss4);

				double loss = (TestRmse.rmseGUGI() + TestRmse.rmseGULI() + TestRmse.rmseLUGI() + TestRmse.rmseLULI()) / 4;
				System.out.println("loss: " + loss);
			} // of for i
		} catch (Exception e) {
			e.printStackTrace();
		} // of try

	}// of main

}// of class GloMA
