package vn.me.vietlotlogger.service;

import java.util.concurrent.ExecutorService;

import vn.me.vietlotlogger.bo.notification.CheckAndRewardNotifyConsumer;
import vn.me.vietlotlogger.bo.notification.UserOnlineNotifyConsumer;
import vn.me.vietlotlogger.util.Configs;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author lamhm
 *
 */
public class RabbitMQService {
	public static final String ROUTING_KEY_ONLINE_STATUS = "online_status";
	private static Channel channel;
	private static Connection connection;


	public static void start() {
		listenQueueExchange();
	}


	private static void listenQueueExchange() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUsername(Configs.queueUsername);
			factory.setPassword(Configs.queuePass);
			factory.setNetworkRecoveryInterval(Configs.queueRecoveryIntervalMillis);
			factory.setAutomaticRecoveryEnabled(true);

			ExecutorService executor = ExecutorManager.getInstance().newThreadPool(Configs.queuePoolSize, RabbitMQService.class.getName());
			connection = factory.newConnection(executor, Configs.queueAddresses);
			channel = connection.createChannel();

			// user on/offline
			channel.exchangeDeclare(Configs.onlineStatusNotifyExchange, "direct", true);
			channel.queueDeclare(Configs.onlineStatusQueueName, true, false, false, null);
			channel.queueBind(Configs.onlineStatusQueueName, Configs.onlineStatusNotifyExchange, ROUTING_KEY_ONLINE_STATUS);
			channel.basicConsume(Configs.onlineStatusQueueName, false, Configs.onlineStatusNotifyConsumer, new UserOnlineNotifyConsumer(channel));

			// notification reward
			channel.exchangeDeclare(Configs.backenExchange, "direct", true);
			channel.queueDeclare(Configs.checkAndRewardQueueName, true, false, false, null);
			channel.queueBind(Configs.checkAndRewardQueueName, Configs.backenExchange, ROUTING_KEY_ONLINE_STATUS);
			channel.basicConsume(Configs.checkAndRewardQueueName, false, Configs.checkAndRewardNotifyConsumer, new CheckAndRewardNotifyConsumer(channel));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
