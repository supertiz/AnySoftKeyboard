package it.tiz.moduli.exceptions.handler;

import androidx.annotation.NonNull;

import it.tiz.moduli.Callable;
import it.tiz.moduli.FunctionsModule;
import it.tiz.moduli.LogModule;

public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {
	private final Callable<Throwable> callbackError;
	
	public ThreadExceptionHandler(Callable<Throwable> callbackError) {
		super();
		this.callbackError = callbackError;
	}
	
	@Override
	public void uncaughtException(@NonNull Thread t, @NonNull Throwable throwable) {
		if(callbackError==null) {
			LogModule.error("Error in thread: "+ FunctionsModule.fromThrowableToString(throwable));
			return;
		}
		callbackError.call(throwable);
	}
}
