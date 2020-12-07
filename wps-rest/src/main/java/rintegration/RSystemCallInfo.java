package rintegration;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "RSystemCallInfo", description = "testing R System call info")
public class RSystemCallInfo {
		
	private String sysInfo;

	public String getSysInfo(){
		return sysInfo;
	}
			
	public void setSysInfo(final String sysInfo) {		
		this.sysInfo = sysInfo;
	}	

}
