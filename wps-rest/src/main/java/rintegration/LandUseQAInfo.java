package rintegration;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "LandUseQAInfo", description = "calling R script LandUseQAInfo")
public class LandUseQAInfo{
	
	private String resultsLink;

	public String getResultsLink(){
		return resultsLink;
	}
	
	
			
	public void setResultsLink(final String resultsLink) {
		
		this.resultsLink = resultsLink;
	}	

}
