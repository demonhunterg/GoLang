package vn.me.vietlotlogger.bo.notification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.me.vietlotlogger.dao.UserDAO;
import vn.me.vietlotlogger.om.MessageExtension;
import vn.me.vietlotlogger.om.ObjectExtension;
import vn.me.vietlotlogger.om.User;
import vn.me.vietlotlogger.util.MessageExtensionConverter;

import com.couchbase.client.java.document.json.JsonObject;

/**
 * @author lamhm
 *
 */
public class UserOnlineNotifyExecutor {
	private static final Logger LOG = LoggerFactory.getLogger(UserOnlineNotifyExecutor.class);
	private static final ExecutorService executor = Executors.newFixedThreadPool(5);

	private static UserDAO userDao = UserDAO.getInstance();


	/**
	 * @param message json object <br>
	 *            <code>{ACT:["create,update,delete"], TARGET:{user_id:123}, DATA:{is_onl:[true,false]}}<code>
	 *            <br>
	 *            ACT: hành động yêu cầu<br>
	 *            TARGET: đối tượng tương tác<br>
	 *            DATA: dữ liệu kèm theo
	 */
	public static void processMessage(final String message) {
		final MessageExtension<User, Data> messageExt = MessageExtensionConverter.convert(message, new User(), new Data());
		LOG.info("[DEBUG] processMessage: " + messageExt.toString());
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					switch (messageExt.getAction()) {
					case "update":
						userDao.updateOnlineStatus(messageExt.getTarget().getUserId(), messageExt.getData().isOnline());
						break;
					default:
						break;
					}

				} catch (Exception e) {
					LOG.error("[ERROR] process message fail! ~~>" + message, e);
				}

			}
		});
	}

	static class Data extends ObjectExtension {
		private boolean isOnline;
		private JsonObject jsonData;


		public Data() {
			isOnline = false;
		}


		@Override
		public void parse(JsonObject obj) {
			isOnline = obj.getBoolean("is_onl");
			this.jsonData = obj;
		}


		public boolean isOnline() {
			return isOnline;
		}


		public void setOnline(boolean isOnline) {
			this.isOnline = isOnline;
		}


		@Override
		public String toString() {
			return jsonData.toString();
		}

	}
}
