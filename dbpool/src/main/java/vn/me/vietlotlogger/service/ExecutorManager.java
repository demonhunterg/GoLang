package vn.me.vietlotlogger.service;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author lamhm
 *
 */
public class ExecutorManager {
	private static ExecutorManager instance;
	private HashMap<String, ExecutorService> executors;
	private boolean isShutdown;
	private ExecutorService excAsyncTasks;


	public static ExecutorManager getInstance() {
		if (instance == null) {
			instance = new ExecutorManager();
		}

		return instance;
	}


	private ExecutorManager() {
		executors = new HashMap<String, ExecutorService>();
		isShutdown = false;

		// add size to config
	}


	public ExecutorService newThreadPool(int poolSize, String threadName) {
		ExecutorService executor = executors.get(threadName);
		if (executor == null) {
			// TODO put executor
		}
		return executor;
	}


	public void destroy() {
		isShutdown = true;
		for (ExecutorService executor : executors.values()) {
			executor.shutdownNow();
		}
		executors.clear();
		excAsyncTasks.shutdownNow();
	}


	public boolean isShutdown() {
		return isShutdown;
	}


	public ExecutorService getPoolAsycTask() {
		return excAsyncTasks;
	}

}
