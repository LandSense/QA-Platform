package externalservices;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;



public class FaceDetectorHTTPService {

	private int numberOfFaces = 0;
	private File processedImageFile;
	private File detectionsImageFile;
	private String imageName;
	
	
	private final static String myService = "http://127.0.0.1:5000/upload";
	public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
			
	private final String USER_AGENT = "Mozilla/5.0";

	public void sendPostMultiPart(URL url, double detectionThreshold) throws Exception {

		System.out.println("Preparing multi-part post with image from URL: " + url.toString());
		System.out.println("temp dir: " + TMP_DIR );
		System.out.println("detection threshold: " + detectionThreshold);

		//Get the name of the filename
		String inputFilename = FilenameUtils.getName(url.getPath());
		imageName = inputFilename;
		
		//Download the image to tmp dir so can form the post		
		File file = File.createTempFile("image_to_facedetect_", inputFilename);		

		System.out.println("temp dir: " + file.getPath());		
		BufferedImage img = ImageIO.read(url);
		BufferedImage imgForBlurring = ImageIO.read(url);
		
		ImageIO.write(img, "jpg", file);
			
		
		//Start creating the references for the http entity
		FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addPart("image", fileBody);
		HttpEntity entity = builder.build();
		HttpPost request = new HttpPost(myService);
		request.setEntity(entity);
		HttpClient client = HttpClientBuilder.create().build();
		
		//Execute the req
		try {
			client.execute(request);

			HttpResponse response = client.execute(request);
			HttpEntity resEntity = response.getEntity();
			System.out.println(response.getStatusLine());			
			
			//Write the image result to disk 
			InputStream is = response.getEntity().getContent();
			File imageWithDetectionsFile = File.createTempFile("image_with_detections_", inputFilename);

			FileOutputStream fos = new FileOutputStream(imageWithDetectionsFile);
			int read = 0;
			
			byte[] buffer = new byte[32768];
			while( (read = is.read(buffer)) > 0) {
			  fos.write(buffer, 0, read);
			}

			fos.close();
			is.close();
			
			System.out.println("imageWithDetectionsFile: " + imageWithDetectionsFile.getPath());		
			detectionsImageFile = imageWithDetectionsFile;			
					
			// Get the result headers
			Header[] allHeaders = response.getAllHeaders();			
			Header[] confHeader = response.getHeaders("confidence_scores");
			System.out.println("my conf scores: ");
			System.out.println(confHeader[0].toString());
			
			//get all headers		
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				System.out.println("Key : " + header.getName() 
				      + " ,Value : " + header.getValue());
			}
			numberOfFaces = countFaces(response, detectionThreshold);	
			System.out.println("numberOfFaces: " + numberOfFaces );			
			
			Header[] boxHeader = response.getHeaders("boxes_in_image");		
			System.out.println("boxHeader : " + boxHeader[0]);
			

			String boxesStr = response.getFirstHeader("boxes_in_image").getValue();
			System.out.println(" boxHeader : " + boxesStr.toString());	
			String regex = "\\[|\\]";			
			String boxesStr2 = boxesStr.replaceAll(regex, "");				
			List<String> arrayStr = Arrays.asList(boxesStr2.split(",")); 	//comma separated bbox values
			int i = 0;
			//Loop over every 4 vals to get bbox  
			while (i < numberOfFaces*4) {			
				int b1 = (int) Math.round((Float.valueOf(arrayStr.get(i)) * imgForBlurring.getHeight()));
				int b2 = (int) Math.round((Float.valueOf(arrayStr.get(i+1)) * imgForBlurring.getWidth()));
				int b3 = (int) Math.round((Float.valueOf(arrayStr.get(i+2)) * imgForBlurring.getHeight()));
				int b4 = (int) Math.round((Float.valueOf(arrayStr.get(i+3)) * imgForBlurring.getWidth()));								
				
				//size of the box
				int sizey= b3-b1;
				int sizex = b4-b2;						
				int shiftx = b2; //top left  x
				int shifty = b1; // top left y		
				
				//kernel size needs to reflect face size
				Kernel kernel = makeKernel((int) (sizey*0.35));
				
				//Get region with face. Do some buffer around it as ConvolveOP won't do the edges.
				int buffSizeX = sizex+(kernel.getWidth());
				int buffSizeY = sizey+(kernel.getHeight());

				int subWidth = shiftx-kernel.getWidth()/2;
				int subHeight = shifty-kernel.getWidth()/2;

				//ensure box blurring area fits within the extent of the image
				subWidth = (subWidth < 1) ? 0: subWidth; 
				subWidth = (subWidth > imgForBlurring.getWidth()) ? imgForBlurring.getWidth(): subWidth; 
				subHeight = (subHeight < 1) ? 0: subHeight; 
				subHeight = (subHeight > imgForBlurring.getHeight()) ? imgForBlurring.getHeight(): subHeight; 
							
				/*
				System.out.println(" subw2 : " + subWidth);	
				System.out.println(" subh2 : " + subHeight);
				
				System.out.println(" shiftx: " + shiftx);
				System.out.println(" shifty : " +  shifty);
				*/
				BufferedImage dest = imgForBlurring.getSubimage(subWidth, subHeight, buffSizeX, buffSizeY); 
				ColorModel cm = dest.getColorModel();
				BufferedImage src = new BufferedImage(cm, dest.copyData(dest.getRaster().createCompatibleWritableRaster()), cm.isAlphaPremultiplied(), null).getSubimage(0, 0, dest.getWidth(), dest.getHeight());
				BufferedImage blurredImage = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null).filter(src, dest);							

				i = i+4; //box coords
			}			
			
