package rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import logicalconsistency.LogicalConsistencyResource;
import positionalaccuracy.PositionalAccuracyResource;
import privacy.PrivacyResource;
import rintegration.RIntegrationResource;
import thematicaccuracy.ThematicAccuracyResource;

/**
 * Main class for exposing the LandSense API. 
 */
public class RestApplication extends Application {

	/**
	 * Registration of API resources and documentation
	 */
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();
		
		resources.add(ISwaggerDefinition.class);
        resources.add(LogicalConsistencyResource.class);
        resources.add(PositionalAccuracyResource.class);
        resources.add(ThematicAccuracyResource.class);        
        resources.add(RIntegrationResource.class);        
        resources.add(PrivacyResource.class);    
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);

        return resources;
	}
}