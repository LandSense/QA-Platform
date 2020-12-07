package rintegration;

import com.fasterxml.jackson.annotation.JsonRawValue;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "RSystemGeoReadWriteInfo", description = "testing R System for reading and writing")
public class RSystemGeoReadWriteInfo {
	
	@JsonRawValue
	private String sysInfo;

	public String getSysInfo(){
		return sysInfo;
	}
			
	public void setSysInfo(final String sysInfo) {
		
		this.sysInfo = sysInfo;
	}	

}
