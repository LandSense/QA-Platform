package iso19157.thematicaccuracy.classificationcorrectness;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class LaplaceBlurCheck {
	
	private int decision = 0;
	private double maxValue = 0;
//	private double percent = 0;
	
	public LaplaceBlurCheck (URL imageLocation, int threshold) {
		
		BufferedImage original = null;		
				
		try {							
			original = ImageIO.read(imageLocation);			
		}		
		catch (IOException e) {				
			System.out.println("problem with file location " + e);
		}
		
		BufferedImage equalisedImage = histogramEqualization(original);
		BufferedImage laplaceImage = getLaplaceImage(equalisedImage);
		BufferedImage greyLapImage = convertImageToGrey(laplaceImage);
		maxValue = getMaxValue(greyLapImage, threshold);
		decision = getBlurryImageDecision(maxValue, threshold);
		//percent = getPercentBlurry(greyLapImage, threshold, (int) maxValue);
	
	}
	
	public int getDecision() {
		return decision;
	}
	
	public int getMaxValue() {
		
		return (int) maxValue;
	}
	
	//public double getPercentBlurry() {
	//	return percent;
	//}
	
	
	
	public static void main (String args[]) throws IOException {
	
		
		BufferedImage original = null;
		
		URL url = new URL("https://cdn.britannica.com/700x450/45/5645-004-7461C1BD.jpg");
		
		original = ImageIO.read(new File("/home/sam/Documents/TensorFlow/Tutorial/flower_photos/flower_photos/flower.jpeg"));
		
		BufferedImage blurry = null;
		
		blurry = ImageIO.read(new File("/home/sam/Documents/TensorFlow/Tutorial/flower_photos/flower_photos/blurry-eyes.jpg"));
		
		//BufferedImage urlImage = ImageIO.read(url);

				
		int threshold = 14;
		
		BufferedImage equalisedImage = histogramEqualization(blurry);
		BufferedImage laplaceImage = getLaplaceImage(equalisedImage);
		BufferedImage greyLapImage = convertImageToGrey(laplaceImage);
		
		int maxValue = (int) getMaxValue(greyLapImage, threshold);
		int i = getBlurryImageDecision(maxValue, threshold);
		
		double percent = getPercentBlurry(greyLapImage, threshold, maxValue);
		
		System.out.println(i + " " + percent);
		
	}
	
	
	private static BufferedImage getLaplaceImage(BufferedImage original){
		
		BufferedImage pic1 = histogramEqualization(original);
		BufferedImage pic2 = new BufferedImage(pic1.getWidth(), pic1.getHeight(), BufferedImage.TYPE_INT_RGB);
		int height = pic1.getHeight();
		int width = pic1.getWidth();
		for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                Color c00 = new Color(pic1.getRGB(x-1, y-1));
                Color c01 = new Color(pic1.getRGB(x-1, y  ));
                Color c02 = new Color(pic1.getRGB(x-1, y+1));
                Color c10 = new Color(pic1.getRGB(x  , y-1));
                Color c11 = new Color(pic1.getRGB(x  , y  ));
                Color c12 = new Color(pic1.getRGB(x  , y+1));
                Color c20 = new Color(pic1.getRGB(x+1, y-1));
                Color c21 = new Color(pic1.getRGB(x+1, y  ));
                Color c22 = new Color(pic1.getRGB(x+1, y+1));
                int r = -c00.getRed() -   c01.getRed() - c02.getRed() +
                        -c10.getRed() + 8*c11.getRed() - c12.getRed() +
                        -c20.getRed() -   c21.getRed() - c22.getRed();
                int g = -c00.getGreen() -   c01.getGreen() - c02.getGreen() +
                        -c10.getGreen() + 8*c11.getGreen() - c12.getGreen() +
                        -c20.getGreen() -   c21.getGreen() - c22.getGreen();
                int b = -c00.getBlue() -   c01.getBlue() - c02.getBlue() +
                        -c10.getBlue() + 8*c11.getBlue() - c12.getBlue() +
                        -c20.getBlue() -   c21.getBlue() - c22.getBlue();
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                Color c = new Color(r, g, b);
                
                pic2.setRGB(x, y, c.getRGB());
                
            }
		
		
	}
		
		
		return pic2;
}
	
	private static BufferedImage histogramEqualization(BufferedImage original) {
		 
        int red;
        int green;
        int blue;
        int alpha;
        int newPixel = 0;
 
        // Get the Lookup table for histogram equalization
        ArrayList<int[]> histLUT = histogramEqualizationLUT(original);
 
        BufferedImage histogramEQ = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
 
        for(int i=0; i<original.getWidth(); i++) {
            for(int j=0; j<original.getHeight(); j++) {
 
                // Get pixels by R, G, B
                alpha = new Color(original.getRGB (i, j)).getAlpha();
                red = new Color(original.getRGB (i, j)).getRed();
                green = new Color(original.getRGB (i, j)).getGreen();
                blue = new Color(original.getRGB (i, j)).getBlue();
 
                // Set new pixel values using the histogram lookup table
                red = histLUT.get(0)[red];
                green = histLUT.get(1)[green];
                blue = histLUT.get(2)[blue];
 
                // Return back to original format
                newPixel = colorToRGB(alpha, red, green, blue);
 
                // Write pixels into image
                histogramEQ.setRGB(i, j, newPixel);
 
            }
        }
        
        System.out.println("Histogram Image " + histogramEQ.getWidth() + " " + histogramEQ.getHeight());
       
		/**try {
			File file = new File ("/Users/lgzsam/Downloads/test1.jpg");
      	  
      	  ImageIO.write(histogramEQ, "jpeg", file);
		} catch (IOException e) {
			LOG.error("IOException " + e);
			e.printStackTrace();
		}**/
		
 
        return histogramEQ;
 
    }
 
    // Get the histogram equalization lookup table for separate R, G, B channels
    private static ArrayList<int[]> histogramEqualizationLUT(BufferedImage input) {
 
        // Get an image histogram - calculated values by R, G, B channels
        ArrayList<int[]> imageHist = imageHistogram(input);
 
        // Create the lookup table
        ArrayList<int[]> imageLUT = new ArrayList<int[]>();
 
        // Fill the lookup table
        int[] rhistogram = new int[256];
        int[] ghistogram = new int[256];
        int[] bhistogram = new int[256];
 
        for(int i=0; i<rhistogram.length; i++) rhistogram[i] = 0;
        for(int i=0; i<ghistogram.length; i++) ghistogram[i] = 0;
        for(int i=0; i<bhistogram.length; i++) bhistogram[i] = 0;
 
        long sumr = 0;
        long sumg = 0;
        long sumb = 0;
 
        // Calculate the scale factor
        float scale_factor = (float) (255.0 / (input.getWidth() * input.getHeight()));
 
        for(int i=0; i<rhistogram.length; i++) {
            sumr += imageHist.get(0)[i];
            int valr = (int) (sumr * scale_factor);
            if(valr > 255) {
                rhistogram[i] = 255;
            }
            else rhistogram[i] = valr;
 
            sumg += imageHist.get(1)[i];
            int valg = (int) (sumg * scale_factor);
            if(valg > 255) {
                ghistogram[i] = 255;
            }
            else ghistogram[i] = valg;
 
            sumb += imageHist.get(2)[i];
            int valb = (int) (sumb * scale_factor);
            if(valb > 255) {
                bhistogram[i] = 255;
            }
            else bhistogram[i] = valb;
        }
 
        imageLUT.add(rhistogram);
        imageLUT.add(ghistogram);
        imageLUT.add(bhistogram);
 
        return imageLUT;
 
    }
 
    // Return an ArrayList containing histogram values for separate R, G, B channels
    public static ArrayList<int[]> imageHistogram(BufferedImage input) {
 
        int[] rhistogram = new int[256];
        int[] ghistogram = new int[256];
        int[] bhistogram = new int[256];
 
        for(int i=0; i<rhistogram.length; i++) rhistogram[i] = 0;
        for(int i=0; i<ghistogram.length; i++) ghistogram[i] = 0;
        for(int i=0; i<bhistogram.length; i++) bhistogram[i] = 0;
 
        for(int i=0; i<input.getWidth(); i++) {
            for(int j=0; j<input.getHeight(); j++) {
 
                int red = new Color(input.getRGB (i, j)).getRed();
                int green = new Color(input.getRGB (i, j)).getGreen();
                int blue = new Color(input.getRGB (i, j)).getBlue();
 
                // Increase the values of colors
                rhistogram[red]++; ghistogram[green]++; bhistogram[blue]++;
 
            }
        }
 
        ArrayList<int[]> hist = new ArrayList<int[]>();
        hist.add(rhistogram);
        hist.add(ghistogram);
        hist.add(bhistogram);
 
        return hist;
 
    }
 
    // Convert R, G, B, Alpha to standard 8 bit
    private static int colorToRGB(int alpha, int red, int green, int blue) {
 
        int newPixel = 0;
        newPixel += alpha; newPixel = newPixel << 8;
        newPixel += red; newPixel = newPixel << 8;
        newPixel += green; newPixel = newPixel << 8;
        newPixel += blue;
 
        return newPixel;
 
    }
    private static BufferedImage convertImageToGrey(BufferedImage original){
		
    	
    	BufferedImage greyImage = new BufferedImage(original.getWidth(), 
    			original.getHeight(),BufferedImage.TYPE_BYTE_GRAY);
    	
        int  width = original.getWidth();
        int  height = original.getHeight();
         
         for(int i=0; i<height; i++){
         
            for(int j=0; j<width; j++){
            
               Color c = new Color(original.getRGB(j, i));
               int red = (int)(c.getRed() * 0.299);
               int green = (int)(c.getGreen() * 0.587);
               int blue = (int)(c.getBlue() *0.114);
               Color newColor = new Color(red+green+blue,
               
               red+green+blue,red+green+blue);
               
               greyImage.setRGB(j,i,newColor.getRGB());
            }
         }
         
         System.out.println("Grey Image " + greyImage.getWidth() + " " + greyImage.getHeight());
          		
    	
    	
    	return greyImage;
    	
    }
    
    private static double getMaxValue(BufferedImage image, int threshold){
    	
    	int maxValue = 0;
    	
         for (int i = 0; i < image.getWidth(); i++){
        	 for (int j = 0; j < image.getHeight(); j++ ){
        		
        		
        		 Color c = new Color(image.getRGB(i, j));
        		 
        		 double value = (c.getBlue() + c.getGreen() + c.getRed())/3;
        		 
        		 if(maxValue < value) {
        			 maxValue = (int) value;
        		 }
        		    		 
        	 }
         }
         
	
		 
         System.out.println("max value " + maxValue) ;
         
       
    	
    	return maxValue;
    }
    
   
    
    private static int getBlurryImageDecision(double maxValue, int threshold) {
    	int decision = 0;
    	if (maxValue > threshold) {
    		
    		decision = 1;
    		
    	}
    	return decision;
    }
    
    private static double getPercentBlurry(BufferedImage image, int threshold, int maxValue) {
    	
    	int count = 0;
    	int imageSize = 0;
    	
    	for (int i = 0; i < image.getWidth(); i++){
         	 for (int j = 0; j < image.getHeight(); j++ ){
         		
         		
         		 Color c = new Color(image.getRGB(i, j));
         		 
         		 double value = (c.getBlue() + c.getGreen() + c.getRed())/3;
         		 
         		 if(threshold > value && value != 0) {
         			 count++;
         		 }
         		 
         		 if(value!=0) {
         			 imageSize++;
         			 //System.out.print(value);
         			 }
         		 
         		
         		    		 
         	 }
          }
    	
    	double percent = (double)count / (double)imageSize;
    	
    	return percent;
    	
    }
    
    
}
