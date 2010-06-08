package org.async.json;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.async.json.in.JSONParser;
import org.async.json.out.JSONWriter;

/**
 *
 * @author lunach
 */
public class JSONUtils {

	public static JSONObject parseObject( String string ) {
		return new JSONParser().parse( new StringReader(string) );
	}

	public static JSONArray parseArray( String arrayString ) {
		return new JSONParser().parseArray( new StringReader(arrayString) );
	}

	public static String toString( JSONObject jsonObject ) {
		try {
			StringWriter sw = new StringWriter();
			new JSONWriter( sw ).writeObject( null, jsonObject );
			return sw.toString();
		} catch( IOException ioe ) {
			throw  new RuntimeException(ioe);
		}
	}

}
