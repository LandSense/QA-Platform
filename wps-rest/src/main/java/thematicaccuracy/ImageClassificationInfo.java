package thematicaccuracy;

import io.swagger.annotations.ApiModel;

@ApiModel(value="ImageClassificationInfo", description="Image classification information")
public class ImageClassificationInfo {

	private boolean match;
	private double classificationRate;
	private double misclassificationRate;
	private int classifications;
	private int misclassifications;
	
	/**
	 * @return the match
	 */
	public boolean isMatch() {
		return match;
	}
	
	/**
	 * @param match the match to set
	 */
	public void setMatch(final boolean match) {
		this.match = match;
	}
	
	/**
	 * @return the classificationRate
	 */
	public double getClassificationRate() {
		return classificationRate;
	}
	
	/**
	 * @param classificationRate the classificationRate to set
	 */
	public void setClassificationRate(final double classificationRate) {
		this.classificationRate = classificationRate;
	}
	
	/**
	 * @return the misclassificationRate
	 */
	public double getMisclassificationRate() {
		return misclassificationRate;
	}
	
	/**
	 * @param misclassificationRate the misclassificationRate to set
	 */
	public void setMisclassificationRate(final double misclassificationRate) {
		this.misclassificationRate = misclassificationRate;
	}
	
	/**
	 * @return the classifications
	 */
	public int getClassifications() {
		return classifications;
	}
	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(final int classifications) {
		this.classifications = classifications;
	}
	
	/**
	 * @return the misclassifications
	 */
	public int getMisclassifications() {
		return misclassifications;
	}
	
	/**
	 * @param misclassifications the misclassifications to set
	 */
	public void setMisclassifications(final int misclassifications) {
		this.misclassifications = misclassifications;
	}
	
}
