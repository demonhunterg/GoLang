package vn.me.vietlotlogger;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.me.vietlotlogger.dao.DBAccess;
import vn.me.vietlotlogger.service.RabbitMQService;
import vn.me.vietlotlogger.util.Configs;

/**
 * @author lamhm
 *
 */
public class VietlottLoggerApplication {
	private static final Logger LOG = LoggerFactory.getLogger(VietlottLoggerApplication.class);


	public static void main(String[] args) {
		PropertyConfigurator.configure("configs/log4j.properties");
		LOG.info("===================== START VIETLOTT LOGGER APPLICATION =====================");
		Configs.init();
		RabbitMQService.start();
		DBAccess.getInstance();
		LOG.info("===================== VIETLOTT LOGGER APPLICATION STARTED =====================");
	}
}
