package vn.me.vietlotlogger.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import snaq.db.ConnectionPool;
import snaq.db.Select1Validator;
import vn.me.vietlotlogger.util.Configs;

import com.mysql.jdbc.Driver;

/**
 * @author lamhm
 *
 */
public class DBAccess {

	private static DBAccess instance;
	protected static ConnectionPool userOnlinePool;
	protected boolean active = true;


	public static DBAccess getInstance() {
		if (instance == null) {
			instance = new DBAccess();
		}

		return instance;
	}


	private DBAccess() {
		try {
			DriverManager.registerDriver((Driver) Class.forName(Configs.sqlDriver).newInstance());

			// init connection pool
			userOnlinePool = new ConnectionPool("LOG_ONLINE_POOL", Configs.sqlWriteMinPoolSize, Configs.sqlWriteMaxPoolSize, Configs.sqlMaxSize,
					Configs.sqlConnectTimeout, Configs.sqlUrl, Configs.sqlUsername, Configs.sqlPassword);
			userOnlinePool.setAsyncDestroy(true);
			userOnlinePool.setCaching(false);
			userOnlinePool.setValidator(new Select1Validator());
		} catch (Exception e) {
			active = false;
		}

	}


	private Connection getUserOnlinePoolConnection() throws SQLException {
		return userOnlinePool.getConnection();
	}


	/**
	 * Build callable statement
	 * 
	 * @param conn : Connection.
	 * @param procedure : Procedure name.
	 * @param params : Object array.
	 * @return CallableStatement instance
	 */
	private CallableStatement callableStatementWithParam(Connection conn, String procedure, Object... params) throws Exception {
		CallableStatement callableStatement = null;
		if (params != null) {
			String sql = formatCallableStatement(procedure, params.length);
			callableStatement = conn.prepareCall(sql);
			if (params != null) {
				for (int i = 1; i <= params.length; i++) {
					callableStatement.setObject(i, params[i - 1]);
				}
			}
		}
		return callableStatement;
	}


	/**
	 * Build callable statement string from procedure & parameter length
	 * 
	 * @param procedureName
	 * @param paramLength
	 * @return String format {CALL prc_storename(?,?)}
	 */
	protected String formatCallableStatement(String procedureName, int paramLength) {
		StringBuilder sql = new StringBuilder();
		sql.append("{CALL ");
		sql.append(procedureName);
		sql.append("(");
		for (int i = 1; i <= paramLength; i++) {
			sql.append("?");
			if (i < paramLength) {
				sql.append(",");
			}
		}
		sql.append(")");
		sql.append("}");
		return sql.toString();
	}


	/**
	 * Execute Single Query (Insert, delete, Update).
	 * 
	 * @param procedure
	 * @param params
	 * @return total row effected
	 */
	public boolean executeUpdate(String procedure, Object... params) {
		boolean result = true;
		try (Connection conn = getUserOnlinePoolConnection(); CallableStatement cstmt = callableStatementWithParam(conn, procedure, params)) {
			cstmt.executeUpdate();
		} catch (Exception ex) {
			result = false;
			ex.printStackTrace();
		}

		return result;
	}


	public void destroy() {
		if (userOnlinePool != null) {
			userOnlinePool.release();
		}
	}


	public boolean isActive() {
		return active;
	}

}