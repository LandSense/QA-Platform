package logicalconsistency;

import io.swagger.annotations.ApiModel;

@ApiModel(value="DatasetConformanceInfo", description="Dataset conformance information")
public class DatasetConformanceInfo {

	
	private boolean conformant;
	private int conformantFeatures;
	private int nonConformantFeatures;
	private double conformanceRate;
	private double nonConformanceRate;
	
	/**
	 * @return the conformant true if the dataset is conformant
	 */
	public boolean isConformant() {
		return conformant;
	}
	
	/**
	 * @param conformant the conformant to set
	 */
	public void setConformant(final boolean conformant) {
		this.conformant = conformant;
	}
	
	/**
	 * @return the number of conformant features
	 */
	public int getConformantFeatures() {
		return conformantFeatures;
	}
	
	/**
	 * @param conformantFeatures the conformantFeatures to set
	 */
	public void setConformantFeatures(final int conformantFeatures) {
		this.conformantFeatures = conformantFeatures;
	}
	
	/**
	 * @return the number of non-conformant features
	 */
	public int getNonConformantFeatures() {
		return nonConformantFeatures;
	}
	
	/**
	 * @param nonConformantFeatures the nonConformantFeatures to set
	 */
	public void setNonConformantFeatures(final int nonConformantFeatures) {
		this.nonConformantFeatures = nonConformantFeatures;
	}
	
	/**
	 * @return the conformanceRate
	 */
	public double getConformanceRate() {
		return conformanceRate;
	}
	
	/**
	 * @param conformanceRate the conformanceRate to set
	 */
	public void setConformanceRate(final double conformanceRate) {
		this.conformanceRate = conformanceRate;
	}
	
	/**
	 * @return the nonConformanceRate
	 */
	public double getNonConformanceRate() {
		return nonConformanceRate;
	}
	
	/**
	 * @param nonConformanceRate the nonConformanceRate to set
	 */
	public void setNonConformanceRate(final double nonConformanceRate) {
		this.nonConformanceRate = nonConformanceRate;
	}

	
}
