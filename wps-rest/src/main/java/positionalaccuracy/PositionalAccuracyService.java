package positionalaccuracy;

import org.geotools.feature.FeatureCollection;

/**
 * Provider of positional accuracy services
 */
public interface PositionalAccuracyService {

	/**
	 * @param target The target feature collection
	 * @param targetIDField The target ID field
	 * @param universe The universe feature collection
	 * @param referenceIDField The reference ID field
	 * @return The mean value of positional uncertainties 
	 */
	public double getMeanValueOfPositionalUncertainties(FeatureCollection target, String targetIDField, 
			FeatureCollection universe, String referenceIDField);
}
