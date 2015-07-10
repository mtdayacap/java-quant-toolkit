package org.quant.toolkit.entity;

import java.util.Date;

public class Order {

	public static final String BUY = "Buy";

	public static final String SELL = "Sell";

	private Date orderDate;

	private String symbol;

	private String order;

	private double shares;
	
	
	public Order(Date orderDate, String symbol, String order, double shares) {
		this.orderDate = orderDate;
		this.symbol = symbol;
		this.order = order;
		this.shares = shares;
	}


	public Date getOrderDate() {
		return orderDate;
	}


	public String getSymbol() {
		return symbol;
	}


	public String getOrder() {
		return order;
	}


	public double getShares() {
		return shares;
	}

}
