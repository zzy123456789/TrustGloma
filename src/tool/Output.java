package tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import datamodel.Triple;

public class Output {


	
    static String split_Sign = " ";
	
    //1. train set
	public static void putTrainFile(String paraFile, 
			Triple[] paraTrain) {
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
			for(int i = 0; i < paraTrain.length; i ++){
				
					String tempStr = "";				
					tempStr += (paraTrain[i].i + 1) + split_Sign + (paraTrain[i].j + 1) + split_Sign + paraTrain[i].rating+ "\r\n";				
					byte[] contentInBytes = tempStr.getBytes();
					fop.write(contentInBytes);
			
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
	
	
	   //2. test set
		public static void putTestFile(String paraFile, 
				Triple[] paraTest) {
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
				for(int i = 0; i < paraTest.length; i ++){
				
						String tempStr = "";				
						tempStr += (paraTest[i].i + 1) + split_Sign + (paraTest[i].j + 1)  + split_Sign + paraTest[i].rating  + "\r\n";				
						byte[] contentInBytes = tempStr.getBytes();
						fop.write(contentInBytes);
				
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
	
}//Of Output