			File blurredFile = File.createTempFile("image_with_blurs_", inputFilename);
			ImageIO.write(imgForBlurring, "jpg", blurredFile);

			//Set the file image with the blurred faces as the processed image
			System.out.println("blurredFile: " + blurredFile.getPath());		
			processedImageFile = blurredFile;		
			
			System.out.println("Completed writing blurred image");		
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	
	//count number of faces detected in TF header response
	private int countFaces(HttpResponse response, double threshold) {
		String myConfScoresStr = response.getFirstHeader("confidence_scores").getValue();
		String regex = "\\[|\\]";
		myConfScoresStr  = myConfScoresStr.replaceAll(regex, "");		
		List<String> array = Arrays.asList(myConfScoresStr.split(","));
		int count = 0;
		for(int x = 0; x < array.size(); x++ ) {
			if (Double.parseDouble(array.get(x))  > threshold) { count++; } 
		}
		return count;
	}	
	
	
	private Kernel makeKernel(int size) {
		//Blurring part of image
		//Make a kernel for convolving
	    //int size = radius * 4 + 1;
	    float weight = 1.0f / (size * size);
	    float[] data = new float[size * size];
	    for (int i = 0; i < data.length; i++) {
	        data[i] = weight;
	    }		    
	    Kernel kernel = new Kernel(size, size, data);
		return kernel;			
	}

	//count number of faces detected in TF header response
	private int getBoxes(HttpResponse response) {
		String myConfScoresStr = response.getFirstHeader("boxes_in_image").getValue();
		System.out.println(" myConfScoresStr boxHeader : " + myConfScoresStr.toString());	
		String regex = "\\[|\\]";
		//myConfScoresStr  = myConfScoresStr.replaceAll(regex, "");		
		List<String> array = Arrays.asList(myConfScoresStr.split(","));
		
		int count = 0;

		return count;
	}	
	
	//getter for number of faces
	public int getNumberOfFaces() {
		return numberOfFaces;
	}
	
	//getter for processed image (i.e. with boxes or blurred faces)
	public File getProcessedImage() {
		return processedImageFile;
	}
	
	public String getImageName() {
		return imageName;
	}
	

	public static void main(String[] args) throws Exception {

		FaceDetectorHTTPService faceDetectorHTTPService = new FaceDetectorHTTPService();		
		//URL url = new URL("https://ichef.bbci.co.uk/news/660/cpsprodpb/3E0C/production/_104248851_mediaitem104248850.jpg");
		//URL url = new URL("https://landsense.eu/img/news/2018-11-21_year2.jpg");
	     //URL url = new URL("http://www.geo-wiki.org/assets/upload/201804/5adaf422da587719242356.jpg");
		//URL url = new URL("https://www.nottingham.ac.uk/~ezzjfr/landsense/5d1495b1b6f5a037570690.jpg");
		//URL url = new URL("https://www.nottingham.ac.uk/~ezzjfr/landsense/5d1495beaed5a041028981.jpg");
		//URL url = new URL("https://www.nottingham.ac.uk/~ezzjfr/landsense/5d1495b8435e8663341797.jpg");
		URL url = new URL("https://landsense.eu/img/news/2018-11-21_year2.jpg");
		
		
		double myThreshold = 0.2;
		faceDetectorHTTPService.sendPostMultiPart(url,myThreshold);
	}
	
	
}
