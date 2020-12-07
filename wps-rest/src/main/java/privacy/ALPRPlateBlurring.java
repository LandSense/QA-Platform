package privacy;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.openalpr.jni.Alpr;
import com.openalpr.jni.AlprCoordinate;
import com.openalpr.jni.AlprException;
import com.openalpr.jni.AlprPlate;
import com.openalpr.jni.AlprPlateResult;
import com.openalpr.jni.AlprResults;

import rintegration.LandUseQA;

public class ALPRPlateBlurring {
	//openalpr configuration
	private String openALPRConfFile = "openalpr.conf"; //This will be found in the res dir
	private String openALPRRuntimeData = "runtime_data"; // this will be found in the res dir
	private String openALPRRegionForPlates = "eu"; // specify a plate region type e.g. eu, gb, us 	
	
	private int numberOfPlates = 0;
	private File processedImageFile;
	private String imageName;

	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");

	public void blurPlates(URL url, double detectionThreshold) throws IOException {
		System.out.println("blurPlates...");

        //Get the config and runtime files from the resources dir
        ClassLoader classLoader = ALPRPlateBlurring.class.getClassLoader();
        File confFile = new File(classLoader.getResource(openALPRConfFile).getFile());
        String openALPRConfFileString = confFile.getAbsolutePath();
        File runtimeDataFile = new File(classLoader.getResource(openALPRRuntimeData).getFile());
        String runtimeDataString = runtimeDataFile.getAbsolutePath();
        
		// Get the name of the image filename
		String inputFilename = FilenameUtils.getName(url.getPath());
		imageName = inputFilename;

		// Download the image
		File file = File.createTempFile("image_to_platedetect_", inputFilename);

		System.out.println("temp dir: " + file.getPath());
		BufferedImage img = ImageIO.read(url);
		// BufferedImage imgForBlurring = ImageIO.read(url);
		ImageIO.write(img, "jpg", file);
		String imagePath = file.getPath();

		// Set up ALPR objects
		Alpr alpr = new Alpr(openALPRRegionForPlates,openALPRConfFileString, runtimeDataString );
		// Set top N candidates returned to 20
		alpr.setTopN(50);

		// Run the detector
		AlprResults results;
		try {
			results = alpr.recognize(imagePath);
			numberOfPlates = results.getPlates().size();
			System.out.println("number of Results: " + numberOfPlates);
			List<AlprCoordinate> plateCoords = null;

			File imageFile = new File(imagePath);
			BufferedImage imgForBlurring = ImageIO.read(imageFile);
			
			// If there are plates found
			if (numberOfPlates > 0) {


				System.out.format("  %-15s%-8s\n", "Plate Number", "Confidence");

				//Loop through the plate results 
				for (AlprPlateResult result : results.getPlates()) {
					System.out.format("number of Results" + results.getPlates().size());
					for (AlprPlate plate : result.getTopNPlates()) {
			
						//Convert the confidence to percentage.
						double plateConfPercentange = plate.getOverallConfidence()*0.01;
						System.out.print(" plateConfPercentange " + plateConfPercentange);
						
						if (plateConfPercentange >= detectionThreshold) {
							plateCoords = result.getPlatePoints();
							System.out.print("alprCoordX " + plateCoords.get(0).getX());
							System.out.print(" alprCoordY " + plateCoords.get(0).getY());
							System.out.print(" plateCoordsSize" + plateCoords.size());
	
							int i = 0;
							AlprCoordinate p1 = plateCoords.get(0);
							AlprCoordinate p2 = plateCoords.get(1);
							AlprCoordinate p3 = plateCoords.get(2);
							AlprCoordinate p4 = plateCoords.get(3);
	
							System.out.print("points: " + p1 + p2 + p3 + p4);
	
							// Create a box (based on top left point in image)
							int x = p1.getX();
							int y = p1.getY();
							int boxHeight = p3.getX() - p1.getX();
							int boxWidth = p3.getY() - p1.getY();
	
							// create the blurred image data based on bbox
							imgForBlurring = blurBBoxInImage(x, y, boxWidth, boxHeight, imgForBlurring);
						
						} // end check plate confidence.
					}
				}
			} else {
				System.out.println("No plates found in image");
			}
			
			// Create a file for the blurred / resulting image.
			File blurredFile = File.createTempFile("image_with_blurs_", ".jpg");

			// Write image data to the file
			ImageIO.write(imgForBlurring, "jpg", blurredFile);

			// Set the file with the blurred plates as the processed image which will then get used by the API as the image to return
			System.out.println("blurredFile: " + blurredFile.getPath());
			processedImageFile = blurredFile;		
			
		} catch (AlprException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Kernel makeKernel(int size) {
		// Blurring part of image
		// Make a kernel for convolving
		// int size = radius * 4 + 1;
		float weight = 1.0f / (size * size);
		float[] data = new float[size * size];
		for (int i = 0; i < data.length; i++) {
			data[i] = weight;
		}
		Kernel kernel = new Kernel(size, size, data);
		return kernel;
	}

	private static BufferedImage blurBBoxInImage(int x, int y, int w, int h, BufferedImage imgForBlurring) {
		System.out.println("blurringBBox in  image");
		
		//Create a rectangle within which the blurring will happen 
		Rectangle2D rectangle = new Rectangle(new Point(y, x), new Dimension(w, h));

		int b1 = (int) rectangle.getX();
		int b2 = (int) rectangle.getY();
		int b3 = (int) rectangle.getMaxX();
		int b4 = (int) rectangle.getMaxY();

		// size of the box
		int sizey = b3 - b1;
		int sizex = b4 - b2;
		int shiftx = b2; // top left x
		int shifty = b1; // top left y

		// kernel size needs to reflect plate size
		Kernel kernel = makeKernel((int) (sizey * 0.35));

		// Get region with the plate. Do some buffer around it as ConvolveOP won't do the
		// edges and it looks a bit rubbish.
		int buffSizeX = sizex + (kernel.getWidth());
		int buffSizeY = sizey + (kernel.getHeight());

		int topLeftX = shiftx - kernel.getWidth() / 2;
		int topLeftY = shifty - kernel.getWidth() / 2;

		// ensure box blurring area fits within the extent of the full image
		// first check the top left point is in the image, if outside set it as the edge
		topLeftX = (topLeftX < 1) ? 0 : topLeftX;
		topLeftX = (topLeftX > imgForBlurring.getWidth()) ? imgForBlurring.getWidth() : topLeftX;
		topLeftY = (topLeftY < 1) ? 0 : topLeftY;
		topLeftY = (topLeftY > imgForBlurring.getHeight()) ? imgForBlurring.getHeight() : topLeftY;

		// now check the rectangular box won't run over the edge once it is placed
		int maxBoxX = topLeftX + buffSizeX + 1;
		int maxBoxY = topLeftY + buffSizeY + 1;
		System.out.print(" maxBoxX:" + maxBoxX + " maxBoxY:" + maxBoxY);
		System.out.println("");
		System.out.print("  imgForBlurring.getWidth():" + imgForBlurring.getWidth() + " imgForBlurring.getHeight():" + imgForBlurring.getHeight());
		//If the box will go over the edge, adjust its dimensions to the edge
		if (maxBoxX > imgForBlurring.getWidth()) {
			buffSizeX = buffSizeX - (maxBoxX - imgForBlurring.getWidth());
		}

		if (maxBoxY > imgForBlurring.getHeight()) {
			buffSizeY = buffSizeY - (maxBoxY - imgForBlurring.getHeight());
		}

		System.out.print(" x:" + topLeftX + " y:" + topLeftY + " w:" + buffSizeX + " h: " + buffSizeY);
		System.out.println("");
		
		//Can now use the full image and rectangle area to the blurring
		BufferedImage dest = imgForBlurring.getSubimage(topLeftX, topLeftY, buffSizeX, buffSizeY);
		ColorModel cm = dest.getColorModel();
		BufferedImage src = new BufferedImage(cm, dest.copyData(dest.getRaster().createCompatibleWritableRaster()),
				cm.isAlphaPremultiplied(), null).getSubimage(0, 0, dest.getWidth(), dest.getHeight());
		BufferedImage blurredImage = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null).filter(src, dest);

		return imgForBlurring;
	}

	// getter for number of faces
	public int getNumberOfPlates() {
		return numberOfPlates;
	}

	// getter for processed image (i.e. with boxes or blurred faces)
	public File getProcessedImage() {
		return processedImageFile;
	}

	public String getImageName() {
		return imageName;
	}

	public static void main(String[] args) throws AlprException, IOException {

		ALPRPlateBlurring alprPlateBlurring = new ALPRPlateBlurring();

		//URL url = new URL("https://www.nottingham.ac.uk/~ezzjfr/landsense/HPIM1271_edge2.jpg");
		//URL url = new URL("https://www.nottingham.ac.uk/~ezzjfr/landsense/HPIM1271_edge2_rotate2.jpg");
		//URL url = new URL("https://www.nottingham.ac.uk/~ezzjfr/landsense/HPIM1271_edge2_upsidedown.jpg");
		
		URL url = new URL("https://blog.iiasa.ac.at/wp-content/uploads/sites/5/dreamstime_m_60197159.jpg");
		
		double myThreshold = 0.02;
		alprPlateBlurring.blurPlates(url, myThreshold);
	}

}
