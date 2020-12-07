package rintegration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;


public class LandUseQA {
		
	private static String rScriptBinary = "Rscript"; // Need to have RScript on the sys path	
	private static String rScriptFileResource =  "rscripts/LandUse_qa_v1.R";		

	private static final String outputDataDir   =  System.getProperty("java.io.tmpdir");
				
	private String sysInfo = "";

	public LandUseQA(String inputData,String campaignPrefix,String redundancyDepth, String samplingPopulation) {

		try {
			sysInfo = getLandUseQAInfo(inputData,campaignPrefix,redundancyDepth, samplingPopulation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getLandUseQAInfo(String inputData,String campaignPrefix,String redundancyDepth, String samplingPopulation) throws IOException {
        Runtime rt = Runtime.getRuntime();       
        System.out.println("RScript binary path: " + rScriptBinary);
        
        //Get the r script from the resources
        ClassLoader classLoader = LandUseQA.class.getClassLoader();
        File file = new File(classLoader.getResource(rScriptFileResource).getFile());
        String rScriptFile = file.getAbsolutePath();		

        //Form the command        
        String[] commands = {rScriptBinary,rScriptFile,inputData,outputDataDir,campaignPrefix,redundancyDepth,samplingPopulation};
        System.out.println("Commands: " + java.util.Arrays.toString(commands));
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
        
		String inputDataDir =  "C:/Users/ezzjfr/Documents/lucas_git/landsenseR/LandSense_QA/LandUse_qa/In/";
		//outputDataDir = "C:/Users/ezzjfr/Documents/lucas_git/landsenseR/LandSense_QA/LandUse_qa/Out/";		
		String campaignPrefix=  "swaggerNew";
		String redundancyDepth = "5";
		String samplingPopulation = "150";					
		
		String test = getLandUseQAInfo(inputDataDir,campaignPrefix,redundancyDepth, samplingPopulation);
		System.out.println("Return of R call ....");
		System.out.println(test.toString());		
				
		System.out.println("Main Finished testing R call ....");			
	}
	
	public String getSysInfo() {
		return sysInfo;
	}
	

}