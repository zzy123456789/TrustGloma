package Graph;

import java.util.Arrays;

import SpectralCluster.OutputGroupFile;

/**
 * The trust graph.
 * @author ziyin
 */
public class TrustGraph {
  
	public static String outDegreePath = new String("src/data/userFilmOut.txt");
	public static String inDegreePath = new String("src/data/userFilmIn.txt");
	
	private void matrixAdd(int paraLength) {
		
		System.out.println("The length is " + paraLength);
		
		double[][] tempTrustor = new double[GraphDataInfo.userNum][GraphDataInfo.userNum];
		double[][] tempTrustee = new double[GraphDataInfo.userNum][GraphDataInfo.userNum];		
		double[][] tempCountMatrix = new double[GraphDataInfo.userNum][GraphDataInfo.userNum];
			
		//Step 1. copy original matrix
		for(int i = 0 ; i < GraphDataInfo.userNum; i++){
			for(int j = 0 ; j < GraphDataInfo.userNum; j++){				
				tempTrustor[i][j] = GraphDataInfo.trustor[i][j];
				tempTrustee[i][j] = GraphDataInfo.trustee[i][j];
				tempCountMatrix[i][j] = GraphDataInfo.trustor[i][j];
			}//Of for j
		}//Of for i
		
		
		for(int i = 1 ; i < paraLength; i++){
			
			//Step 2. add matrix
			tempTrustor = addTrustor(tempTrustor, tempTrustee);
			
			//Step 3. update matrix
			/*
			 * if update trustee, T = T + T2 + T4 + T16 +...
			 * if not, T = T + T2 + T3 + T4 + ...
			 */
			//tempTrustee = updateTrustee(tempTrustor);
			
			//Step 4. count matrix
			tempCountMatrix = countTrustor(tempTrustor, tempCountMatrix);
		}//Of for i
		
		//Step 5. final update trustor matrix
		GraphDataInfo.trustor = updateTrustor(tempCountMatrix);
		GraphDataInfo.trustee = updateTrustee(tempCountMatrix);
	}//of matrixAdd

	/**
	 * Add two matrix
	 * @param paraFirstMatrix
	 * @param paraSecondMatrix
	 * @return
	 */
	public double[][] addTrustor(double[][] paraFirstMatrix, double[][] paraSecondMatrix){
		
		double[][] tempMatrix = new double[GraphDataInfo.userNum][GraphDataInfo.userNum];
        
		for(int i = 0 ; i < GraphDataInfo.userNum ; i++){
			for(int j = 0 ; j <GraphDataInfo.userNum ; j++){				
				tempMatrix[i][j] = addTrustorValue(i,paraFirstMatrix,j,paraSecondMatrix);
			}//Of for j
		}//Of for i
		
		return tempMatrix;
	}//Of addTrustor

	/**
	 * Compute one value of matrix.
	 * @param paraRow
	 * @param paraFirstMatrix
	 * @param paraCol
	 * @param paraSecondMatrix
	 * @return
	 */
	private double addTrustorValue(int paraRow, double[][] paraFirstMatrix, int paraCol, double[][] paraSecondMatrix) {		
		
		double tempValue = 0;
		
		for(int i = 0 ; i < GraphDataInfo.userNum; i++){
			tempValue += paraFirstMatrix[paraRow][i] * paraSecondMatrix[paraCol][i];
		}//Of for i	
		
		return tempValue;
	}//Of addTrustorValue
	
	/**
	 * trustee[j][i] == trustor[i][j];
	 * @param tempTrustor
	 * @return
	 */
	private double[][] updateTrustee(double[][] tempTrustor) {
		
		double[][] tempTrustee = new double[GraphDataInfo.userNum][GraphDataInfo.userNum];		
		
		for(int i = 0 ; i < GraphDataInfo.userNum; i++){
			for(int j = 0 ; j < GraphDataInfo.userNum; j++){				
				tempTrustee[i][j] = tempTrustor[j][i];						
			}//Of for j			
		}//Of for i
			
		return tempTrustee;
	}//Of updateTrustee


    /**
     * 
     * @param tempCount
     * @return
     */
	private double[][] updateTrustor(double[][] tempCount) {

		double[][] tempTrustor = new double[GraphDataInfo.userNum][GraphDataInfo.userNum];		
		
		for(int i = 0 ; i < GraphDataInfo.userNum; i++){
			for(int j = 0 ; j < GraphDataInfo.userNum; j++){				
				tempTrustor[i][j] = tempCount[i][j];						
			}//Of for j			
		}//Of for i
			
		return tempTrustor;
	}//Of updateTrustor
	
	/**
	 * Count the matrix value.
	 * @param tempTrustor
	 * @param tempCountMatrix
	 * @return
	 */
	private double[][] countTrustor(double[][] tempTrustor, double[][] tempCountMatrix) {
 
		double[][] tempCount = new double[GraphDataInfo.userNum][GraphDataInfo.userNum];		
		
		for(int i = 0 ; i < GraphDataInfo.userNum; i++){
			for(int j = 0 ; j < GraphDataInfo.userNum; j++){				
				tempCount[i][j] = tempTrustor[i][j] + tempCountMatrix[i][j];						
			}//Of for j			
		}//Of for i		
		
		return tempCount;	
	}//Of countTrustor


	public static void trustAdd() throws Exception {
		
		GraphDataInfo tempSoc = new GraphDataInfo();
		tempSoc.buildDataModel();
		
		TrustGraph tempAdd = new TrustGraph();
		
//		System.out.println("Before add, trustor is \n" + Arrays.deepToString(GraphDataInfo.trustor));
//		System.out.println("Before add, trustee is \n" + Arrays.deepToString(GraphDataInfo.trustee));
//		
		tempAdd.matrixAdd(3);
		
	//	System.out.println("After add, trustor is \n" + Arrays.deepToString(GraphDataInfo.trustor));
	//	System.out.println("After add, trustee is \n" + Arrays.deepToString(GraphDataInfo.trustee));
		
		OutputGraphFile.putToOutGrpFile(outDegreePath, GraphDataInfo.userNum, GraphDataInfo.trustor);
		OutputGraphFile.putToInGrpFile(inDegreePath, GraphDataInfo.userNum, GraphDataInfo.trustor);

	}//Of trustorAdd


	public static void main(String[] args) {
		
		try {
			trustAdd();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}//Of main

}//Of TrustorGraph
