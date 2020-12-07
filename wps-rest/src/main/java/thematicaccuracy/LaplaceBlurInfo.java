package thematicaccuracy;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "LaplaceBlurInfo", description = "check for blur in photo using Laplace transform")
public class LaplaceBlurInfo {
	
	private int decision;
	private int maxValue;
	//private double percentBlurry;
	
	public int getDecision(){
		return decision;
	}
	
	public int getMaxValue() {
		return maxValue;
	}
	
	//public double getPercentBlurry() {
		//return percentBlurry;
	//}
	
	
	public void setDecision(final int decision) {
		
		this.decision = decision;
	}
	
	public void setMaxValue(final int maxValue) {
		this.maxValue = maxValue;
	}
	
	//public void setPercentBlurry(final double percentBlurry) {
	//	this.percentBlurry = percentBlurry;
	//}

}
