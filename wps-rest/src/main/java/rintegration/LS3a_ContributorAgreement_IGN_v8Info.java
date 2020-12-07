package rintegration;

import com.fasterxml.jackson.annotation.JsonRawValue;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "LS3a_ContributorAgreement_IGN_v8Info", description = "calling R script LS3a_ContributorAgreement_IGN_v8Info")
public class LS3a_ContributorAgreement_IGN_v8Info{
	
	@JsonRawValue
	private String resultsLink;

	public String getResultsLink(){
		return resultsLink;
	}
		
			
	public void setResultsLink(final String resultsLink) {
		
		this.resultsLink = resultsLink;
	}	

}
