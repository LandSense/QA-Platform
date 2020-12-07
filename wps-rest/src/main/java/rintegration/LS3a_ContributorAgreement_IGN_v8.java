package rintegration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.geotools.feature.FeatureCollection;
import org.json.JSONException;
import org.json.JSONObject;

import common.JsonUtils;


public class LS3a_ContributorAgreement_IGN_v8 {
		
	
    private static String rScriptBinary = "Rscript"; // Need to have RScript on the sys path		
	private static String rScriptFileResource =  "rscripts/LS3a_ContributorAgreement_IGN_v8.R";	
	
	private static final String outputDataDir   =  System.getProperty("java.io.tmpdir");
				
	private String sysInfo = "";

	public LS3a_ContributorAgreement_IGN_v8(String inputData, String dateStr, String campaignPrefix,String redundancyDepth,  String scottsPiThresh) {

		try {
			sysInfo = getLS3a_ContributorAgreement_IGN_v8Info(inputData,dateStr,campaignPrefix,redundancyDepth, scottsPiThresh);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getLS3a_ContributorAgreement_IGN_v8Info(String inputData,String dateStr, String campaignPrefix,String redundancyDepth, String scottsPiThresh) throws IOException {
        Runtime rt = Runtime.getRuntime();       
        System.out.println("RScript binary path: " + rScriptBinary);
        
        //Get the r script from the resources
        ClassLoader classLoader = LS3a_ContributorAgreement_IGN_v8.class.getClassLoader();
        File file = new File(classLoader.getResource(rScriptFileResource).getFile());
        String rScriptFile = file.getAbsolutePath();		

        //Form the command        
        String[] commands = {rScriptBinary,rScriptFile,dateStr,campaignPrefix,redundancyDepth,scottsPiThresh,inputData};
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

		String dateStr =  "03062019";
		String campaignPrefix=  "TLS";
		String redundancyDepth = "2";
		String scottsPiThresh = "0.6";						 
		String inputData = "https://www.nottingham.ac.uk/~ezzjfr/validations2019_points_geoville2018_forlandsense.geojson";								
		
		String test = getLS3a_ContributorAgreement_IGN_v8Info(inputData,dateStr, campaignPrefix,redundancyDepth, scottsPiThresh);
		System.out.println("Return of R call ....");	
		System.out.println(test.toString());
	}
	
	public String getSysInfo() {
		return sysInfo;
	}
	

}
