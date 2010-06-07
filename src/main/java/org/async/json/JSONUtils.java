package org.async.json;

import java.io.StringReader;
import org.async.json.in.JSONParser;

/**
 *
 * @author lunach
 */
public class JSONUtils {

	public static JSONObject parseString( String string ) {
		return new JSONParser().parse( new StringReader(string) );
	}

}
