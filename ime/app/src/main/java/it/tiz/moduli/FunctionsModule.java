package it.tiz.moduli;

import java.io.PrintWriter;
import java.io.StringWriter;

public class FunctionsModule {
	private FunctionsModule(){}
	
	public static String fromThrowableToString(Throwable throwable){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		return sw.toString();
	}
	
}
