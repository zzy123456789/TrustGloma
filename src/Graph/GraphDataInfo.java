package Graph;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * The data information.
 * @author ziyin
 */
public class GraphDataInfo {	
	public final static int userNum = 1642;//10//1642
	
	//rating user number
	public static int rUserNum = 1508; //10//1508
	public final static int itemNum = 2071; //10 //2071
	
	/**
	 * trustor training set
	 */
	public static double[][] trustor; //The trust matrix for the trustor 
	public static int[][] trustorUserIndices;//The trustee indices for the user training	
	public static int[] trustorDegree;	//The degrees for the trustor
	
	/**
	 * trustee training set
	 */
	public static double[][] trustee; //The trust matrix for the trustee
	public static int[][] trusteeUserIndices;//The trustor indices for the user training	
	public static int[] trusteeDegree;	//The degrees for the trustee
	
	
	
	/**
	 * 
	 * @param paraFileName
	 * @throws Exception
	 */
	public GraphDataInfo() {
	}// Of the first constructor

	/**
	 * Get the trustor train set.
	 * @param paraFileName
	 * @throws Exception
	 */
	public void setTrustorTrainSet(String paraFileName) throws Exception{
		File tempFile = null;
		String tempString = null;
		String[] tempStrArray = null;

		//1. Compute values of arrays
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
		int tempTrusterIndex = 0;
		int tempTrusteeIndex = 0;
		double tempTrust = 0;
			
		trustor = new double[userNum][userNum];
		trustorUserIndices= new int[userNum][userNum];
		trustorDegree = new int[userNum];
		
		//Step 2.initial self to self as 1
		for(int i = 0 ; i < userNum; i++){
			trustor[i][i] = 1;
		}//of for i
		
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempTrusterIndex = Integer.parseInt(tempStrArray[0]) - 1;

			trustorDegree[tempTrusterIndex] ++;
		}// Of while
		
//		for(int i = 0; i < trustorDegree.length; i ++){
//			trustor[i] = new int[trustorDegree[i]];		
//			trustorUserIndices[i] = new int[trustorDegree[i]];		
//			trustorDegree[i] = 0;
//		}//Of for i
	
		// �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
		tempRanFile.seek(tempBeginIndex);
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempTrusterIndex = Integer.parseInt(tempStrArray[0]) - 1;
			tempTrusteeIndex = Integer.parseInt(tempStrArray[1]) - 1; 
			tempTrust = Double.parseDouble(tempStrArray[2]); //*2
		
			trustor[tempTrusterIndex][tempTrusteeIndex] = (int) tempTrust;
			trustorUserIndices[tempTrusterIndex][tempTrusteeIndex] = tempTrusteeIndex;
			trustorDegree[tempTrusterIndex] ++;
		}// Of while		
		tempRanFile.close();
		

	}//Of setTrustorTrainSet
	
	/**
	 * Get the trustee train set.
	 * @param paraFileName
	 * @throws Exception
	 */
	public void setTrusteeTrainSet(String paraFileName) throws Exception{
		File tempFile = null;
		String tempString = null;
		String[] tempStrArray = null;

		//1. Compute values of arrays
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

		// Step 1. count the  degree
		int tempTrusterIndex = 0;
		int tempTrusteeIndex = 0;
		double tempTrust = 0;
			
		trustee = new double[userNum][userNum];
		trusteeUserIndices= new int[userNum][userNum];
		trusteeDegree = new int[userNum];
				
		//Step 2.initial self to self as 1
		for(int i = 0 ; i < userNum; i++){
			trustee[i][i] = 1;
		}//of for i	
		
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempTrusteeIndex = Integer.parseInt(tempStrArray[1]) - 1;
			trusteeDegree[tempTrusteeIndex] ++;
		}// Of while
		
//		for(int i = 0; i < trusteeDegree.length; i ++){
//			trustee[i] = new int[trusteeDegree[i]];	
//			trusteeUserIndices[i] = new int[trusteeDegree[i]];	
//			trusteeDegree[i] = 0;
//		}//Of for i
		
			
		// �����ļ��Ŀ�ʼλ���Ƶ�beginIndexλ�á�
		tempRanFile.seek(tempBeginIndex);
		while ((tempString = tempRanFile.readLine()) != null) {
			tempStrArray = tempString.split(" ");
			tempTrusterIndex = Integer.parseInt(tempStrArray[0]) - 1;
			tempTrusteeIndex = Integer.parseInt(tempStrArray[1]) - 1; 
			tempTrust = Double.parseDouble(tempStrArray[2]); //*2
			
			trustee[tempTrusteeIndex][tempTrusterIndex] = (int) tempTrust;
			trusteeUserIndices[tempTrusteeIndex][tempTrusterIndex] = tempTrusterIndex;			
			trusteeDegree[tempTrusteeIndex] ++;
		}// Of while		
		tempRanFile.close();
		
	}//Of setTrusteeTrainSet
	
	/**
	 * Build the data model.
	 * @throws Exception
	 */
	public void buildDataModel() throws Exception{
		String tempTrain = "src/data/FilmTrust.txt";
		setTrustorTrainSet(tempTrain);
	//	System.out.println("The matrix of trustor is \n" + Arrays.deepToString(trustor));
	
		setTrusteeTrainSet(tempTrain);
	//	System.out.println("The matrix of trustee is \n" + Arrays.deepToString(trustee));
		
	}//of buildDataModel
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {	
			GraphDataInfo tempSoc = new GraphDataInfo();
			tempSoc.buildDataModel();
		} catch (Exception ee) {
			ee.printStackTrace();
		} // Of try
	}// Of main
}//Of GraphDataInfo
