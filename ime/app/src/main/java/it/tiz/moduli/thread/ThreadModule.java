package it.tiz.moduli.thread;

import it.tiz.moduli.Callable;
import it.tiz.moduli.exceptions.handler.ThreadExceptionHandler;

public class ThreadModule extends Thread {
	private final Callable<Throwable> callbackError;
	
	public ThreadModule(Callable<Throwable> callbackError) {
		super();
		this.callbackError = callbackError;
	}
	
	@Override
	public synchronized void start() {
		// set default error handler
		if(getUncaughtExceptionHandler() instanceof ThreadGroup) {
			setUncaughtExceptionHandler(new ThreadExceptionHandler(callbackError));
		}
		// start it
		super.start();
	}
}
