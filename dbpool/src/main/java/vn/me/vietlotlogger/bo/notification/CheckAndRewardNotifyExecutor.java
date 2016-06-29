package vn.me.vietlotlogger.bo.notification;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.me.vietlotlogger.dao.UserDAO;

import com.couchbase.client.java.document.json.JsonObject;

/**
 * @author lamhm
 *
 */
public class CheckAndRewardNotifyExecutor {
	private static final Logger LOG = LoggerFactory.getLogger(CheckAndRewardNotifyExecutor.class);
	private static final ExecutorService executor = Executors.newFixedThreadPool(1);
	private static final int POWER_BALL_POINT = 10;

	private static UserDAO userDao = UserDAO.getInstance();


	/**
	 * @param message json object <br>
	 *            <code>{ "ticket_type" : 1 }<code>
	 */
	public static void processMessage(final String message) {
		LOG.info("[DEBUG] processMessage: " + message);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {

					Map<String, List<String>> ticketWinning = new HashMap<String, List<String>>();
					JsonObject jo = JsonObject.fromJson(message);
					Integer ticketType = jo.getInt("ticket_type");
					Integer step = jo.getInt("step");

					// TODO map danh sách cơ cấu giải thưởng

					// TODO lấy số vé trúng

					// TODO lấy danh sách vé theo thời gian, so sánh với số
					// trúng, kiểm tra và lưu lại danh sách trúng thưởng. Sau
					// khi trả thưởng xong cập nhật trạng thái đợt xổ số.

				} catch (Exception e) {
					LOG.error("[ERROR] process message fail! ~~>" + message, e);
				}

			}
		});
	}


	/**
	 * So sánh vé trúng
	 * 
	 * @return điểm trúng
	 */
	public static int compareMatricesTicket(int[] expTicketNumber, int[] ticketNumber) {
		int result = 0;
		try {
			// so powerball
			result = expTicketNumber[5] == ticketNumber[5] ? POWER_BALL_POINT : 0;

			// so 5 số còn lại
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (ticketNumber[i] == expTicketNumber[j]) {
						result++;
						break;
					} else if (ticketNumber[i] < expTicketNumber[j]) {
						break;
					}
				}
			}

		} catch (Exception e) {
			LOG.error("[ERROR] compareMatricesTicket fail! ~~>" + Arrays.toString(expTicketNumber), e);
		}

		return result;
	}


	private int[] convertToArray(String ticketNumber) {
		if (ticketNumber == null || ticketNumber.trim().length() <= 0) {
			return new int[] {};
		}

		String[] items = ticketNumber.split(" ");
		int[] result = new int[items.length];
		for (int i = 0; i < items.length; i++) {
			result[i] = Integer.parseInt(items[i]);
		}

		return result;
	}

}
