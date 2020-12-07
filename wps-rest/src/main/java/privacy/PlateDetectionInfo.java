package privacy;



import java.io.File;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "PlateDetectionInfo", description = "check for if Plates are in photo")
public class PlateDetectionInfo {
	
	
	private int numOfPlates;
	private File processedImageFile;
	private String imageName;
	private String processedImageLocation; 
	
	public void setNumberOfPlates(final int numOfPlates) {		
		this.numOfPlates = numOfPlates;
		
	}
	
	public int getNumberOfPlates() {		
		return numOfPlates;
	}
	
	public void setProcessedImageFile(final File processedImageFile ) {		
		this.processedImageFile = processedImageFile ;
	}
	
	public File getProcessedImageFile() {		
		return processedImageFile;
	}

	public void setProcessedImageLocation(final String processedImageLocation) {		
		this.processedImageLocation = processedImageLocation;
	}
	
	public String getProcessedImageLocation() {		
		return processedImageLocation;
	}
	
	public String getImageName() {		
		return imageName;
	}
	
	public void setImageName(final String imageName) {		
		this.imageName = imageName;
	}

}