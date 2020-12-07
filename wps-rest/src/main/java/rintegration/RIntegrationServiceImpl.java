package rintegration;

import java.net.URL;

import org.geotools.feature.FeatureCollection;

import common.JsonUtils;
//import example.test.RSystemCall;
import example.test.RSystemGeoReadWrite;
import example.test.TryRServeJSONScript;
import logicalconsistency.SpatialDuplicateInfo;

public class RIntegrationServiceImpl implements RIntegrationService {



	@Override
	public RSystemCallInfo getRSystemDetails(final String imageLocation) {
		
			//Call the class in the web-app
			RSystemCall rSysCall = new RSystemCall();
			
			//Populate an object for the API return data
			RSystemCallInfo data = new RSystemCallInfo();
			data.setSysInfo(rSysCall.getSysInfo());
			
			return data;					
		}

	
	@Override
	public RSystemGeoReadWriteInfo getRSystemGeoReadWriteDetails(String fileLocation) {
		//Call the class in the web-app
		RSystemGeoReadWrite RSysGeoReadWrite = new RSystemGeoReadWrite(fileLocation);
		
		//Populate an object for the API return data
		RSystemGeoReadWriteInfo data = new RSystemGeoReadWriteInfo();
		data.setSysInfo(RSysGeoReadWrite.getSysInfo());
		
		return data;			
	}


	@Override
	public LandUseQAInfo getLandUseQA(String inputData,String campaignPrefix,String redundancyDepth, String samplingPopulation) {
		// TODO Auto-generated method stub
		//Call the class in the web-app
		
		LandUseQA landUseQA= new LandUseQA(inputData,campaignPrefix,redundancyDepth, samplingPopulation);
		
		//Populate an object for the API return data
		LandUseQAInfo data = new LandUseQAInfo();
		data.setResultsLink(landUseQA.getSysInfo());
		
		return data;			
	}


	@Override
	public LS3a_ContributorAgreement_IGN_v8Info getLS3a_ContributorAgreement_IGN_v8(String inputData, String dateStr,
			String campaignPrefix, String redundancyDepth, String scottsPiThresh) {
		// TODO Auto-generated method stub
		
		LS3a_ContributorAgreement_IGN_v8 ls3a_ContributorAgreement_IGN_v8 = new LS3a_ContributorAgreement_IGN_v8(inputData,dateStr,campaignPrefix,redundancyDepth, scottsPiThresh);
		
		//Populate an object for the API return data
		LS3a_ContributorAgreement_IGN_v8Info data = new LS3a_ContributorAgreement_IGN_v8Info();
		data.setResultsLink(ls3a_ContributorAgreement_IGN_v8.getSysInfo());
	
		return data;	
	}
	
	
}
