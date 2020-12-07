package thematicaccuracy;

import java.net.URL;
import java.util.List;

/**
 * Provider of thematic accuracy related services 
 */
public interface ThematicAccuracyService {

	/**
	 * Check the classification of an image
	 * @param imagePath The path to the image
	 * @param submittedConcept The submitted concept
	 * @param confidenceThreshold The confidence threshold
	 * @return The image classification information
	 */
	public ImageClassificationInfo checkImageClassification(String imagePath, String submittedConcept, double confidenceThreshold);
	
	/**
	 * Check the classification of an image
	 * @param imagePaths The paths to the images and concepts
	 * @param confidenceThreshold The confidence threshold
	 * @return The image classification information
	 */
	public ImageClassificationInfo checkImageClassifications(List<LocationConceptPair> imagePaths, double confidenceThreshold);

	/**
	 * Check the classification of a flower image using TensorFlow
	 * @param imagePaths
	 * @param confidenceThreshold
	 * @return The image classification information
	 */
	public FlowerClassificationInfo checkFlowerClassification(List<LocationConceptPair> imagePaths, double confidenceThreshold);
	
	
	/**
	 * Check the blurriness of an image using Laplace transform
	 * @param imageLocation
	 * @param threshold
	 * @return The image classification information
	 */

	public LaplaceBlurInfo checkLaplaceBlur(URL imageLocation, int threshold);

	public BlurAWTInfo checkBlurAWT(URL imageLocation, int threshold);
	
	/**
	 * Checks for blur by 1) converting the image to greyscale and resizing it 2) convolving the image with a Laplace transform 3) taking the variance of the convolved image and assessing this against a user-defined threshold. Low variance values indicate blurriness
	 * @param imageLocation
	 * @param threshold
	 * @return A blurriness metric (variance of the laplace transform convolution). Boolean decision of isBlurry.
	 */
}
