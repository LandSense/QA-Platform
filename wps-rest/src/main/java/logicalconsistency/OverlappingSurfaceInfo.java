package logicalconsistency;

import com.fasterxml.jackson.annotation.JsonRawValue;

import io.swagger.annotations.ApiModel;

/**
 * A collection of information related to overlapping polygons 
 */
@ApiModel(value="OverlappingSurfaceInfo", description="Overlapping surface information")
public class OverlappingSurfaceInfo {
	
	private int numberOfOverlappingPolygons;
	
	@JsonRawValue
	private String overlappingPolygons;
	
	/**
	 * @return The number of overlapping polygons
	 */
	public int getNumberOfOverlappingPolygons() {
		return numberOfOverlappingPolygons;
	}
	
	/**
	 * @param numberOfOverlappingPolygons the numberOfOverlappingPolygons to set
	 */
	public void setNumberOfOverlappingPolygons(final int numberOfOverlappingPolygons) {
		this.numberOfOverlappingPolygons = numberOfOverlappingPolygons;
	}
	
	/**
	 * @return The overlapping polygons
	 */
	public String getOverlappingPolygons() {
		return overlappingPolygons;
	}
	
	/**
	 * @param overlappingPolygons The overlappingPolygons to set
	 */
	public void setOverlappingPolygons(final String overlappingPolygons) {
		this.overlappingPolygons = overlappingPolygons;
	}
}
