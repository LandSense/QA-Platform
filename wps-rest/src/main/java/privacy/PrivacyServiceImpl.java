package privacy;


import java.net.URL;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import externalservices.FaceDetectorHTTPService;


public class PrivacyServiceImpl implements PrivacyService {

    @Context ServletContext context;

    
	@Override
	public FaceDetectionInfo checkFaceDetection(final URL imageLocation, final double detectionThreshold) {
	
		//Need to implement the class in the other module
		//FaceDetectionCheck fc = new FaceDetectionCheck();

		System.out.println("Constructor " + context);  // null here  
		
		FaceDetectorHTTPService faceDetectorHTTPService = new FaceDetectorHTTPService();
		try {
			faceDetectorHTTPService.sendPostMultiPart(imageLocation, detectionThreshold);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		FaceDetectionInfo info = new FaceDetectionInfo();
		
		System.out.println("faceDetectorHTTPService getNumberOfFaces " + faceDetectorHTTPService.getNumberOfFaces());  		
		
		info.setImageName(faceDetectorHTTPService.getImageName());
		info.setNumberOfFaces(faceDetectorHTTPService.getNumberOfFaces());		
		info.setProcessedImageFile(faceDetectorHTTPService.getProcessedImage());
		
		System.out.println("Check face context " + context);  // null here
		
		return info;
	}

	
	


	@Override
	public PlateDetectionInfo checkPlateDetection(final URL imageLocation, final double detectionThreshold) {
	
		System.out.println("");  // null here  		


		ALPRPlateBlurring alprPlateBlurring= new ALPRPlateBlurring();
		
		try {
			alprPlateBlurring.blurPlates(imageLocation,detectionThreshold); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		PlateDetectionInfo info = new PlateDetectionInfo();
		
		System.out.println("Privacy service plate detection");  		
		
		info.setImageName(alprPlateBlurring.getImageName());
		info.setNumberOfPlates(alprPlateBlurring.getNumberOfPlates());		
		info.setProcessedImageFile(alprPlateBlurring.getProcessedImage());
		
		System.out.println("Check plate context " + context);  // null here  	
		
		return info;
	}

	


	
	
}

