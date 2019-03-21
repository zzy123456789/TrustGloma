package datamodel;

import java.util.Random;

import tool.SimpleTool;

public class FeatureMatrix {
	
	/**
	 * 
	 */
	public static void initFeatureMatrix(){
		//LULI
		for(int i = 0; i < DataInfo.userGroupNum; i ++){
			for(int j = 0; j < DataInfo.itemGroupNum; j ++){
				DataInfo.uLuLiFeature[i][j] = initFeature(
						DataInfo.userGroup[i].length, DataInfo.featureNumber);
				DataInfo.iLuLiFeature[i][j] = initFeature(
						DataInfo.itemGroup[j].length, DataInfo.featureNumber);		
			}//of for j
		}//of for i
		
		//LUGI
		for(int i = 0; i < DataInfo.userGroupNum; i ++){
			DataInfo.uLuGiFeature[i] = initFeature(
					DataInfo.userGroup[i].length, DataInfo.featureNumber);
			DataInfo.iLuGiFeature[i] = initFeature(
					DataInfo.itemNumber, DataInfo.featureNumber);
		}//Of for i
		
		//GULI
		for(int i = 0; i < DataInfo.itemGroupNum; i ++){
			DataInfo.uGuLiFeature[i] = initFeature(
					DataInfo.userNumber, DataInfo.featureNumber);
			DataInfo.iGuLiFeature[i] = initFeature(
					DataInfo.itemGroup[i].length, DataInfo.featureNumber);
		}//Of for i
		
		//GUGI
		DataInfo.uGuGiFeature = initFeature(
				DataInfo.userNumber, DataInfo.featureNumber);
		DataInfo.iGuGiFeature = initFeature(
				DataInfo.itemNumber, DataInfo.featureNumber);
	}//Of initFeatureMatrix
	
	/**
	 * 给特征矩阵填上随机值
	 * @param paraUserOrItemNum
	 * @param paraFeatNum
	 * @return
	 */
	static double[][] initFeature(int paraUserOrItemNum, int paraFeatNum) {
		double[][] tempFeatureMatrix = new double[paraUserOrItemNum][paraFeatNum];
		Random rand = new Random();

		for (int i = 0; i < paraUserOrItemNum; i++){
			for (int j = 0; j < paraFeatNum; j++){
				tempFeatureMatrix[i][j] = 0.01 * rand.nextDouble();
			}//of for j
		}//Of for i
		
		return tempFeatureMatrix;
	}//of initFeature

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			GroupInfo.readUserGroup(GroupInfo.userGroupPath);
			GroupInfo.readItemGroup(GroupInfo.itemGroupPath);
			initFeatureMatrix();
			SimpleTool.printMatrix(DataInfo.uGuGiFeature);
		}catch(Exception e){
			e.printStackTrace();
		}//of try
	}//of main

}//of FeatureMatrix
