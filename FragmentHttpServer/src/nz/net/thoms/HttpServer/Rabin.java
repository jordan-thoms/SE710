package nz.net.thoms.HttpServer;

public class Rabin {
	public static int PRIME = 113;
	public static int WINDOW = 3;
		
		
	static int calculateRabin(byte[] b, int prevValue, int i) {
		// p ^ (WINDOW-1) which is p^2.
		int adjustedPrevious = prevValue; 
		
		if (i > 1) {
			adjustedPrevious -= (b[i-WINDOW+1] * (PRIME * PRIME));
		}
		 
	    int newValue = b[i+1] ;
		
		return adjustedPrevious * PRIME + newValue;
	}
}
