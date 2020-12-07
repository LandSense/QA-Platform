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
import java.util.List;

import javax.imageio.ImageIO;

import com.openalpr.jni.Alpr;
import com.openalpr.jni.AlprCoordinate;
import com.openalpr.jni.AlprException;
import com.openalpr.jni.AlprPlate;
import com.openalpr.jni.AlprPlateResult;
import com.openalpr.jni.AlprRegionOfInterest;
import com.openalpr.jni.AlprResults;
import com.openalpr.jni.json.JSONObject;

public class ALPRDemoMain {

	public static void main(String[] args) throws AlprException, IOException {
		// TODO Auto-generated method stub

		Alpr alpr = new Alpr("eu", "C:\\openalpr_64\\openalpr.conf", "runtime_data");

		// Set top N candidates returned to 20
		alpr.setTopN(50);

		// path to image
		String imagePath;
		imagePath = "C:\\tensorflow\\tensorflow-anpr\\small_sample\\HPIM1271.JPG"; // success
		imagePath = "C:\\tensorflow\\tensorflow-anpr\\small_sample\\HPIM1232 - tooclose.JPG"; // success
		// imagePath =
		// "C:\\tensorflow\\tensorflow-anpr\\small_sample\\HPIM1271_partial.JPG";
		// //failure
		imagePath = "C:\\tensorflow\\tensorflow-anpr\\small_sample\\HPIM1271_partial.JPG"; // failure
		// imagePath =
		// "C:\\tensorflow\\tensorflow-anpr\\difficult_color_more_than_one\\11190002.jpg";
		// //failure
		imagePath = "C:\\tensorflow\\tensorflow-anpr\\small_sample\\HPIM1271_edge.JPG"; // failure
		imagePath = "C:\\tensorflow\\tensorflow-anpr\\small_sample\\HPIM1271_edge3.JPG"; // failure
		imagePath = "C:\\tensorflow\\tensorflow-anpr\\difficult_color_more_than_one\\11250002.jpg";
		// Run the detector
		AlprResults results = alpr.recognize(imagePath);
		int numberOfPlatesFound = results.getPlates().size();
		System.out.println("number of Results: " + numberOfPlatesFound);
		List<AlprCoordinate> plateCoords = null;

		// If there are plates found
		if (numberOfPlatesFound > 0) {

			File imageFile = new File(imagePath);
			BufferedImage imgForBlurring = ImageIO.read(imageFile);
			BufferedImage imgWithBlur = null;
			System.out.format("  %-15s%-8s\n", "Plate Number", "Confidence");

			for (AlprPlateResult result : results.getPlates()) {
				System.out.format("number of Results" + results.getPlates().size());
				for (AlprPlate plate : result.getTopNPlates()) {
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
					imgWithBlur = blurBBoxInImage(x, y, boxWidth, boxHeight, imgForBlurring);

					if (plate.isMatchesTemplate())
						System.out.print("  * ");
					else
						System.out.print("  - ");
					System.out.format("%-15s%-8f\n", plate.getCharacters(), plate.getOverallConfidence());
				}
			}

			// Create a file for the blurred image
			File blurredFile = File.createTempFile("image_with_blurs_", ".jpg");

			// Write blurred image data to the file
			ImageIO.write(imgWithBlur, "jpg", blurredFile);

			// Set the file image with the blurred faces as the processed image
			System.out.println("blurredFile: " + blurredFile.getPath());
		} else {
			System.out.println("No plates found in image");
		}
	}

	private static BufferedImage blurBBoxInImage(int x, int y, int w, int h, BufferedImage imgForBlurring) {

		Rectangle2D rectangle = new Rectangle(new Point(y, x), new Dimension(w, h));
		// Rectangle2D rectangle = new Rectangle(new Point(p1.getY(), p1.getX()), new
		// Dimension(100, 25));

		System.out.println("rectangle " + rectangle.getX());
		int b1 = (int) rectangle.getX();
		int b2 = (int) rectangle.getY();
		int b3 = (int) rectangle.getMaxX();
		int b4 = (int) rectangle.getMaxY();

		// size of the box
		int sizey = b3 - b1;
		int sizex = b4 - b2;
		int shiftx = b2; // top left x
		int shifty = b1; // top left y

		// kernel size needs to reflect face size
		Kernel kernel = makeKernel((int) (sizey * 0.35));

		// Get region with face. Do some buffer around it as ConvolveOP won't do the
		// edges.
		int buffSizeX = sizex + (kernel.getWidth());
		int buffSizeY = sizey + (kernel.getHeight());

		int topLeftX = shiftx - kernel.getWidth() / 2;
		int topLeftY = shifty - kernel.getWidth() / 2;

		// ensure box blurring area fits within the extent of the image
		// check the top left point is in the image, if outside set is as the edge
		topLeftX = (topLeftX < 1) ? 0 : topLeftX;
		topLeftX = (topLeftX > imgForBlurring.getWidth()) ? imgForBlurring.getWidth() : topLeftX;
		topLeftY = (topLeftY < 1) ? 0 : topLeftY;
		topLeftY = (topLeftY > imgForBlurring.getHeight()) ? imgForBlurring.getHeight() : topLeftY;

		// check the box won't run over the edge
		int maxBoxX = topLeftX + buffSizeX + 1;
		int maxBoxY = topLeftY + buffSizeY + 1;
		System.out.print(" maxBoxX:" + maxBoxX + " maxBoxY:" + maxBoxY);
		System.out.println("");
		System.out.print("  imgForBlurring.getWidth():" + imgForBlurring.getWidth() + " imgForBlurring.getHeight():"
				+ imgForBlurring.getHeight());
		// buffSizeX = (maxBoxX > imgForBlurring.getWidth()) ?
		// (imgForBlurring.getWidth() imgForBlurring.getWidth())_ : buffSizeX;
		// buffSizeY = (maxBoxY > imgForBlurring.getHeight()) ?
		// imgForBlurring.getHeight() : buffSizeY;

		if (maxBoxX > imgForBlurring.getWidth()) {
			buffSizeX = buffSizeX - (maxBoxX - imgForBlurring.getWidth());
		}

		if (maxBoxY > imgForBlurring.getHeight()) {
			buffSizeY = buffSizeY - (maxBoxY - imgForBlurring.getHeight());
		}

		System.out.print(" x:" + topLeftX + " y:" + topLeftY + " w:" + buffSizeX + " h: " + buffSizeY);
		System.out.println("");
		BufferedImage dest = imgForBlurring.getSubimage(topLeftX, topLeftY, buffSizeX, buffSizeY);

		ColorModel cm = dest.getColorModel();
		BufferedImage src = new BufferedImage(cm, dest.copyData(dest.getRaster().createCompatibleWritableRaster()),
				cm.isAlphaPremultiplied(), null).getSubimage(0, 0, dest.getWidth(), dest.getHeight());
		BufferedImage blurredImage = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null).filter(src, dest);

		return imgForBlurring;
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

}
