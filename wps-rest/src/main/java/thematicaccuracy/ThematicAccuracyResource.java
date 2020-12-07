package thematicaccuracy;

import java.net.URL;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import rest.Secured;

/**
 * Resource for exposing positional accuracy related services via REST
 */
@Path("/thematicaccuracy")
@Api(tags = { "Thematic Accuracy" }, authorizations = @Authorization(value = "access_token", scopes = {
		@AuthorizationScope(scope = "default", description = "Default role") }))
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses({ @ApiResponse(code = 200, message = "Request successful"),
		@ApiResponse(code = 400, message = "An error occurred"), @ApiResponse(code = 401, message = "Unauthorized") })
public class ThematicAccuracyResource {

	private ThematicAccuracyService service = new ThematicAccuracyServiceImpl();

	@GET
	@Path("/imageclassification")
	@Secured
	@ApiOperation(value = "Classify an image", response = ImageClassificationInfo.class)
	public Response getMeanValueOfPositionalUncertainties(
			@ApiParam(name = "imageLocation", value = "The location of the image", required = true) @QueryParam("imageLocation") final String imageLocation,
			@ApiParam(name = "submittedConcept", value = "The target ID field") @QueryParam("submittedConcept") final String submittedConcept,
			@ApiParam(name = "confidenceThreshold", value = "The confidence threshold") @QueryParam("confidenceThreshold") final double confidenceThreshold) {

		ImageClassificationInfo entity = service.checkImageClassification(imageLocation, submittedConcept,
				confidenceThreshold);

		return Response.ok().entity(entity).build();
	}
	
	

	@POST
	@Path("/imageclassification")
	@Secured
	@ApiOperation(value = "Classify one or more images", response = ImageClassificationInfo.class)
	public Response getMeanValueOfPositionalUncertainties(
			@ApiParam(name = "imageLocations", value = "The locations of the images and concepts", required = true) final List<LocationConceptPair> imageLocations,
			@ApiParam(name = "confidenceThreshold", value = "The confidence threshold") @QueryParam("confidenceThreshold") final double confidenceThreshold) {

		ImageClassificationInfo entity = service.checkImageClassifications(imageLocations, confidenceThreshold);

		return Response.ok().entity(entity).build();
	}
	
	@POST
	@Path("/laplaceblurcheck")
	@Secured
	@ApiOperation(value = "Check if an image is blurry", response = LaplaceBlurInfo.class)
	public Response getLaplaceBlurCheck(
			@ApiParam(name = "imageLocation", value = "The location of an image", required = true) final URL imageLocation,
			@ApiParam(name = "threshold", value = "A number between 0-255 for blur threshold") @QueryParam("threshold") final int threshold) {
		
		LaplaceBlurInfo entity = service.checkLaplaceBlur(imageLocation, threshold);
				
		return Response.ok().entity(entity).build();
	}
	
	
	
	@POST
	@Path("/blurawtcheck")
	@Secured
	@ApiOperation(value = " Checks for blur by 1) converting the image to greyscale and resizing it 2) convolving the image with a Laplace transform 3) taking the variance of the convolved image and assessing this against a user-defined threshold. Low variance values indicate blurriness", response = BlurAWTInfo.class)
	public Response getBlurAWTCheck(
			@ApiParam(name = "imageLocation", value = "The location of an image", required = true) final URL imageLocation,
			@ApiParam(name = "threshold", value = "An integer for blur threshold. 1500 is a good start. Lower values are more blur.") @QueryParam("threshold") final int threshold) {
		
		BlurAWTInfo entity = service.checkBlurAWT(imageLocation, threshold);
				
		return Response.ok().entity(entity).build();
	}
}