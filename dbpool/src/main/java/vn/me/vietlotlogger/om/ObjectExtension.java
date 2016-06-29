package vn.me.vietlotlogger.om;

import com.couchbase.client.java.document.json.JsonObject;

/**
 * @author lamhm
 *
 */
public abstract class ObjectExtension {
	public abstract void parse(JsonObject obj);
}
