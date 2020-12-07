package logicalconsistency;

import io.swagger.annotations.ApiModel;

/**
 * Statistics about slivers 
 */
@ApiModel
public class SliverStats {

	private int numberOfSlivers;
	private double mean;
	private double variance;
	private double standardDeviation;
	private int numberOfInvalidGeometries;
	
	/**
	 * @return the numberOfSlivers
	 */
	public int getNumberOfSlivers() {
		return numberOfSlivers;
	}
	
	/**
	 * @param numberOfSlivers the numberOfSlivers to set
	 */
	public void setNumberOfSlivers(final int numberOfSlivers) {
		this.numberOfSlivers = numberOfSlivers;
	}
	
	/**
	 * @return the mean
	 */
	public double getMean() {
		return mean;
	}
	
	/**
	 * @param mean the mean to set
	 */
	public void setMean(final double mean) {
		this.mean = mean;
	}
	
	/**
	 * @return the variance
	 */
	public double getVariance() {
		return variance;
	}
	
	/**
	 * @param variance the variance to set
	 */
	public void setVariance(final double variance) {
		this.variance = variance;
	}
	
	/**
	 * @return the standardDeviation
	 */
	public double getStandardDeviation() {
		return standardDeviation;
	}
	
	/**
	 * @param standardDeviation the standardDeviation to set
	 */
	public void setStandardDeviation(final double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	/**
	 * @return return the number of invalid geometries
	 **/
	public int getNumberOfInvalidGeometries() {
		return this.numberOfInvalidGeometries;
	}
	
	
}
