package common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.n52.wps.io.data.IData;
import org.n52.wps.io.data.binding.complex.GTVectorDataBinding;
import org.n52.wps.io.datahandler.generator.GeoJSONGenerator;
import org.n52.wps.io.datahandler.parser.GeoJSONParser;

/**
 * Class containing common utilities for reading/writing json.
 */
public class JsonUtils {

	/**
	 * Read a Feature Collection from JSON into a FeatureCollection object
	 * 
	 * @param json
	 *            The json
	 * @return The feature collection object
	 */
	public static FeatureCollection parseFeatureCollection(final String json) {

		FeatureJSON featureJson = new FeatureJSON();
		FeatureCollection featureCollection = null;

		GeoJSONParser parser = new GeoJSONParser();
		IData data = parser.parse(new ByteArrayInputStream(json.getBytes()), MediaType.APPLICATION_JSON, "");
		
		if(data.getSupportedClass() == FeatureCollection.class) {
			featureCollection = (FeatureCollection) data.getPayload();
		}

		return featureCollection;
	}

	/**
	 * Convert a FeatureCollection object to String (JSON) form
	 * 
	 * @param featureCollection
	 *            The feature collection to serialize
	 * @return The feature collection as a JSON formatted string
	 */
	public static String serializeFeatureCollection(final FeatureCollection featureCollection) {

		GeoJSONGenerator generator = new GeoJSONGenerator();
		String output = null;

		try {
			InputStream stream = generator.generateStream(new GTVectorDataBinding(featureCollection), MediaType.APPLICATION_JSON, "");
			
			ByteArrayOutputStream result = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = stream.read(buffer)) != -1) {
			    result.write(buffer, 0, length);
			}		
					
			output = result.toString("UTF-8");

		} catch (IOException | IllegalArgumentException ex) {
			ex.printStackTrace();
		}
		
		return output;
	}
}