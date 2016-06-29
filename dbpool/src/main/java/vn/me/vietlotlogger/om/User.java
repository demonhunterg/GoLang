package vn.me.vietlotlogger.om;

import com.couchbase.client.java.document.json.JsonObject;

/**
 * @author lamhm
 *
 */
public class User extends ObjectExtension {
	private int userId;


	@Override
	public void parse(JsonObject obj) {
		userId = obj.getInt("user_id");
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	@Override
	public String toString() {
		return String.format("{userId: %d}", userId);
	}

}
