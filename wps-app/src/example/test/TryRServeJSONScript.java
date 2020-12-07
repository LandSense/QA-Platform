package example.test;


import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RserveException;

public class TryRServeJSONScript {	
	
	private static String fileName = "cobweb_pts.geojson";	
	private static String fileNameAndPath  = "C:/Users/ezzjfr/Downloads/cobweb_pts.geojson";	
					
	private double result = 0;
	
	public TryRServeJSONScript() {	
        System.out.println("Trying RServe JSON...");
		result = callScript(); 
	}
	
	public double getResult() {
		return result;
	}	

	public static double callScript() {
		
		System.out.println("Calling Rserve script...");
		 
		double myResult = 0;
		RConnection connection;
		try {
			
			System.out.println("Making connection to RServe...");
			connection = new RConnection();						
	
			// Source the R script 
			connection.eval("source('C:/Users/ezzjfr/Documents/lucas_git/landsenseR/rserve_examples/myJSONAverage.R')");
			
			//Call the R script with the 
			myResult=connection.eval("myJSONAverage('"+ fileNameAndPath +"')").asDouble();
			
			System.out.println("The result is=" + myResult );
			
		} catch (RserveException e) {
			e.printStackTrace();
		} catch (REXPMismatchException e) {
			e.printStackTrace();
		}
		
		return myResult;
		
	}
	
	
	public static void main(String[] args) {
		double myMainRes = callScript();
		
	}
}
