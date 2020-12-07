package privacy;

import java.net.URL;
import java.util.List;

/**
 * Provider of privacy related services 
 */
public interface PrivacyService {
	

	public FaceDetectionInfo checkFaceDetection(URL imageLocation, double detectionThreshold);
	
	/**
	 * checkFaceDetection
	 * @param 
	 * @param 
	 * @return Face detection information - location
	 */
	
	
	public PlateDetectionInfo checkPlateDetection(URL imageLocation, double detectionThreshold);
	
	/**
	 * checkPlateDetection
	 * @param 
	 * @param 
	 * @return Plate detection information - location
	 */
}
