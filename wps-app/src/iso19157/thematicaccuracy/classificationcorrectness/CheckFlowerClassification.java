package iso19157.thematicaccuracy.classificationcorrectness;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class CheckFlowerClassification {
	
	private ArrayList<String> classifications = new ArrayList<String>();
	private boolean match = false;
	
	
	public CheckFlowerClassification(String imageToCheck, String expectedClassification, double acceptanceThreshold) {
		
		classifications = getClassification (imageToCheck);
		
		match = checkClassification(classifications,expectedClassification, acceptanceThreshold);
				
		
		
	}
	
	public boolean getMatch() {
		return match;
	}
	
	public ArrayList<String> getClassifications(){
		return classifications;
	}
	
	
	
	private ArrayList<String> getClassification(String imageString) {
		
		
		String s = null;
		
		ArrayList<String> output = new ArrayList<String>();
		
		BufferedImage image = null;
		
		File tempFile = null;
		
		try {
			URL url = new URL(imageString);
			image = ImageIO.read(url);
			
			String writerNames[] = ImageIO.getWriterFormatNames();
			
			RenderedImage ri = (RenderedImage) image;
			
			tempFile = File.createTempFile("temp", "jpg");
			
			ImageIO.write(ri, "jpg", tempFile);
			
			
		}
		
		catch (Exception e) {
			
			System.out.println("Exception " + e);
		}
		
		
		
		try {
			
			
			Process p = Runtime.getRuntime().exec("python /home/sam/Documents/TensorFlow/example_code/label_image.py " + 
					"--graph=/tmp/output_graph.pb --labels=/tmp/output_labels.txt " + 
					"--input_layer=Placeholder " + 
					"--output_layer=final_result " + 
					"--image=" + tempFile.getAbsolutePath());
			
		
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			
			
					
			
			
			 // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                
                output.add(s);
               
                
            }
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
		}
		
		catch (Exception e) {
			
			
		}
		
		System.out.println(output.size() );
		
		return output;
	}
	
	private boolean checkClassification(ArrayList<String> classifications, String expectedClassification, double threshold) {
		
		return false;
	}


	
	
	
	/**public static void main (String []args) {
		
		String s = null;
		
		String path = "https://www.dawsonsgardenworld.com.au/wp-content/uploads/2013/06/Fire_0020_and_0020_Ice-300x300.jpg";
		
		ArrayList<String> output = new ArrayList<String>();
		
		BufferedImage image = null;
		
		File tempFile = null;
		
		try {
			URL url = new URL(path);
			image = ImageIO.read(url);
			
			String writerNames[] = ImageIO.getWriterFormatNames();
			
			RenderedImage ri = (RenderedImage) image;
			
			tempFile = File.createTempFile("temp", "jpg");
			
			ImageIO.write(ri, "jpg", tempFile);
			
			
		}
		
		catch (Exception e) {
			
			System.out.println("Exception " + e);
		}
		
		
		
		try {
			
			/**Process p = Runtime.getRuntime().exec("python /home/sam/Documents/TensorFlow/example_code/label_image.py " + 
					"--graph=/tmp/output_graph.pb --labels=/tmp/output_labels.txt " + 
					"--input_layer=Placeholder " + 
					"--output_layer=final_result " + 
					"--image=/home/sam/Documents/TensorFlow/Tutorial/flower_photos/flower_photos/summer_flower.jpeg");
			
			
			
			
			
			
		Process p = Runtime.getRuntime().exec("python /home/sam/Documents/TensorFlow/example_code/label_image.py " + 
					"--graph=/tmp/output_graph.pb --labels=/tmp/output_labels.txt " + 
					"--input_layer=Placeholder " + 
					"--output_layer=final_result " + 
					"--image=" + tempFile.getAbsolutePath());
			
		
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			
			
					
			
			
			 // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                
                output.add(s);
               
                
            }
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
		}
		
		catch (Exception e) {
			
			
		}
		
		System.out.println(output.size() );
		
	}**/
	
	
	
}
