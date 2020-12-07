package positionalaccuracy;

import com.fasterxml.jackson.annotation.JsonRawValue;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Wrapper around a pair of JSON objects (string format) for target and
 * reference feature collections
 */
@ApiModel(value = "TargetReferenceDoublet", description = "A pair of feature collections consisting of a target collection and a reference collection")
public class TargetReferenceDoublet {

	@JsonRawValue
	private String target;

	@JsonRawValue
	private String reference;

	/**
	 * @return the target
	 */
	@ApiModelProperty("The target feature collection JSON")
	public String getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(final String target) {
		this.target = target;
	}

	/**
	 * @return the reference
	 */
	@ApiModelProperty("The reference feature collection JSON")
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(final String reference) {
		this.reference = reference;
	}
}
