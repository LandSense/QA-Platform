package privacy;



import java.io.File;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "FaceDetectionInfo", description = "check for if faces are in photo")
public class FaceDetectionInfo {
	
	
	private int numOfFaces;
	private File processedImageFile;
	private String imageName;
	private String processedImageLocation; 
	
	public void setNumberOfFaces(final int numOfFaces) {		
		this.numOfFaces = numOfFaces;
		
	}
	
	public int getNumberOfFaces() {		
		return numOfFaces;
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