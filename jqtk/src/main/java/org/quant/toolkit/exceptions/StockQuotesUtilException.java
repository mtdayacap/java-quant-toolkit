package org.quant.toolkit.exceptions;

public class StockQuotesUtilException extends Exception {
	private static final long serialVersionUID = 1L;

	public StockQuotesUtilException(){
		super();
	}
	
	public StockQuotesUtilException(String message){
		super(message);
	}
	
	public StockQuotesUtilException(String message, Throwable t){
		super(message, t);
	}
	
	public StockQuotesUtilException(Throwable t){
		super(t);
	}
}
