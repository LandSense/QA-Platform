package thematicaccuracy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import iso19157.thematicaccuracy.classificationcorrectness.BlurCheckAwt;
import iso19157.thematicaccuracy.classificationcorrectness.CheckImageClassification;
import iso19157.thematicaccuracy.classificationcorrectness.LaplaceBlurCheck;

public class ThematicAccuracyServiceImpl implements ThematicAccuracyService {

	@Override
	public ImageClassificationInfo checkImageClassification(final String imagePath, final String submittedConcept,
			final double confidenceThreshold) {

		ImageClassificationInfo info = new ImageClassificationInfo();
		CheckImageClassification check = new CheckImageClassification(imagePath, submittedConcept, confidenceThreshold);

		info.setMatch(check.getMatchDecision());
		info.setClassifications(check.getNumberOfCorrectClassifications());
		info.setMisclassifications(check.getNumberOfMisClassifications());
		info.setClassificationRate(check.getClassificationRate());
		info.setMisclassificationRate(check.getMisClassificationRate());

		return info;
	}

	@Override
	public ImageClassificationInfo checkImageClassifications(final List<LocationConceptPair> imagePaths, final double confidenceThreshold) {

		ImageClassificationInfo info = new ImageClassificationInfo();
		
		String[][] imageArray = new String[imagePaths.size()][2];
		
		for(int i = 0; i < imageArray.length; ++i) {
			imageArray[i][0] = imagePaths.get(i).getLocation();
			imageArray[i][1] = imagePaths.get(i).getConcept();
		}
		
		CheckImageClassification check = new CheckImageClassification(imageArray, confidenceThreshold);

		info.setMatch(check.getMatchDecision());
		info.setClassifications(check.getNumberOfCorrectClassifications());
		info.setMisclassifications(check.getNumberOfMisClassifications());
		info.setClassificationRate(check.getClassificationRate());
		info.setMisclassificationRate(check.getMisClassificationRate());

		return info;
	}
	@Override
	public FlowerClassificationInfo checkFlowerClassification(final List<LocationConceptPair> imagePaths, final double confidenceThreshold) {
		
		String[][] imageArray = new String[imagePaths.size()][2];
		
		for(int i = 0; i < imageArray.length; i++) {
			imageArray[i][0] = imagePaths.get(i).getLocation();
			imageArray[i][1] = imagePaths.get(i).getConcept();
		}
		
		FlowerClassificationInfo info = new FlowerClassificationInfo();
		
		return info;
		
	}
	
	@Override
	public LaplaceBlurInfo checkLaplaceBlur (final URL imageLocation, final int threshold) {
		
		LaplaceBlurCheck bc = new LaplaceBlurCheck(imageLocation, threshold);
		
		LaplaceBlurInfo info = new LaplaceBlurInfo();
		
		info.setDecision(bc.getDecision());
		info.setMaxValue(bc.getMaxValue());
		//info.setPercentBlurry(bc.getPercentBlurry());		
		return info;		
	}

	@Override
	public BlurAWTInfo checkBlurAWT(URL imageLocation, int threshold) {
		// TODO Auto-generated method stub
		File testImage;
		String TMP_DIR = System.getProperty("java.io.tmpdir");

		System.out.println("Preparing multi-part post with image from URL: " + imageLocation.toString());
		System.out.println("temp dir: " + TMP_DIR );		
		//Download the image to tmp dir 
		File file;
        BlurAWTInfo info = new BlurAWTInfo();		  
		try {
			file = File.createTempFile("image_to_blur_detect_", FilenameUtils.getName(imageLocation.getPath()));
			System.out.println("temp dir: " + file.getPath());		
			BufferedImage img = ImageIO.read(imageLocation);
			ImageIO.write(img, "jpg", file);
			
			testImage = file;
			BlurCheckAwt lap = new BlurCheckAwt(testImage, threshold, true);
	        lap.run();		       
      
			info.setIsSharp(lap.pass);
			info.setVariance(lap.variance);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	

		return info;
	}

}
