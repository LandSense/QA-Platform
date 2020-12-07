package iso19157.potisionalaccuracy.absoluteaccuracy;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class MeanValueOfPositionalUncertanties {

	/**
	 * @param target the target dataset
	 * @param universe the positional universe of discourse
	 */
	
	//1D = modulus of position - UoD position
	//2D = SQRT((x-xUod)squared + (y-yUoD)squared)
	
	
	private double meanUncertainty = 0;
	
	public MeanValueOfPositionalUncertanties(FeatureCollection target, String targetIDField, 
			FeatureCollection universe, String referenceIDField) {
		
		double[] meanFeatureDiff = get2DMeanDifference(target, targetIDField, universe, referenceIDField);
		
		this.meanUncertainty = getMeanValueOfPositionalUncertainties(meanFeatureDiff);
		
		
	}
	
	private double getMeanValueOfPositionalUncertainties(double[] uncert) {
		
		double meanUncertainty = 0;
		
		double runningTotal = 0;
		
		for (int i=0; i < uncert.length; i++) {
			
			runningTotal += uncert[i];
			
		}
		double denominator = uncert.length - 1;
		
		meanUncertainty = (1/(denominator)) * runningTotal;
		
		System.out.println("rt " + runningTotal + " l " + uncert.length );
		
		return meanUncertainty;
	}
	
	
	private double[] get2DMeanDifference(FeatureCollection target, String targetIDField, 
			FeatureCollection universe, String referenceIDField) {
		
		double meanUncertainty = 0;
		
		FeatureIterator tFI = target.features();
			
		int counter = 0;
		
		double total = 0;
		
		double[] listOfUncert = new double[target.size()];
				
		while (tFI.hasNext()){
			SimpleFeature  targetTemp = (SimpleFeature)tFI.next();
			Property targetTempID = targetTemp.getProperty(targetIDField);
			Geometry targetGeom = (Geometry) targetTemp.getDefaultGeometry();
			
			Coordinate targetCoord = targetGeom.getCoordinate();
			
			
			double targetInt = Double.parseDouble(targetTempID.getValue().toString());
			FeatureIterator rFI = universe.features();
			
			while (rFI.hasNext()){
				
				SimpleFeature refTemp = (SimpleFeature)rFI.next();
				
				Geometry referenceGeom = (Geometry) refTemp.getDefaultGeometry();
				
				Coordinate referenceCoord = referenceGeom.getCoordinate();
				
				Property authPropertyID = refTemp.getProperty(referenceIDField);
				
				double authInt = Double.parseDouble(authPropertyID.getValue().toString());
				
				
				if (targetInt == authInt){
					
					//double uncertX = Math.sqrt(Math.pow(targetCoord.x, 2) + (Math.pow(referenceCoord.x,2)));
				
					double uncert = Math.sqrt(Math.pow((targetCoord.x-referenceCoord.x),2) + 
							Math.pow((targetCoord.y-referenceCoord.y), 2));
					
					listOfUncert[counter] = uncert;
					
					System.out.println("target " + targetInt + " reference " + authInt + " " +uncert);
					
					
					counter++;
				}
				
			}
			
			
		}
		
		
		
		
		return listOfUncert;
	}
	
	
	public double getMeanUncertainty() {
		return meanUncertainty;
	}
	
	
}
