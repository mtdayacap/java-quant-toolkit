package org.quant.toolkit.entity;

public class Quote {

	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static final String DATE = "Date";

	public static final String OPEN = "Open";

	public static final String HIGH = "High";

	public static final String LOW = "Low";

	public static final String VOLUME = "Volume";

	public static final String ADJ_CLOSE = "Adj Close";

	public static final String CLOSE = "Close";

	private double open;

	private double close;

	private double high;

	private double low;

	private double adjClose;

	private long volume;

	public Quote(String open, String high, String low, String close,
			String volume, String adjClose) throws NumberFormatException {
		this.open = Double.valueOf(open);
		this.high = Double.valueOf(high);
		this.low = Double.valueOf(low);
		this.close = Double.valueOf(close);
		this.volume = Long.valueOf(volume);
		this.adjClose = Double.valueOf(adjClose);
	}

	public Quote(double open, double high, double low, double close,
			long volume, double adjClose) {
		this.open = open;
		this.high = high;
		this.close = close;
		this.low = low;
		this.volume = volume;
		this.adjClose = adjClose;
	}

	public double getClose() {
		return close;
	}

	public double getHigh() {
		return high;
	}

	public double getLow() {
		return low;
	}

	public double getOpen() {
		return open;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}
}
