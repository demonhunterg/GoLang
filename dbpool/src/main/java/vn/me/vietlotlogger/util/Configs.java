package vn.me.vietlotlogger.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Address;

/**
 * @author lamhm
 *
 */
public class Configs {
	private static final Logger LOG = LoggerFactory.getLogger(Configs.class);

	// MQQueue
	public static int queuePort;
	public static String myServerId;
	public static String queueUsername;
	public static String queuePass;
	public static int queuePoolSize;
	public static int queueRecoveryIntervalMillis;
	public static Address[] queueAddresses;
	public static String onlineStatusNotifyExchange = "OnlineStatus";
	public static String onlineStatusQueueName = "OnlineStatusQueue";
	public static String onlineStatusNotifyConsumer = "OnlineStatusNotifyConsumer";

	public static String backenExchange = "backend";
	public static String checkAndRewardQueueName = "CheckAndRewardQueue";
	public static String checkAndRewardNotifyConsumer = "CheckAndRewardNotifyConsumer";

	// SQL
	public static String sqlDriver;
	public static String sqlUrl;
	public static String sqlUsername;
	public static String sqlPassword;
	public static int sqlMaxActiveConnections;
	public static int sqlMaxIdleConnections;
	public static int sqlBlockTime;
	public static int sqlWriteMinPoolSize;
	public static int sqlWriteMaxPoolSize;
	public static int sqlMaxSize;
	public static int sqlConnectTimeout;


	public static void init() {
		Properties prop = null;
		try (InputStream input = new FileInputStream(new File("configs/config.properties"));) {
			prop = new Properties();
			prop.load(input);
		} catch (Exception e) {
			LOG.error("[ERROR] Init config fail!", e);
			return;
		}

		// mqQueue
		queuePort = Integer.parseInt(prop.getProperty("queue.port", "5672"));
		queueUsername = prop.getProperty("queue.username", "queueapi");
		queuePass = prop.getProperty("queue.pass", "queueapi");
		queuePoolSize = Integer.parseInt(prop.getProperty("queue.poolSize", "1"));
		queueRecoveryIntervalMillis = Integer.parseInt(prop.getProperty("queue.recoveryIntervalMillis", "5000"));
		queueAddresses = splitAddress(prop.getProperty("queue.addresses", "10.8.36.146"));

		// sql
		sqlDriver = prop.getProperty("sqlDriver", "com.mysql.jdbc.Driver");
		sqlUrl = prop.getProperty("sqlUrl", "jdbc:mysql://10.8.24.10:3306/vietlott");
		sqlUsername = prop.getProperty("sqlUsername", "dev");
		sqlPassword = prop.getProperty("sqlPassword", "vietlot@@@");
		sqlMaxActiveConnections = Integer.parseInt(prop.getProperty("sqlMaxActiveConnections", "10"));
		sqlMaxIdleConnections = Integer.parseInt(prop.getProperty("sqlMaxIdleConnections", "10"));
		sqlBlockTime = Integer.parseInt(prop.getProperty("sqlBlockTime", "1000"));
		sqlWriteMinPoolSize = Integer.parseInt(prop.getProperty("sqlWrite.minPoolSize", "5"));
		sqlWriteMaxPoolSize = Integer.parseInt(prop.getProperty("sqlWrite.maxPoolSize", "10"));
		sqlMaxSize = Integer.parseInt(prop.getProperty("sqlWrite.maxSize", "10"));
		sqlConnectTimeout = Integer.parseInt(prop.getProperty("sqlWrite.connectionTimeOut", "3600"));
	}


	/**
	 * lấy danh sách host của queue
	 * 
	 * @param addressString danh sách host của queue cách nhau bởi dấu
	 *            <code>";"<code>
	 */
	private static Address[] splitAddress(String addressString) {
		String[] addresses = addressString.split(";");
		int length = addresses.length;
		Address[] queueAddresses = new Address[length];
		for (int i = 0; i < length; i++) {
			queueAddresses[i] = new Address(addresses[i], queuePort);
		}

		return queueAddresses;
	}

}
