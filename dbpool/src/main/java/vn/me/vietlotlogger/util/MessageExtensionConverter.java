package vn.me.vietlotlogger.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.me.vietlotlogger.om.MessageExtension;
import vn.me.vietlotlogger.om.ObjectExtension;

import com.couchbase.client.java.document.json.JsonObject;

/**
 * @author lamhm
 *
 */
public class MessageExtensionConverter {
	private static final Logger LOG = LoggerFactory.getLogger(MessageExtensionConverter.class);


	public static <T extends ObjectExtension, D extends ObjectExtension> MessageExtension<T, D> convert(String message, T target, D data) {
		MessageExtension<T, D> msgExt = new MessageExtension<>();
		try {
			JsonObject jo = JsonObject.fromJson(message);
			msgExt.setAction(jo.getString("ACT"));
			target.parse(jo.getObject("TARGET"));
			msgExt.setTarget(target);
			data.parse(jo.getObject("DATA"));
			msgExt.setData(data);
		} catch (Exception e) {
			LOG.error("[ERROR] convert message fail! ~~> " + message, e);
		}

		return msgExt;
	}
}
