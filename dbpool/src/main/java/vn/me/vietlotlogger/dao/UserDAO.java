package vn.me.vietlotlogger.dao;

/**
 * @author lamhm
 *
 */
public class UserDAO {

	private static UserDAO instance;
	private DBAccess dbAccess;


	public static UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
		}

		return instance;
	}


	private UserDAO() {
		dbAccess = DBAccess.getInstance();
	}


	/**
	 * Update trạng thái on/offline của user
	 * 
	 * @param userId
	 * @param isOnline
	 */
	public void updateOnlineStatus(int userId, boolean isOnline) {
		dbAccess.executeUpdate("sp_user_updateUserOnlineStatus", userId, isOnline);
	}
}
