package org.quant.toolkit.exceptions;

public class MetricsCalculatorException extends Exception {

	private static final long serialVersionUID = 1L;

	public MetricsCalculatorException(){
		super();
	}
	
	public MetricsCalculatorException(String message) {
		super(message);
	}
	
	public MetricsCalculatorException(String message, Throwable e){
		super(message, e);
	}
	
	public MetricsCalculatorException(Throwable e){
		super(e);
	}

}
