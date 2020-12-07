package privacy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.FileUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import rest.Secured;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.DatagramSocket;


@Path("/privacy")
@Api(tags = { "Privacy" }, authorizations = @Authorization(value = "access_token", scopes = {
		@AuthorizationScope(scope = "default", description = "Default role") }))
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@ApiResponses({ @ApiResponse(code = 200, message = "Request successful"),
		@ApiResponse(code = 400, message = "An error occurred"), @ApiResponse(code = 401, message = "Unauthorized") })
public class PrivacyResource {

    @Context
    private ServletContext context;
    private String processedDataDir = "processed"; 
	private String host = "http://localhost:8080"; //should really get this dynamically, couldn't work out how to do this consistently
        
	private PrivacyService service = new PrivacyServiceImpl();

	@POST
	@Path("/facedetection")
	@Secured
	@ApiOperation(value = "Checks if the supplied image (URL) contains faces using a remote object detection service trained on faces. Uses a detection threshold between 0 and 1, and blurs out the detected faces in the result image.", response = FaceDetectionInfo.class)
	public Response getFaceDetectionCheck(
			@ApiParam(name = "imageLocation", value = "The location of an image", required = true) final URL imageLocation,
			@ApiParam(name = "detectionThreshold", value = "A number between 0 and 1 as a threshold for confidence that detected object is a face") @QueryParam("detectionThreshold") final double detectionThreshold) {
		
		//Run the face detection
		FaceDetectionInfo entity = service.checkFaceDetection(imageLocation, detectionThreshold);
		
		System.out.println("Context " + context.getServerInfo());  
		System.out.println("Context content path " + context.getContextPath());  
		System.out.println("Context getContext TempDir " + context.getContext(ServletContext.TEMPDIR));
		System.out.println("Context getServletContextName " + context.getServletContextName());
		System.out.println("Context get real path" + context.getRealPath(File.separator));					
			
		//Work out a web server location to host the image result		
		String processedFileRelativePath = context.getContextPath() + "/" + processedDataDir + "/" + entity.getProcessedImageFile().getName();
		System.out.println("processedFileRelativePath " + processedFileRelativePath);		
		File processedFile = new File(context.getRealPath(File.separator) + processedDataDir, entity.getProcessedImageFile().getName());

		//Copy the processed file result to the web server location
		try {
			FileUtils.copyFile(entity.getProcessedImageFile(),  processedFile);
			String processedFileURI = host + processedFileRelativePath;
			entity.setProcessedImageLocation(processedFileURI);
			System.out.println("processedFileURI " + processedFileURI);
			
			//Set the image file as null
			entity.setProcessedImageFile(null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Build the response
		return Response.ok().entity(entity).build();
	}
	
		
	@POST
	@Path("/platedetection")
	@Secured
	@ApiOperation(value = "Checks if the supplied image (URL) contains a licence plate and blurs it out.", response = PlateDetectionInfo.class)
	public Response getPlateDetectionCheck(
			@ApiParam(name = "imageLocation", value = "The location of an image", required = true) final URL imageLocation,
			@ApiParam(name = "detectionThreshold", value = "A number between 0 and 1 as a threshold for confidence that detected object is a plate") @QueryParam("detectionThreshold") final double detectionThreshold) {
		
		//Run the face detection
		System.out.println("Running plate detection");  
		PlateDetectionInfo entity = service.checkPlateDetection(imageLocation, detectionThreshold);

		System.out.println("Context " + context.getServerInfo());  
		System.out.println("Context content path " + context.getContextPath());  
		System.out.println("Context getContext TempDir " + context.getContext(ServletContext.TEMPDIR));
		System.out.println("Context getServletContextName " + context.getServletContextName());
		System.out.println("Context get real path" + context.getRealPath(File.separator));					
					
		//Work out a web server location to host the plate blurred image result		
		String processedFileRelativePath = context.getContextPath() + "/" + processedDataDir + "/" + entity.getProcessedImageFile().getName();
		System.out.println("processedFileRelativePath " + processedFileRelativePath);		
		File processedFile = new File(context.getRealPath(File.separator) + processedDataDir, entity.getProcessedImageFile().getName());

		//Copy the processed file result to the web server location
		try {
			FileUtils.copyFile(entity.getProcessedImageFile(),  processedFile);
			String processedFileURI = host + processedFileRelativePath;
			entity.setProcessedImageLocation(processedFileURI);
			System.out.println("processedFileURI " + processedFileURI);
			
			//Set the image file as null
			entity.setProcessedImageFile(null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
				
		//Build the response
		return Response.ok().entity(entity).build();
	}
}
