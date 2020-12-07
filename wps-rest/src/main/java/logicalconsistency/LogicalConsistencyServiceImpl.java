package logicalconsistency;

import org.geotools.feature.FeatureCollection;

import common.JsonUtils;
import iso19157.logicalconsistency.conceptualconsistency.IdentifySpatialDuplicates;
import iso19157.logicalconsistency.conceptualconsistency.OverlappingSurfaces;
import iso19157.logicalconsistency.domainconsistency.DatasetConformance;
import iso19157.logicalconsistency.toplogicalconsistency.NumberOfInvalidSlivers;

public class LogicalConsistencyServiceImpl implements LogicalConsistencyService {

	@Override
	public SpatialDuplicateInfo checkSpatialDuplicates(final FeatureCollection featureCollection, final int stddevs) {
		IdentifySpatialDuplicates isd = new IdentifySpatialDuplicates(featureCollection, stddevs, 0);

		SpatialDuplicateInfo data = new SpatialDuplicateInfo();
		data.setDuplicated(isd.getDuplicationBoolean());
		data.setDuplicationRate(isd.getDuplicationRate());
		data.setNumberOfDuplicates(isd.getNumberOfDuplicates());

		return data;
	}

	@Override
	public SpatialDuplicateInfo checkSpatialDuplicates(final FeatureCollection featureCollection,
			final double bufferTolerance) {
		IdentifySpatialDuplicates isd = new IdentifySpatialDuplicates(featureCollection, bufferTolerance, 0);

		SpatialDuplicateInfo data = new SpatialDuplicateInfo();
		data.setDuplicated(isd.getDuplicationBoolean());
		data.setDuplicationRate(isd.getDuplicationRate());
		data.setNumberOfDuplicates(isd.getNumberOfDuplicates());

		return data;
	}

	@Override
	public OverlappingSurfaceInfo checkOverlappingSurfaces(final FeatureCollection featureCollection) {
		OverlappingSurfaces os = new OverlappingSurfaces(featureCollection);

		OverlappingSurfaceInfo data = new OverlappingSurfaceInfo();
		data.setNumberOfOverlappingPolygons(os.getNumberOfOverlappingPolygons());

		FeatureCollection overlappingPolygons = os.getOverlappingPolygons();
		data.setOverlappingPolygons(JsonUtils.serializeFeatureCollection(overlappingPolygons));
		
		return data;
	}

	@Override
	public DatasetConformanceInfo checkDatasetConformance(final FeatureCollection featureCollection,
			final String attributeName, String codeListPath, String codeListName) {

		DatasetConformance conformance = new DatasetConformance(featureCollection, attributeName, codeListPath,
				codeListName);

		DatasetConformanceInfo data = new DatasetConformanceInfo();
		data.setConformant(conformance.getConformance());
		data.setConformanceRate(conformance.getConformanceRate());
		data.setNonConformanceRate(conformance.getNonConformanceRate());
		data.setConformantFeatures(conformance.getNumberOfConformingFeatures());
		data.setNonConformantFeatures(conformance.getNumberOfNonConformingFeatures());

		return data;
	}

	@Override
	public SliverStats getSliverStats(final FeatureCollection featureCollection, final String method,
			final double areaThreshold, final double thicknessQuotient) {

		NumberOfInvalidSlivers invalidSlivers = new NumberOfInvalidSlivers(featureCollection, method, areaThreshold,
				thicknessQuotient);

		SliverStats sliverStats = new SliverStats();
		sliverStats.setMean(invalidSlivers.getMean());
		sliverStats.setStandardDeviation(invalidSlivers.getStddev());
		sliverStats.setVariance(invalidSlivers.getVariance());
		sliverStats.setNumberOfSlivers(invalidSlivers.getSliverPolygonsNumber());
		
		return sliverStats;
	}

	@Override
	public String getAreaSlivers(final FeatureCollection featureCollection, final String method,
			final double areaThreshold, final double thicknessQuotient) {

		NumberOfInvalidSlivers invalidSlivers = new NumberOfInvalidSlivers(featureCollection, method, areaThreshold,
				thicknessQuotient);

		FeatureCollection slivers = invalidSlivers.getAreaSlivers();
		String sliverJson = JsonUtils.serializeFeatureCollection(slivers);
		
		return sliverJson;
	}

	@Override
	public String getTotalSlivers(final FeatureCollection featureCollection, final String method,
			final double areaThreshold, final double thicknessQuotient) {

		NumberOfInvalidSlivers invalidSlivers = new NumberOfInvalidSlivers(featureCollection, method, areaThreshold,
				thicknessQuotient);

		FeatureCollection slivers = invalidSlivers.getTotalSlivers();
		String sliverJson = JsonUtils.serializeFeatureCollection(slivers);
		
		return sliverJson;
	}
}
