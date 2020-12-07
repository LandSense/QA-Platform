package positionalaccuracy;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.geotools.feature.FeatureCollection;

import common.JsonUtils;
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
@Path("/positionalaccuracy")
@Api(tags = { "Positional Accuracy" }, authorizations = @Authorization(value = "access_token", scopes = {
		@AuthorizationScope(scope = "default", description = "Default scope") }))
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses({ @ApiResponse(code = 200, message = "Request successful"),
		@ApiResponse(code = 400, message = "An error occurred"), @ApiResponse(code = 401, message = "Unauthorized") })
public class PositionalAccuracyResource {

	private PositionalAccuracyService service = new PositionalAccuracyServiceImpl();

	@POST
	@Path("/uncertainty/mean")
	@Secured
	@ApiOperation(value = "Get the mean value of positional uncertainties", response = double.class)
	public Response getMeanValueOfPositionalUncertainties(
			@ApiParam(name = "features", value = "The target and reference feature collections as JSON", required = true) final TargetReferenceDoublet features,
			@ApiParam(name = "targetIdField", value = "The target ID field") @QueryParam("targetIdField") final String targetIDField,
			@ApiParam(name = "referenceIdField", value = "The reference ID field") @QueryParam("referenceIdField") final String referenceIDField) {

		FeatureCollection targetFeatures = JsonUtils.parseFeatureCollection(features.getTarget());
		FeatureCollection universeFeatures = JsonUtils.parseFeatureCollection(features.getReference());
		if (targetFeatures == null || universeFeatures == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		double entity = service.getMeanValueOfPositionalUncertainties(targetFeatures, targetIDField, universeFeatures,
				referenceIDField);

		return Response.ok().entity(entity).build();
	}

}