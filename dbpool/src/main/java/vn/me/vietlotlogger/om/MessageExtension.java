package vn.me.vietlotlogger.om;

/**
 * @author lamhm
 *
 * @param <T> Đối tượng target là đối tượng thực hiện 1 hành động nào đó
 * @param <D> Dữ liệu kèm theo một hành động
 */
public class MessageExtension<T extends ObjectExtension, D extends ObjectExtension> {
	private String action;
	private T target;
	private D data;


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public T getTarget() {
		return target;
	}


	public void setTarget(T target) {
		this.target = target;
	}


	public D getData() {
		return data;
	}


	public void setData(D data) {
		this.data = data;
	}


	@Override
	public String toString() {
		return String.format("[MessageExtension] action:%s, target:%s, data:%s", action, target.toString(), data.toString());
	}

}
