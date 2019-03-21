package datamodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import tool.SimpleTool;;

public class GroupInfo {
	public static String userGroupPath = new String("src/data/GroupUserLittle.txt");
	public static String itemGroupPath = new String("src/data/GroupItemLittle.txt");
	static String split_Sign = new String(" ");

	/**
	 * 
	 * @param paraUserGroupPath
	 * @param paraItemGroupPath
	 * @throws IOException
	 */
	public GroupInfo(String paraUserGroupPath, String paraItemGroupPath) throws IOException{
		readUserGroup(paraUserGroupPath);
		readItemGroup(paraItemGroupPath);
	}//Of the first constructor
	/**
	 * 
	 * @param trainPath
	 * @param split_Sign
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	static void readUserGroup(String paraUserGroupPath) throws IOException {
		File file = new File(paraUserGroupPath);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		int[] groupDegree = new int[DataInfo.userGroupNum];
		buffRead.mark((int) file.length() + 1);
		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			int group = Integer.parseInt(parts[0]);// group id

			groupDegree[group]++;
		} // Of while

		for (int i = 0; i < DataInfo.userGroupNum; i++) {
			DataInfo.userGroup[i] = new int[groupDegree[i]];
			groupDegree[i] = 0;
		} // of for i
		buffRead.reset();
		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			int group = Integer.parseInt(parts[0]);// group id
			int user = Integer.parseInt(parts[1]);// user id

			DataInfo.userGroup[group][groupDegree[group]] = user;

			groupDegree[group]++;
		} // Of while
		buffRead.close();
	}// Of readUserGroup

	/**
	 * 
	 * @param trainPath
	 * @param split_Sign
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	static void readItemGroup(String paraItemGroupPath) throws IOException {
		File file = new File(paraItemGroupPath);
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		int[] groupDegree = new int[DataInfo.itemGroupNum];
		buffRead.mark((int) file.length() + 1);
		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			int group = Integer.parseInt(parts[0]);// group id

			groupDegree[group]++;
		} // Of while

		for (int i = 0; i < DataInfo.itemGroupNum; i++) {
			DataInfo.itemGroup[i] = new int[groupDegree[i]];
			groupDegree[i] = 0;
		} // of for i
		buffRead.reset();

		while (buffRead.ready()) {
			String str = buffRead.readLine();
			String[] parts = str.split(split_Sign);

			int group = Integer.parseInt(parts[0]);// group id
			int item = Integer.parseInt(parts[1]);// item id

			DataInfo.itemGroup[group][groupDegree[group]] = item;

			groupDegree[group]++;
		} // Of while
		buffRead.close();
	}// Of readUserGroup
	
	/**
	 * 
	 * @param paraUser
	 * @return tempGroup[0]: Group ID of the user;
	 * 		   tempGroup[1]: Group Position of the user;
	 */
	public static int[] getGroupIndex(int paraGroupNum, int[][] paraGroupMatrix, int paraUserOrItem) {
		int[] tempGroup = new int[2];
		for (int i = 0; i < paraGroupNum; i++) {
			tempGroup[1] = SimpleTool.binarySerach(paraGroupMatrix[i], paraUserOrItem);
			if(tempGroup[1] != -1){
				tempGroup[0] = i;
				break;
			}//Of if 
		} // Of for i
		return tempGroup;
	}// Of getUserGroup

	/**
	 * 
	 * @param paraUser
	 * @return tempGroup[0]: Group ID of the user;
	 * 		   tempGroup[1]: Group Position of the user;
	 */
	public static int[] getUserGroup(int paraUser) {
		int[] tempGroup = getGroupIndex(DataInfo.userGroupNum, DataInfo.userGroup, paraUser);
		
		return tempGroup;
	}// Of getUserGroup
	
	/**
	 * 
	 * @param paraItem
	 * @return tempGroup[0]: Group ID of the item;
	 * 		   tempGroup[1]: Group Position of the item;
	 */
	public static int[] getItemGroup(int paraItem) {
		int[] tempGroup = getGroupIndex(DataInfo.itemGroupNum, DataInfo.itemGroup, paraItem);
		
		return tempGroup;
	}// Of getUserGroup
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			GroupInfo tempGroup = new GroupInfo(userGroupPath, itemGroupPath);
			//SimpleTool.printMatrix(DataInfo.userGroup);
			int[] tempItemGroup = getItemGroup(8);
			System.out.println(tempItemGroup[1]);
			
		}catch(Exception e){
			e.printStackTrace();
		}//of try
	}//of main
}// Of class GroupInfo
