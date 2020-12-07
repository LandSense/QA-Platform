package iso19157.logicalconsistency.conceptualconsistency;

import java.util.ArrayList;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Geometry;

public class OverlappingSurfaces {
	
	private int numberOfOverlappingPolygons = 0;
	private FeatureCollection overlappingPolygons = null;
	
	public OverlappingSurfaces(FeatureCollection polygons){
		
		this.overlappingPolygons = getOverlappingPolygons(polygons);
		
		if(overlappingPolygons!= null) {
			this.numberOfOverlappingPolygons = overlappingPolygons.size();
		}
		else {
			numberOfOverlappingPolygons = 0;
		}
	
	}
	
	public int getNumberOfOverlappingPolygons(){
		return numberOfOverlappingPolygons;
	}
	
	public FeatureCollection getOverlappingPolygons() {
		return this.overlappingPolygons;
	}
	
	private FeatureCollection getOverlappingPolygons(FeatureCollection polygons) {
		
		ArrayList<SimpleFeature> overlappingPolygons = new ArrayList<SimpleFeature>();
		
		SimpleFeatureType type = null;
		
		type = (SimpleFeatureType) polygons.features().next().getType();
		
		SimpleFeatureTypeBuilder bType= new SimpleFeatureTypeBuilder();
		
		bType.setName("featureBuilder");
		
		bType.addAll(type.getAttributeDescriptors());
		//bType.add("OL_feat", Integer.class);
		
		SimpleFeatureType sType = bType.buildFeatureType();
		
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(sType);
		
		SimpleFeatureIterator target = (SimpleFeatureIterator) polygons.features();
		
		System.out.println("here");
					
		while (target.hasNext()) {
			
			SimpleFeature targetFeature = target.next();
			
			Geometry targetGeom = (Geometry) targetFeature.getDefaultGeometry();
			
			SimpleFeatureIterator universe = (SimpleFeatureIterator) polygons.features();
			
			while (universe.hasNext()) {
				
				SimpleFeature universeFeature = universe.next();
				
				Geometry universeGeom = (Geometry) universeFeature.getDefaultGeometry();
				
				if(targetGeom!=null && universeGeom!=null && targetGeom.overlaps(universeGeom)) {
					
					System.out.println("true");
					
					SimpleFeature sf = builder.buildFeature(targetFeature.getID());
					
					for ( Property property : targetFeature.getProperties()) {
						
						sf.setAttribute(property.getName(), property.getValue());
						//System.out.println(property.getName());
						
					}
					
					if( universeFeature.getAttribute("the_geom")!=null) {
					
						sf.setAttribute("the_geom", targetFeature.getAttribute("the_geom"));
					
					//System.out.println("geom non-null");
					}
					
					
					//sf.setAttribute("OL_feat", universeFeature.getProperty("id").getValue());
					sf.setDefaultGeometry(targetFeature.getDefaultGeometry());
					
					overlappingPolygons.add(sf);
					
					//System.out.println("overlapping polygons " + overlappingPolygons.size());
					
					
				}
				
				else {
					System.out.println("false");
				}
				
			}
			
			universe.close();
			
		}
		
		target.close();
		
		FeatureCollection overlappingPolygonsReturn = new ListFeatureCollection(sType, overlappingPolygons);
		
		System.out.println("polygons " + polygons.size() + " overlap " + overlappingPolygons.size());
		
		return overlappingPolygonsReturn;
	}

}
