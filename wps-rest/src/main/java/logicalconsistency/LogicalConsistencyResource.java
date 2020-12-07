package logicalconsistency;

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
 * Resource for exposing logical consistency related services via REST
 */
@Path("/logicalconsistency")
@Api(tags = { "Logical Consistency" }, authorizations = @Authorization(value = "access_token", scopes = {
		@AuthorizationScope(scope = "default", description = "Default scope") }))
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses({ @ApiResponse(code = 200, message = "Request successful"),
		@ApiResponse(code = 400, message = "An error occurred"), @ApiResponse(code = 401, message = "Unauthorized") })
public class LogicalConsistencyResource {

	private LogicalConsistencyService service;

	/**
	 * Create a new Logical Consistency Resource
	 */
	public LogicalConsistencyResource() {
		service = new LogicalConsistencyServiceImpl();
	}

	@POST
	@Path("/spatialduplicates")
	@Secured
	@ApiOperation(value = "Check for spatial duplicates within a number of standard deviations", response = SpatialDuplicateInfo.class)
	public Response checkSpatialDuplicates(
			@ApiParam(name = "featureCollection", value = "The feature collection to check", required = true) final String featureCollection,
			@ApiParam(name = "stddevs", value = "The number of standard deviations", defaultValue = "3") @QueryParam("stddevs") final int stddevs) {

		FeatureCollection fc = JsonUtils.parseFeatureCollection(featureCollection);
		if (fc == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		SpatialDuplicateInfo entity = service.checkSpatialDuplicates(fc, stddevs);

		return Response.ok().entity(entity).build();
	}

	@POST
	@Path("/spatialduplicates/tolerance")
	@Secured
	@ApiOperation(value = "Check for spatial duplicates within an explicit buffer tolerance", response = SpatialDuplicateInfo.class)
	public Response checkSpatialDuplicates(
			@ApiParam(name = "featureCollection", value = "The feature collection in JSON format", required = true) final String featureCollection,
			@ApiParam(name = "bufferTolerance", value = "The buffer tolerance to use", required = true) @QueryParam("bufferTolerance") final double bufferTolerance) {

		FeatureCollection fc = JsonUtils.parseFeatureCollection(featureCollection);
		if (fc == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		SpatialDuplicateInfo entity = service.checkSpatialDuplicates(fc, bufferTolerance);

		return Response.ok().entity(entity).build();
	}

	@POST
	@Path("/surfaceoverlap/")
	@Secured
	@ApiOperation(value = "Check for overlapping surfaces", response = OverlappingSurfaceInfo.class)
	public Response checkOverlappingSurfaces(
			@ApiParam(name = "featureCollection", value = "The feature collection in JSON format", required = true) final String featureCollection) {

		FeatureCollection fc = JsonUtils.parseFeatureCollection(featureCollection);
		if (fc == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		OverlappingSurfaceInfo entity = service.checkOverlappingSurfaces(fc);

		return Response.ok().entity(entity).build();
	}

	@POST
	@Path("/datasetconformance/")
	@Secured
	@ApiOperation(value = "Check for dataset conformance", response = DatasetConformanceInfo.class)
	public Response checkDatasetConformance(
			@ApiParam(name = "featureCollection", value = "The feature collection", required = true) final String featureCollection,
			@ApiParam(name = "attributeName", value = "The attribute name", required = true) @QueryParam("attributeName") final String attributeName,
			@ApiParam(name = "codelistPath", value = "The codelist path", required = true) @QueryParam("codelistPath") final String codelistPath,
			@ApiParam(name = "codelistName", value = "The codelist name", required = true) @QueryParam("codelistName") final String codelistName) {

		FeatureCollection fc = JsonUtils.parseFeatureCollection(featureCollection);
		if (fc == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		DatasetConformanceInfo entity = service.checkDatasetConformance(fc, attributeName, codelistPath, codelistName);

		return Response.ok().entity(entity).build();
	}

	@POST
	@Path("/slivers/stats")
	@Secured
	@ApiOperation(value = "Get the sliver statistics", response = SliverStats.class)
	public Response getSliverStats(
			@ApiParam(name = "featureCollection", value = "The feature collection", required = true) final String featureCollection,
			@ApiParam(name = "method", value = "The calculation method", required = true, allowableValues = "area, stddev") @QueryParam("method") final String method,
			@ApiParam(name = "areaThreshold", value = "The area threshold", required = true) @QueryParam("areaThreshold") final double areaThreshold,
			@ApiParam(name = "thicknessQuotient", value = "The thickness quotient", required = true) @QueryParam("thicknessQuotient") final double thicknessQuotient) {

		FeatureCollection fc = JsonUtils.parseFeatureCollection(featureCollection);
		if (fc == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		SliverStats entity = service.getSliverStats(fc, method, areaThreshold, thicknessQuotient);

		return Response.ok().entity(entity).build();
	}

	@POST
	@Path("/slivers/area")
	@Secured
	@ApiOperation(value = "Get the area slivers", response = String.class)
	public Response getAreaSlivers(
			@ApiParam(name = "featureCollection", value = "The feature collection", required = true) final String featureCollection,
			@ApiParam(name = "method", value = "The calculation method", required = true, allowableValues = "area, stddev") @QueryParam("method") final String method,
			@ApiParam(name = "areaThreshold", value = "The area threshold", required = true) @QueryParam("areaThreshold") final double areaThreshold,
			@ApiParam(name = "thicknessQuotient", value = "The thickness quotient", required = true) @QueryParam("thicknessQuotient") final double thicknessQuotient) {

		FeatureCollection fc = JsonUtils.parseFeatureCollection(featureCollection);
		if (fc == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		String entity = service.getAreaSlivers(fc, method, areaThreshold, thicknessQuotient);

		return Response.ok().entity(entity).build();
	}

	@POST
	@Path("/slivers/total")
	@Secured
	@ApiOperation(value = "Get the total slivers", response = String.class)
	public Response gettotalSlivers(
			@ApiParam(name = "featureCollection", value = "The feature collection", required = true) final String featureCollection,
			@ApiParam(name = "method", value = "The calculation method", required = true, allowableValues = "area, stddev") @QueryParam("method") final String method,
			@ApiParam(name = "areaThreshold", value = "The area threshold", required = true) @QueryParam("areaThreshold") final double areaThreshold,
			@ApiParam(name = "thicknessQuotient", value = "The thickness quotient", required = true) @QueryParam("thicknessQuotient") final double thicknessQuotient) {

		FeatureCollection fc = JsonUtils.parseFeatureCollection(featureCollection);
		if (fc == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}

		String entity = service.getTotalSlivers(fc, method, areaThreshold, thicknessQuotient);

		return Response.ok().entity(entity).build();
	}
}
