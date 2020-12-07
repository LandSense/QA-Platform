package rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import javax.annotation.Priority;
import javax.servlet.ServletContext;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * Class used for authenticating bearer tokens contained within REST requests.
 * This class will intercept calls to any REST endpoint marked with the
 * {@link Secured} annotation
 */

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class TokenValidationFilter implements ContainerRequestFilter {

	
	private static final String PROPS_FILE = "WEB-INF/classes/oauth.properties";
	
	private static final String REALM = "default";
	private static final String BASIC_AUTHENTICATION_SCHEME = "Basic";
	private static final String AUTHENTICATION_SCHEME = "Bearer";
	private static final String ACCESS_TOKEN = "token";
	private static final String TOKEN_TYPE_HINT = "token_type_hint";
	private static final String REQUESTING_PARTY_TOKEN = "requesting_party_token";

	private String introspectUrl;
	private String clientId;
	private String clientSecret;
	private String adminKey;
	private boolean configLoaded = false;

	@Context
	private ServletContext context;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		// Load the OAuth2 configuration from file
		if (!configLoaded) {
			Properties properties = new Properties();
			InputStream props = context.getResourceAsStream(PROPS_FILE);

			if (props != null) {
				properties.load(props);
				props.close();
			} else {
				abortWithUnauthorized(requestContext,
						"Unable to load oauth properties from file - something is wrong with the application setup.");
			}

			introspectUrl = properties.getProperty("introspect.url");
			clientId = properties.getProperty("client.id");
			clientSecret = properties.getProperty("client.secret");
			adminKey = properties.getProperty("admin.key");

			if (clientId == null || clientSecret == null || introspectUrl == null) {
				abortWithUnauthorized(requestContext,
						"One or more required oauth properties were missing from the properties file.");
			} else {
				configLoaded = true;
			}
		}

		// Get the access token from the Authorization header (must be of type "Bearer").
		// If not present, check the URI for an "access_key" param instead provided it isn't a POST request
		String token = null;

		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		if (isTokenBasedAuthentication(authorizationHeader)) {
			token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
		} else if(!requestContext.getMethod().equals(javax.ws.rs.HttpMethod.POST)){
			token = requestContext.getUriInfo().getQueryParameters().getFirst(ACCESS_TOKEN);
		} else {
			abortWithUnauthorized(requestContext, "No access token was provided");
		}
		
		try {
			validateToken(token);
		} catch (Exception ex) {
			abortWithUnauthorized(requestContext, ex.getMessage());
			return;
		}
	}

	/**
	 * @param authorizationHeader
	 *            The HTTP "Authorization" header
	 * @return True if the header consists of an access token string, preceded by
	 *         "Bearer" (not case sensitive) and a space
	 */
	private boolean isTokenBasedAuthentication(String authorizationHeader) {

		// Check if the Authorization header is valid
		return authorizationHeader != null
				&& authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
	}

	/**
	 * Reject the user's HTTP request with an unauthorized response, preventing the REST resources from being triggered
	 * @param requestContext The request context
	 * @param message A message to provide to the user in the HTTP response
	 */
	private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
		// Abort the filter chain with a 401 status code response
		// The WWW-Authenticate header is sent along with the response
		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
				.entity("{\"message\": \"" + message + "\"}")
				.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"").build());
	}

	/**
	 * Validate the provided access token by sending it to the authorization server
	 * over HTTP for introspection
	 * 
	 * @param token
	 *            The provided access token
	 * @throws Exception
	 *             Invalid token
	 */
	private void validateToken(String token) throws Exception {
		if (token == null || token.isEmpty()) {
			throw new Exception("No access token was provided");
		}

		// Allow an admin key to bypass the token introspection (for testing)
		if (adminKey != null && token.equals(adminKey)) {
			return;
		}

		String credentials = clientId + ":" + clientSecret;
		String encodedCreds = Base64.getEncoder().encodeToString(credentials.getBytes("UTF-8"));

		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(introspectUrl);
		request.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
		request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
		request.addHeader(HttpHeaders.AUTHORIZATION, BASIC_AUTHENTICATION_SCHEME + " " + encodedCreds);

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair(ACCESS_TOKEN, token));
		//params.add(new BasicNameValuePair(TOKEN_TYPE_HINT, REQUESTING_PARTY_TOKEN));
		request.setEntity(new UrlEncodedFormEntity(params));

		CloseableHttpResponse response = client.execute(request);

		// Validate based on the response
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			throw new Exception("Token validation request to authorization server was unsuccessful");
		}

		JSONObject payloadJson = getJsonPayload(response);
		
		System.out.println(payloadJson);

		if (!payloadJson.getBoolean("active")) {
			throw new Exception("Provided token is not active");
		}

		client.close();
		response.close();
	}

	/**
	 * Get the payload from a HTTP response as a JSON object
	 * 
	 * @param response
	 *            The HTTP response
	 * @return The payload as a JSONObject
	 * @throws Exception
	 *             Error reading the HTTP payload
	 */
	private JSONObject getJsonPayload(final HttpResponse response) throws Exception {

		HttpEntity payload = response.getEntity();

		InputStream contentStream = payload.getContent();
		InputStreamReader reader = new InputStreamReader(contentStream);
		StringBuilder builder = new StringBuilder();

		BufferedReader br = null;
		String line;
		try {
			br = new BufferedReader(reader);
			while ((line = br.readLine()) != null) {
				builder.append(line);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new Exception("Unable to read response from authorization server");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex) {

				}
			}
		}

		return new JSONObject(builder.toString());
	}
}
