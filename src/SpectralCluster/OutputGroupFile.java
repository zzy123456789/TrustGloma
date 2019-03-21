package SpectralCluster;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class OutputGroupFile {
	static String split_Sign = new String(" ");
	
	/**
	 * 
	 * @param paraFile
	 * @param paraGroupNum
	 * @param paraGroupInfo
	 */
	public static void outToGrpFile(String paraFile, 
			int paraGroupNum, int[] paraGroupInfo) {
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
			for(int i = 0; i < paraGroupNum; i ++){
				for(int j = 0; j < paraGroupInfo.length; j ++){
					String tempStr = "";
					if(paraGroupInfo[j] == i){
						tempStr += i + split_Sign + j + "\r\n";
					}//of if
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
