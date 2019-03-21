package Graph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputGraphFile {
	static String split_Sign = new String(" ");
	
    //out degree
	public static void putToOutGrpFile(String paraFile, 
			int paraUserNum, double[][] paraGraphInfo) {
		FileOutputStream fop = null;
		File file;
		
		try {
			file = new File(paraFile);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			for(int i = 0; i < paraUserNum; i ++){
				for(int j = i; j < paraUserNum; j ++){
					String tempStr = "";				
					tempStr += i + split_Sign + j + split_Sign + paraGraphInfo[i][j] + "\r\n";				
					byte[] contentInBytes = tempStr.getBytes();
					fop.write(contentInBytes);
				}//Of for j
			}//Of for i

			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	   //in degree
		public static void putToInGrpFile(String paraFile, 
				int paraUserNum, double[][] paraGraphInfo) {
			FileOutputStream fop = null;
			File file;
			
			try {
				file = new File(paraFile);
				fop = new FileOutputStream(file);

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				// get the content in bytes
				for(int i = 0; i < paraUserNum; i ++){
					for(int j = i ; j < paraUserNum; j ++){
						String tempStr = "";				
						tempStr += j + split_Sign + i + split_Sign + paraGraphInfo[j][i] + "\r\n";				
						byte[] contentInBytes = tempStr.getBytes();
						fop.write(contentInBytes);
					}//Of for j
				}//Of for i

				fop.flush();
				fop.close();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fop != null) {
						fop.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
}
