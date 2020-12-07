package thematicaccuracy;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "FlowerClassificationInfo", description = "Classify a flower photo")
public class FlowerClassificationInfo {
	
	
	private boolean match;
	
	public boolean isMatch() {
		return match;
	}
	
	public void setMatch(final boolean match) {
		this.match=match;
	}
	
	

}
