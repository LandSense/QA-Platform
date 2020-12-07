package iso19157.logicalconsistency.conceptualconsistency;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.data.binding.complex.GenericXMLDataBinding;
import org.n52.wps.io.data.binding.literal.LiteralBooleanBinding;
import org.n52.wps.io.data.binding.literal.LiteralDoubleBinding;
import org.n52.wps.io.data.binding.literal.LiteralIntBinding;
import org.n52.wps.server.AbstractAlgorithm;
import org.n52.wps.server.ExceptionReport;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.vividsolutions.jts.geom.Geometry;

import helyx.dataquality.metadata.GenerateISO19115QualityMetadata;

public class IdentifySpatialDuplicates{
	
	private int numberOfDuplicates;
	private double duplicationRate;
	private boolean duplication;
	

	//parameters are hardcoded in the Swagger interface class.
	
	public IdentifySpatialDuplicates(FeatureCollection fc, double bufferTolerance, int numberTolerance) {
		
		FeatureCollection nonNulls = removeNullGeometries(fc);
		
		int nDuplicates = calculateSpatialDuplicates(nonNulls, bufferTolerance);
		
		this.numberOfDuplicates = nDuplicates;
		
		this.duplicationRate = ((double)numberOfDuplicates)/nonNulls.size();
		
		if(numberOfDuplicates > 0) {
			
			this.duplication = true;
			
		}
		
	}
	
private FeatureCollection removeNullGeometries(FeatureCollection rawData) {
		
		ArrayList<SimpleFeature> listOfValidFeatures = new ArrayList<SimpleFeature>();
		
		SimpleFeatureType type = (SimpleFeatureType) rawData.features().next().getType();
		
		SimpleFeatureIterator sfi = (SimpleFeatureIterator) rawData.features();
		
		while(sfi.hasNext()) {
			
			SimpleFeature tempFeature = sfi.next();
			
			Geometry geom = (Geometry) tempFeature.getDefaultGeometry();
			
			if(tempFeature.getDefaultGeometry()!=null) {
				
				listOfValidFeatures.add(tempFeature);
				
			}
			
		}
		
		FeatureCollection returnFeatures = new ListFeatureCollection(type, listOfValidFeatures);
		
		System.out.println(returnFeatures.size());
		return returnFeatures;
		
		
	}
	
	private void IdentifySpatialDuplicates(FeatureCollection fc, int numberOfStddevs, int numberTolerance) {
		
		double mean = getMean(fc);
		
		double variance = getVariance(fc, mean);
		
		double stddev = Math.sqrt(variance);
		
		double bufferT = mean - (stddev * numberOfStddevs);
		
		int nDuplicates = calculateSpatialDuplicates(fc, bufferT);
		this.numberOfDuplicates = nDuplicates;
		
		this.duplicationRate = ((double)numberOfDuplicates)/fc.size();
		
		if(numberOfDuplicates > 0) {
			
			this.duplication = true;
		}
		//System.out.println(mean + " " + variance + " " + stddev + " " + nDuplicates);
		
	}
	
	private int calculateSpatialDuplicates(FeatureCollection fc, double bufferTolerance) {
		
		FeatureIterator target = fc.features();
		
		int duplicates = 0;
		
		
		while (target.hasNext()) {
			
			boolean duplicate = false;
			
			SimpleFeature targetFeature = (SimpleFeature) target.next();
			
			Geometry targetGeom = (Geometry) targetFeature.getDefaultGeometry();
			
			Collection<Property> targetProps = targetFeature.getProperties();
			
			FeatureIterator universe = fc.features();
			
						
			while (universe.hasNext()) {
				
				SimpleFeature universeFeature = (SimpleFeature) universe.next();
				
				Geometry universeGeom = (Geometry) universeFeature.getDefaultGeometry();
				
				Collection<Property> universeProps = universeFeature.getProperties();
				
				//cycle through both sets of properties and check for match
				
				
				if(targetGeom.distance(universeGeom) < bufferTolerance && !targetGeom.equalsExact(universeGeom)) {
					
					int numberOfSameProperties = 0;
					
					for (Property property : targetProps) {
												
						String propertyName = property.getName().getLocalPart();
						  
						String propertyValue = property.getValue().toString();
						
						if(!propertyName.equalsIgnoreCase("the_geom")) {
							
							for (Property universeProp : universeProps) {
								
								String universePropertyName = universeProp.getName().getLocalPart();
								String universePropertyValue = universeProp.getValue().toString();
								
								if(propertyName.equalsIgnoreCase(universePropertyName)) {
									
									if (propertyValue.equals(universePropertyValue)) {
										
										numberOfSameProperties++;
										
										//System.out.println(propertyName + " " + propertyValue + " " +universePropertyValue);
										}
									
									}
								
								}
							

								if(numberOfSameProperties == targetProps.size()-1) {
									System.out.println(numberOfSameProperties + " " + targetProps.size());
									duplicate = true;
									duplicates++;
								
								}
								
						}
							
					}
				
				}
			
			}
			
			universe.close();
		
		}
		
		return duplicates/2;
		
	}
	
	
	
	private double getMean(FeatureCollection fc) {
		
		SimpleFeatureIterator target = (SimpleFeatureIterator) fc.features();
		
		
		double mean = 0;
		double sum = 0;
		
		while (target.hasNext()) {
			SimpleFeature tempFeature = target.next();
			
			Geometry targetGeom = (Geometry) tempFeature.getDefaultGeometry();
			SimpleFeatureIterator universe = (SimpleFeatureIterator) fc.features();
			
			while (universe.hasNext()) {
				
				SimpleFeature universeFeature = universe.next();
				
				Geometry universeGeom = (Geometry) universeFeature.getDefaultGeometry();
				
				sum += targetGeom.distance(universeGeom);
				
			}
				
		}
		
		mean = sum/(fc.size() * (fc.size()));
	
		return mean;
		
	}
	
	
	private double getVariance(FeatureCollection fc, double mean) {
		
		FeatureIterator target = fc.features();
		
		
		double runningTotal = 0;
		
		while (target.hasNext()) {
			SimpleFeature tempFeature = (SimpleFeature) target.next();
			
			Geometry targetGeom = (Geometry) tempFeature.getDefaultGeometry();
			
			FeatureIterator universe = fc.features();
			
			while (universe.hasNext()) {
				
				SimpleFeature universeFeature = (SimpleFeature) universe.next();
				
				Geometry universeGeom = (Geometry) universeFeature.getDefaultGeometry();
				
				double distance = targetGeom.distance(universeGeom);
				
				runningTotal = (mean - distance) * (mean - distance);
				
				
				
			}
				
		}
		
		
		double denominator = (fc.size()) * (fc.size());
		
		return runningTotal/(denominator - 1);
		
		
	}
	
		
	public int getNumberOfDuplicates() {
		return numberOfDuplicates;
	}
	
	public double getDuplicationRate() {
		return duplicationRate;
	}
	
	public boolean getDuplicationBoolean() {
		return duplication;
	}

	

}
