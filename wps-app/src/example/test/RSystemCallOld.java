package example.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class RSystemCallOld {
		
	private static String rScriptBinary= "RScript"; // Need to have RScript on the sys path	
	//private static String rScriptFile = new File("").getAbsolutePath()  + "/rscripts/myScript.R";	
	private static String rScriptFile = "C:\\Users\\ezzjfr\\Documents\\lucas_git\\landsenseR\\myScript.R";
	
	private String sysInfo = "";

	public RSystemCallOld() {

		try {
			sysInfo = getRSystemInfo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getRSystemInfo() throws IOException {
        Runtime rt = Runtime.getRuntime();
       
        System.out.println(rScriptBinary);
        System.out.println(rScriptFile);
        
        String[] commands = {rScriptBinary,rScriptFile};
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
        
		String test = getRSystemInfo();
		System.out.println("Return of R call ....");
		System.out.println(test.toString());		
		
		System.out.println("Main Finished testing R call ....");			
	}
	
	public String getSysInfo() {
		return sysInfo;
	}
	

}
