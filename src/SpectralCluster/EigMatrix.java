package SpectralCluster;

import java.util.Arrays;

import org.ujmp.core.Matrix;
import org.ujmp.core.calculation.Calculation.Ret;

import tool.Eig;

/**
 * <p>
 * Summary: Close the dialog. The message sender could be either a button or X
 * at the right top of respective dialog.
 * <p>
 * Author: <b>Henry</b> zhanghrswpu@163.com <br>
 * Copyright: The source code and all documents are open and free. PLEASE keep
 * this header while revising the program. <br>
 * Organization: <a href=http://www.fansmale.com/>Lab of Machine Learning</a>,
 * SouthWest Petroleum University, Sichuan 610500, China.<br>
 * Progress: OK. Copied from Hydrosimu.<br>
 * Written time: August 15, 2017. <br>
 * Last modify time: August 15, 2017.
 */
public class EigMatrix {
	
    public static Eig[] eig(double[][] matrix) {
		
		Matrix matrix2 = Matrix.Factory.importFromArray(matrix);
		
		Matrix[] reMatrixs = matrix2.eig();
		
		Eig[] re = new Eig[matrix.length];
		
		for (int i = 0; i < re.length; i++) {
			Matrix vec = reMatrixs[0].selectColumns(Ret.NEW, i).transpose();
			
			re[i] = new Eig(reMatrixs[1].getAsDouble(i, i), vec.toDoubleArray()[0]);
		}
		
		Arrays.sort(re);
		return re;
	}
	
    public static double[][] computeNewL(double[][] D, double[][] L) {
    	Matrix DMatrix = Matrix.Factory.importFromArray(D);
    	Matrix LMatrix = Matrix.Factory.importFromArray(L);
    	
    	DMatrix = DMatrix.inv().times(0.5);
    	Matrix re = DMatrix.mtimes(LMatrix).mtimes(DMatrix); 	
    	return re.toDoubleArray();
    }
    
	public static void main(String[] args) {
		Matrix m = Matrix.Factory.rand(5, 5);
		
		Eig[] re = eig(m.toDoubleArray());
		for(int i = 0; i < re.length; i++) {
			System.out.println(re[i]);
			
		}
		
		
	}
}
