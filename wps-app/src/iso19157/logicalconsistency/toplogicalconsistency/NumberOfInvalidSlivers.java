package iso19157.logicalconsistency.toplogicalconsistency;

import java.util.ArrayList;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class NumberOfInvalidSlivers {
	
	//for normally distributed data, use the standard deviation area threshold, otherwise use an ultimate size.
	//thickness quotient use very small numbers to identify slivers.
	
	private FeatureCollection polygons;
	double threshold;
	int numberOfSlivers = 0;
	double mean = 0;
	double variance = 0;
	double stddev = 0;
	int numberOfNullGeometries = 0;
	FeatureCollection areaSlivers = null;
	FeatureCollection slivers = null;

	
	
	public NumberOfInvalidSlivers(FeatureCollection polygons, String method, double areaThreshold, double thicknessQuotient){
		
		FeatureCollection processPolys = removeNullGeometries(polygons);
		
		this.polygons = processPolys;
		this.numberOfNullGeometries = polygons.size() - processPolys.size();
	
	
		
		switch(method) {
		case "area":
			
			areaSlivers = getNumberOfSliversByArea(this.polygons, areaThreshold);
			slivers = getThicknessQuotient(areaSlivers, thicknessQuotient);
			this.numberOfSlivers = slivers.size();
		
		case "stddev":
			
			this.mean = getMeanArea(this.polygons);
			this.variance = getVariance(this.polygons, mean);
			this.stddev = Math.sqrt(variance);
			double stddevThreshold = this.mean - (areaThreshold * this.stddev);
			
			this.areaSlivers = getNumberOfSliversByArea(this.polygons, stddevThreshold);
			this.slivers = getThicknessQuotient(areaSlivers, thicknessQuotient);
			
			//System.out.println("mean area " + this.mean + " variance " + variance + " stddev " + stddev);
			//System.out.println("area slivers " + areaSlivers.size() + " total " + slivers.size());
			
			//System.out.println(mean * 1000 + " " + variance * 1000);
			this.numberOfSlivers = slivers.size();
								
		
			break;
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
	

	
	
	
	private double getMeanArea(FeatureCollection polygons) {
		
		//have to work out what to do with null geometries
		// should ignore nulls and reduce the size of polygons by number of nulls.
		
		FeatureIterator fi = polygons.features();
		
		double runningTotal = 0;
		
		while (fi.hasNext()) {
			
			SimpleFeature tempFeature = (SimpleFeature) fi.next();
			
			Geometry tempGeom = (Geometry) tempFeature.getDefaultGeometry();
			
			runningTotal += tempGeom.getArea();
			
					
		}
		
		double mean = runningTotal/polygons.size();
		
		return mean;
		
	}
	
	private double getVariance(FeatureCollection polygons, double mean) {
		
		FeatureIterator fi = polygons.features();
		
		double runningTotal = 0;
		
		while (fi.hasNext()) {
			
			SimpleFeature tempFeature = (SimpleFeature) fi.next();
			
			Geometry geom = (Geometry) tempFeature.getDefaultGeometry();
			
			double a = geom.getArea();
			
			runningTotal += (a-mean) * (a-mean);
			
		}
			
		
		return runningTotal/(polygons.size()-1);
	}
	
	
	private FeatureCollection getNumberOfSliversByArea(FeatureCollection polygons, double areaThreshold) {
		
		
		SimpleFeatureType type = null;
		FeatureIterator fi = polygons.features();
		
		ArrayList<SimpleFeature> sliverCandidates = new ArrayList<SimpleFeature>();
		
		while (fi.hasNext()) {
			
			SimpleFeature tempFeature = (SimpleFeature) fi.next();
			
			type = tempFeature.getFeatureType();
			
			Geometry geom = (Geometry) tempFeature.getDefaultGeometry();
			
			if(geom.getArea() < areaThreshold) {
				
				sliverCandidates.add(tempFeature);
			}
			
			
		}
		
		FeatureCollection areaCollection = new ListFeatureCollection(type,sliverCandidates);
		
		return areaCollection;
		
		
	}
	
	private FeatureCollection getThicknessQuotient(FeatureCollection polygons, double thicknessQuotientThreshold){
		
		FeatureIterator fi = polygons.features();
		
		ArrayList<SimpleFeature> thicknessPolygons = new ArrayList<SimpleFeature>();
		SimpleFeatureType type = null;
		FeatureCollection thicknessCollection = null;
		
		while(fi.hasNext()) {
			
			SimpleFeature tempFeature = (SimpleFeature) fi.next();
			
			Geometry geom = (Geometry) tempFeature.getDefaultGeometry();
			
			type = tempFeature.getFeatureType();
			
			double thicknessQuotient = (4 * Math.PI * geom.getArea())/geom.getBoundary().getLength();
			
			if(thicknessQuotient < thicknessQuotientThreshold) {
				thicknessPolygons.add(tempFeature);
			}
			
			 thicknessCollection = new ListFeatureCollection(type, thicknessPolygons);
			
		}
		
		return thicknessCollection;
	}
	
	
	public int getSliverPolygonsNumber() {
		return numberOfSlivers;
	}
	
	public double getMean() {
		return this.mean;
	}
	
	public double getVariance() {
		return this.variance;
	}
	
	public double getStddev() {
		return this.stddev;
	}
	
	public FeatureCollection getAreaSlivers(){
		return this.areaSlivers;
	}
	
	public FeatureCollection getTotalSlivers() {
		return this.slivers;
	}
	
	
	
	//count number of polygons that are a sliver

}
