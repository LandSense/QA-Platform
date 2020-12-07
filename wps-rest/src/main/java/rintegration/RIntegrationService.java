package rintegration;

import java.net.URL;
import java.util.List;

/**
 * Provider of thematic accuracy related services 
 */
public interface RIntegrationService {
	
	

	public RSystemCallInfo getRSystemDetails(String imageLocation);
	
	/**
	 * getRSystemGeoReadWriteDetails
	 * @param 
	 * @param 
	 * @return R system geoReadWrite
	 */
	public RSystemGeoReadWriteInfo getRSystemGeoReadWriteDetails(String fileLocation);
	
	
	/**
	 * getLandUseQA
	 * @param inputData
	 * @param campaignPrefix
	 * @param redundancyDepth
	 * @param samplingPopulation
	 * @return QA report
	 */
	public LandUseQAInfo getLandUseQA(String inputData,String campaignPrefix,String redundancyDepth, String samplingPopulation);
	
	
	public LS3a_ContributorAgreement_IGN_v8Info getLS3a_ContributorAgreement_IGN_v8(String inputData,String dateStr, String campaignPrefix,String redundancyDepth, String scottsPiThresh);
	
	
}
