package thematicaccuracy;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "BlurAWTInfo", description = "Checks for blur by 1) converting the image to greyscale and resizing it 2) convolving the image with a Laplace transform 3) taking the variance of the convolved image and assessing this against a user-defined threshold. Low variance values indicate blurriness")
public class BlurAWTInfo {
	
	private boolean isSharp;
	private long variance;
	//private double percentBlurry;
	
	public boolean getIsSharp(){
		return isSharp;
	}
	
	public long getVariance() {
		return variance;
	}	
	
	public void setIsSharp(final boolean isSharp) {		
		this.isSharp = isSharp;
	}
	
	public void setVariance(final long variance) {
		this.variance = variance;
	}


}
