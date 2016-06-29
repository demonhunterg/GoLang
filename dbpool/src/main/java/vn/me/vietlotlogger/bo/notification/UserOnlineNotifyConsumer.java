package vn.me.vietlotlogger.bo.notification;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @author lamhm
 *
 */
public class UserOnlineNotifyConsumer extends DefaultConsumer {
	public UserOnlineNotifyConsumer(Channel channel) {
		super(channel);
	}


	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		try {
			// process message
			UserOnlineNotifyExecutor.processMessage(new String(body));
		} catch (Exception e) {
		}

		getChannel().basicAck(envelope.getDeliveryTag(), false);
	}
}
