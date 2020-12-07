package example.test;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Geometry;

import iso19157.logicalconsistency.conceptualconsistency.IdentifySpatialDuplicates;
import iso19157.logicalconsistency.conceptualconsistency.OverlappingSurfaces;
import iso19157.logicalconsistency.domainconsistency.DatasetConformance;
import iso19157.logicalconsistency.toplogicalconsistency.NumberOfInvalidSlivers;
import iso19157.potisionalaccuracy.absoluteaccuracy.MeanValueOfPositionalUncertanties;
import iso19157.thematicaccuracy.classificationcorrectness.CheckImageClassification;



public class Main {
	
	public static void main (String args[]) throws IOException, NoSuchAuthorityCodeException, FactoryException {
		
		/**System.out.println("This is a test");
		
		Runnable rtest = () -> System.out.println("runnable lambda");
		
		rtest.run(); **/
		
	//	String infoSensePath = "/home/sam/Documents/Landsense_hackathon_Feb_2018/map.shp";
		
	//	FeatureCollection infosenseExample = getFeatureCollectionFromShp(infoSensePath);
		
	//	System.out.println("infoSense size " + infosenseExample.size());
		
		//OverlappingSurfaces op = new OverlappingSurfaces(infosenseExample);
		//System.out.println("Stats " + op.getNumberOfOverlappingPolygons() + " " + op.getOverlappingPolygons().size());
		
		
	//	String outputPath = "/home/sam/Downloads/output.shp";
		
		//outputShapefileFromFeatureCollection(outputPath, op.getOverlappingPolygons());
		
		
	//	String path = "/home/sam/Documents/Test_Data/landmarks.shp";
		
		/**String attribute = "cat";
		
		//String moved = "/home/sam/Downloads/bugsites_moved.shp";
		
		//String outPath = "/home/sam/Downloads/landmarks_overlapping.shp";
		
		String codeListPath = "resources/CodeLists/DomainCodeList.xml"; **/
		
		//FeatureCollection landmarks = getFeatureCollectionFromShp(path);
		
	/**	FeatureCollection targetPoints = getFeatureCollectionFromShp(path);
		
		//FeatureCollection universePoints = getFeatureCollectionFromShp(moved);
		
		System.out.println(landmarks.size()); **/
		
		//NumberOfInvalidSlivers n = new NumberOfInvalidSlivers(landmarks,"stddev", 0, 0.0007);
		
		//OverlappingSurfaces s = new OverlappingSurfaces(landmarks);
		
		//System.out.println("number of slivers " + n.getSliverPolygonsNumber());
		
		//outputShapefileFromFeatureCollection(outputPath, n.getAreaSlivers());
		
		
		
		/**DatasetConformance d = new DatasetConformance(landmarks,"f_code", codeListPath, "f_code");
		
		IdentifySpatialDuplicates i = new IdentifySpatialDuplicates(targetPoints, 0.0,0);
		
		//MeanValueOfPositionalUncertanties m = new MeanValueOfPositionalUncertanties(targetPoints, "cat", universePoints, "cat");
		
		//System.out.println("mean positional uncertainties " + m.getMeanUncertainty());
		
		System.out.println(i.getDuplicationBoolean() + " " + i.getNumberOfDuplicates() + " " + i.getDuplicationRate()); **/
		
		String imageLocation = "https://lep.landsense.eu/img/news/2017-10-13_FreiRaumNetz.jpg";
		
		String secondImageLocation = "https://news.nationalgeographic.com/content/dam/news/photos/000/284/28416.ngsversion.1421959831254.adapt.1900.1.jpg";
		
		String concept = "person";
		
		String secondConcept = "lizard";
		
		String[][] inputData = new String [2][2];
					
		//System.out.println("The decision on " + concept + " is " + c.getMatchDecision());
		
		inputData[0][0] = imageLocation;
		inputData[0][1] = concept;
		inputData[1][0] = secondImageLocation;
		inputData[1][1] = secondConcept;
		
		CheckImageClassification c = new CheckImageClassification(inputData, 0.9);
		
		System.out.println(c.getMatchDecision() + " " + c.getNumberOfCorrectClassifications() + " " + c.getClassificationRate() + 
				" " + c.getNumberOfMisClassifications() + " " + c.getMisClassificationRate());
		
		//System.out.println(d.getConformance() + " " + d.getConformanceRate() + " " + d.getNonConformanceRate()
		//+ " " + d.getNumberOfConformingFeatures() + " " + d.getNumberOfNonConformingFeatures());
				
		//outputShapefileFromFeatureCollection(outPath, s.getOverlappingPolygons());
		
		
	}
	
	
	private static FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatureCollectionFromShp(String path) 
			throws IOException{
		
		
		File file = new File(path);
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("url", file.toURI().toURL());

	    DataStore dataStore = DataStoreFinder.getDataStore(map);
	    String typeName = dataStore.getTypeNames()[0];

	    FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore
	            .getFeatureSource(typeName);
	    Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

	    FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
//	    try (FeatureIterator<SimpleFeature> features = collection.features()) {
//	    //    while (features.hasNext()) {
//	      //      SimpleFeature feature = features.next();
//	        //}
//	    }
	    
	    
	    
	    
		return collection;
	}
	
public static void outputShapefileFromFeatureCollection(String path, FeatureCollection fc) throws IOException, NoSuchAuthorityCodeException, FactoryException{
		
		FeatureIterator fi = fc.features();
	
		SimpleFeature typeF = (SimpleFeature) fi.next();
		Geometry geom = (Geometry) typeF.getDefaultGeometry();
	
	//nt srid = geom.get;
	
//	CoordinateReferenceSystem crs = CRS.decode(""+srid);
	
//	System.out.println(crs);
		File newFile = new File(path);

		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", newFile.toURI().toURL());
		params.put("create spatial index", Boolean.TRUE);

		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
		newDataStore.createSchema(typeF.getFeatureType());

    /*
     * You can comment out this line if you are using the createFeatureType method (at end of
     * class file) rather than DataUtilities.createType
     */
 
   // newDataStore.forceSchemaCRS(crs);;
    Transaction transaction = new DefaultTransaction("create");

    String typeName = newDataStore.getTypeNames()[0];
    SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

    if (featureSource instanceof SimpleFeatureStore) {
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
    
    	

        featureStore.setTransaction(transaction);
        try {
            featureStore.addFeatures(fc);
            transaction.commit();

        } catch (Exception problem) {
            problem.printStackTrace();
            System.out.println(problem);
            transaction.rollback();

        } finally {
            transaction.close();
        }
        System.exit(0); // success!
    } else {
    	
        System.out.println(typeName + " does not support read/write access");
        
       System.exit(1);
    }
}
	
	
}