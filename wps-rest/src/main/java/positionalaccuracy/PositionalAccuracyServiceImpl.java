package positionalaccuracy;

import org.geotools.feature.FeatureCollection;

import iso19157.potisionalaccuracy.absoluteaccuracy.MeanValueOfPositionalUncertanties;

public class PositionalAccuracyServiceImpl implements PositionalAccuracyService {

	@Override
	public double getMeanValueOfPositionalUncertainties(FeatureCollection target, String targetIDField,
			FeatureCollection universe, String referenceIDField) {

		MeanValueOfPositionalUncertanties mean = new MeanValueOfPositionalUncertanties(target, targetIDField, universe,
				referenceIDField);

		return mean.getMeanUncertainty();
	}

}
