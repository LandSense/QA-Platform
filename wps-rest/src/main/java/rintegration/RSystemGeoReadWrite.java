package rintegration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.geotools.feature.FeatureCollection;

public class RSystemGeoReadWrite {
	
	private static String rScriptBinary= "Rscript"; // Need to have RScript on the sys path	
	private static String rScriptFileResource =  "rscripts/myGeoReadWriteSystem.R";		
	
	private String sysInfo = "";

	public RSystemGeoReadWrite(String fileName) {		
		try {
			sysInfo = getRSystemInfo(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getRSystemInfo(String fileName) throws IOException {
        Runtime rt = Runtime.getRuntime();

        System.out.println("RScript binary path: " + rScriptBinary);
        
        //Get the r script from the resources
        ClassLoader classLoader = RSystemCall.class.getClassLoader();
        File file = new File(classLoader.getResource(rScriptFileResource).getFile());
        String rScriptFile = file.getAbsolutePath();		
        System.out.println(rScriptFile);
        
        String[] commands = {rScriptBinary,rScriptFile,fileName};
        Process proc = rt.exec(commands);

        BufferedReader stdInput = new BufferedReader(new 
             InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new 
             InputStreamReader(proc.getErrorStream()));

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        String s = null;
        String output = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
            output = s;
        }
        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }        
        return output;        
	}
	
	public static void main (String args[]) throws IOException, InterruptedException {		
		System.out.println("Main testing R call ....");		
        
		String fileName = "C:/Users/ezzjfr/Downloads/vienna_testcase2.json";		
        
		String test = getRSystemInfo(fileName);
		System.out.println("Return of R call ....");
		System.out.println(test.toString());		
		
		System.out.println("Main Finished testing R call ....");		
		
		System.out.println(test);		
	}
	
	public String getSysInfo() {
		return sysInfo;
	}
	

}
