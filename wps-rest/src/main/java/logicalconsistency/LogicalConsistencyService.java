package logicalconsistency;

import org.geotools.feature.FeatureCollection;

/**
 * Provider of logical consistency services
 */
public interface LogicalConsistencyService {

	/**
	 * Check the given feature collection for spatial duplicates within a given number of standard deviations
	 * @param featureCollection The feature collection to check
	 * @param stddevs The number of standard deviations
	 * @return The spatial duplicate information
	 */
	public SpatialDuplicateInfo checkSpatialDuplicates(FeatureCollection featureCollection, int stddevs);

	/**
	 * Check the given feature collection for spatial duplicates within a given buffer tolerance
	 * @param featureCollection The feature collection to check
	 * @param bufferTolerance An explicit buffer tolerance
	 * @return The spatial duplicate information
	 */
	public SpatialDuplicateInfo checkSpatialDuplicates(FeatureCollection featureCollection, double bufferTolerance);
	
	/**
	 * Check the given feature collection for overlapping surfaces
	 * @param featureCollection The feature collection to check
	 * @return The overlapping surface information
	 */
	public OverlappingSurfaceInfo checkOverlappingSurfaces(FeatureCollection featureCollection);

	/**
	 * Check the data set conformance
	 * @param featureCollection The feature collection
	 * @param attributeName The attribute name
	 * @param codeListPath The codelist path
	 * @param codeListName The codelist name
	 * @return The dataset conformance information 
	 */
	public DatasetConformanceInfo checkDatasetConformance(FeatureCollection featureCollection, String attributeName, String codeListPath, String codeListName);
	
	/**
	 * Get the sliver statistics using the provided parameters
	 * @param featureCollection The feature collection
	 * @param method The method to use when calculating slivers
	 * @param areaThreshold The area threshold
	 * @param thicknessQuotient The thickness quotient
	 * @return The sliver statistics
	 */
	public SliverStats getSliverStats(FeatureCollection featureCollection, String method, double areaThreshold, double thicknessQuotient);
	
	/**
	 * Get the area slivers using the provided parameters
	 * @param featureCollection The feature collection
	 * @param method The method to use when calculating slivers
	 * @param areaThreshold The area threshold
	 * @param thicknessQuotient The thickness quotient
	 * @return The area slivers as JSON
	 */
	public String getAreaSlivers(FeatureCollection featureCollection, String method, double areaThreshold, double thicknessQuotient);
	
	/**
	 * Get the total slivers using the provided parameters
	 * @param featureCollection The feature collection
	 * @param method The method to use when calculating slivers
	 * @param areaThreshold The area threshold
	 * @param thicknessQuotient The thickness quotient
	 * @return The total slivers as JSON
	 */
	public String getTotalSlivers(FeatureCollection featureCollection, String method, double areaThreshold, double thicknessQuotient);
}
