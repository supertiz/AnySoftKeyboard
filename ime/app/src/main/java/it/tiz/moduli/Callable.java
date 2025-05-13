package it.tiz.moduli;

/**
 Like java.util.function.Consumer class but it does not require SDK 24
 */
public interface Callable<I> {
	
	void call(I input);
}
