package logicalconsistency;

import io.swagger.annotations.ApiModel;

@ApiModel(value="SpatialDuplicateInfo", description="Spatial duplicate information")
public class SpatialDuplicateInfo {
	
	private boolean duplicated;
	private int numberOfDuplicates;
	private double duplicationRate;

	/**
	 * @return true if there are any spatial duplicates
	 */
	public boolean isDuplicated() {
		return duplicated;
	}

	/**
	 * @param duplicated The duplicated to set
	 */
	public void setDuplicated(final boolean duplicated) {
		this.duplicated = duplicated;
	}

	/**
	 * @return The number of spatial duplicates
	 */
	public int getNumberOfDuplicates() {
		return numberOfDuplicates;
	}

	/**
	 * @param numberOfDuplicates the numberOfDuplicates to set
	 */
	public void setNumberOfDuplicates(final int numberOfDuplicates) {
		this.numberOfDuplicates = numberOfDuplicates;
	}

	/**
	 * @return The duplication rate (0 - 100)
	 */
	public double getDuplicationRate() {
		return duplicationRate;
	}

	/**
	 * @param duplicationRate the duplicationRate to set
	 */
	public void setDuplicationRate(final double duplicationRate) {
		this.duplicationRate = duplicationRate;
	}
	
}
