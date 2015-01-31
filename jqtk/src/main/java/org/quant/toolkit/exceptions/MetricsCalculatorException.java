package org.quant.toolkit.exceptions;

public class MetricsCalculatorException extends Exception {

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
