package rintegration;

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
import logicalconsistency.OverlappingSurfaceInfo;
import rest.Secured;

/**
 * Testing extending REST framework
 */
@Path("/rintegration")
@Api(tags = { "R Integration" }, authorizations = @Authorization(value = "access_token", scopes = {
		@AuthorizationScope(scope = "default", description = "Default role") }))
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses({ @ApiResponse(code = 200, message = "Request successful"),
		@ApiResponse(code = 400, message = "An error occurred"), @ApiResponse(code = 401, message = "Unauthorized") })
public class RIntegrationResource {

	private RIntegrationService service = new RIntegrationServiceImpl();
	
	
		
	
	@POST
	@Path("/rsystemcall")
	@Secured
	@ApiOperation(value = "Test R system script to identify R version", response = RSystemCallInfo.class)
	public Response getRSystemDetails(
			@ApiParam(name = "imageLocation", value = "Test R system script to output the R version installed", required = false) final String imageLocation
			) {
		System.out.println("RIntegrationResource: get R System version" );
		RSystemCallInfo entity = service.getRSystemDetails(imageLocation);
				
		return Response.ok().entity(entity).build();
	}
	
	@POST
	@Path("/rsystemgeoread")
	@Secured
	@ApiOperation(value = "Test R system script to read the supplied geojson and then write it back out in the response.", response = RSystemGeoReadWriteInfo.class)
	public Response getRSystemGeoReadWrite(
			@ApiParam(name = "fileLocation", value = "Some string required", required = false) final String fileLocation
			) {		
		RSystemGeoReadWriteInfo entity = service.getRSystemGeoReadWriteDetails(fileLocation);
				
		return Response.ok().entity(entity).build();
	}
	

	@POST
	@Path("/landuseqa")
	@Secured
	@ApiOperation(value = "R script calling LandUseQAInfo", response = LandUseQAInfo.class)
	public Response getLandUseQA(
			@ApiParam(name = "inputData", value = "Input data location", required = true) final String inputData,
			//@ApiParam(name = "outputData", value = "Some string required", required = true) final String fileLocation,
			@ApiParam(name = "campaignPrefix", value = "campaign prefix", required = true) @QueryParam("campaignPrefix") final String campaignPrefix,
			@ApiParam(name = "redundancyDepth", value = "redundancy depth", required = true) @QueryParam("redundancyDepth") final String redundancyDepth,	
			@ApiParam(name = "samplingPopulation", value = "samplingPopulation", required = true) @QueryParam("samplingPopulation") final String samplingPopulation		
			
		) {		
		LandUseQAInfo entity = service.getLandUseQA(inputData,campaignPrefix,redundancyDepth,samplingPopulation);				
		return Response.ok().entity(entity).build();
	}
	
	
	@POST
	@Path("/LS3a_ContributorAgreement_IGN_v8")
	@Secured
	@ApiOperation(value = "R script calling LS3a_ContributorAgreement_IGN_v8Info", response = LandUseQAInfo.class)
	public Response getLS3a_ContributorAgreement_IGN_v8(
			@ApiParam(name = "inputData", value = "Input data location", required = true) final String inputData,
			@ApiParam(name = "dateStr", value = "dateStr", required = true) @QueryParam("dateStr") final String dateStr,
			@ApiParam(name = "campaignPrefix", value = "campaign prefix", required = true) @QueryParam("campaignPrefix") final String campaignPrefix,
			@ApiParam(name = "redundancyDepth", value = "redundancy depth", required = true) @QueryParam("redundancyDepth") final String redundancyDepth,	
			@ApiParam(name = "scottsPiThresh", value = "scottsPiThresh", required = true) @QueryParam("scottsPiThresh") final String scottsPiThresh
			
		) {		
		LS3a_ContributorAgreement_IGN_v8Info entity = service.getLS3a_ContributorAgreement_IGN_v8(inputData,dateStr,campaignPrefix,redundancyDepth,scottsPiThresh);				
		return Response.ok().entity(entity).build();
	}
	
	
}