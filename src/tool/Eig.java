package tool;

import java.util.Arrays;

public class Eig implements Comparable{
	double eigValue;
	public double[] eigVector;
	
	public Eig(double eigValue, double[] eigVector) {
		this.eigValue = eigValue;
		this.eigVector = eigVector; 
	}

	@Override
	public String toString() {
		return "Eig [eigValue=" + eigValue + ", eigVector=" + Arrays.toString(eigVector) + "]";
	}

	@Override
	public int compareTo(Object arg0) {
		
		//What if argo cannot be converted to class Eig?
		Eig e = (Eig)arg0;
		
		if(eigValue == e.eigValue) {
			return 0;
		}
		return eigValue > e.eigValue ? 1:-1;
	}
	
}
