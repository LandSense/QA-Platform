package rest;

import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.ApiKeyAuthDefinition.ApiKeyLocation;
import io.swagger.annotations.Info;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.SwaggerDefinition.Scheme;
import io.swagger.annotations.Tag;

/**
 * Marker interface whose only purpose is to hold the top level swagger definition
 */
@SwaggerDefinition(info = @Info(title = "LandSense", description = "Services for LandSense", version = "v0.0.1"), schemes = {
		Scheme.HTTP,
		Scheme.HTTPS }, securityDefinition = @SecurityDefinition(apiKeyAuthDefinitions = @ApiKeyAuthDefinition(key = "access_token", in = ApiKeyLocation.HEADER, name = "Authorization", description = "An OAuth2 access token given to you by an authorization server, preceded by \"Bearer \"")), tags = {
				@Tag(name = "Privacy", description = "Services related to privacy and GDPR"),
				@Tag(name = "R Integration", description = "Services related to R scripts"),
				@Tag(name = "Logical Consistency", description = "Services related to logical consistency"),
				@Tag(name = "Positional Accuracy", description = "Services related to positional accuracy"),
				@Tag(name = "Thematic Accuracy", description = "Services related to thematic accuracy") })
public interface ISwaggerDefinition {

}
