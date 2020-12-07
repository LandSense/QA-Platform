package thematicaccuracy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="LocationConceptPair", description="An image location and it's corresponding concept")
public class LocationConceptPair {
	
	private String location;
	private String concept;
	
	/**
	 * @return the image location
	 */
	@ApiModelProperty(name = "location", value = "The location of the image")
	public String getLocation() {
		return location;
	}
	
	/**
	 * @param location the image location to set
	 */
	public void setLocation(final String location) {
		this.location = location;
	}
	
	/**
	 * @return the submitted concept location
	 */
	@ApiModelProperty(name = "concept", value = "The location of the submitted concept")
	public String getConcept() {
		return concept;
	}
	
	/**
	 * @param concept the submitted concept location to set
	 */
	public void setConcept(final String concept) {
		this.concept = concept;
	}
}
