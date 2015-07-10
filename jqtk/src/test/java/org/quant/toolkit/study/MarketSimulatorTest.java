package org.quant.toolkit.study;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jblas.DoubleMatrix;
import org.jdaf.DoubleDataFrame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quant.toolkit.StockQuotesUtil;
import org.quant.toolkit.dao.YahooDAO;
import org.quant.toolkit.entity.Order;
import org.quant.toolkit.entity.Quote;
import org.quant.toolkit.entity.StockQuotes;
import org.quant.toolkit.exceptions.StockQuotesUtilException;

public class MarketSimulatorTest {

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRun() throws ParseException {
		Date date1 = df.parse("2011-01-05");
		Order order1 = new Order(date1, "AAPL", Order.BUY, 1500);
		List<Order> orders1 = new ArrayList<Order>();
		orders1.add(order1);

		Date date2 = df.parse("2011-01-20");
		Order order2 = new Order(date2, "AAPL", Order.SELL, 1500);
		List<Order> orders2 = new ArrayList<Order>();
		orders2.add(order2);

		Map<Date, List<Order>> orderBook = new HashMap<Date, List<Order>>();
		orderBook.put(date1, orders1);
		orderBook.put(date2, orders2);

		// Build adjusted closing price
		List<Date> index = new ArrayList<Date>();
		index.add(df.parse("2011-01-05"));
		index.add(df.parse("2011-01-06"));
		index.add(df.parse("2011-01-07"));
		index.add(df.parse("2011-01-10"));
		index.add(df.parse("2011-01-11"));
		index.add(df.parse("2011-01-12"));
		index.add(df.parse("2011-01-13"));
		index.add(df.parse("2011-01-14"));
		index.add(df.parse("2011-01-18"));
		index.add(df.parse("2011-01-19"));
		index.add(df.parse("2011-01-20"));

		List<String> labels = new ArrayList<String>();
		labels.add("AAPL");

		DoubleMatrix adjClosingPricesMx = new DoubleMatrix(new double[] {
				332.57, 332.30, 334.68, 340.99, 340.18, 342.95, 344.20, 346.99,
				339.19, 337.39, 331.26 });
		DoubleDataFrame<Date> adjClosingPrice = new DoubleDataFrame<Date>(
				index, adjClosingPricesMx, labels);

		// Build expected daily portfolio values
		DoubleMatrix dailyPortfolioValuesMx = new DoubleMatrix(new double[] {
				1000000, 999595, 1003165, 1012630, 1011415, 1015570, 1017445,
				1021630, 1009930, 1007230, 998035 });

		DoubleDataFrame<Date> dailyPortfolioValuesExp = new DoubleDataFrame<Date>(
				index, dailyPortfolioValuesMx, labels);

		// Run MarketSimulator
		MarketSimulator ms = new MarketSimulator(orderBook, adjClosingPrice);
		ms.run(1000000);
		DoubleDataFrame<Date> dailyPortfolioValuesAct = ms
				.getDailyPortfolioValues();

		System.out.println(dailyPortfolioValuesAct.getValues());
		assertEquals(dailyPortfolioValuesExp.getValues(),
				dailyPortfolioValuesAct.getValues());
		
		
	}

	@Test
	public void testRunWithMultipleOrdersPerDate() throws ParseException,
			UnsupportedEncodingException, IOException, StockQuotesUtilException {
		// Build Order Book
		String AAPL = "AAPL";
		String IBM = "IBM";
		String GOOG = "GOOG";
		String XOM = "XOM";
		Map<Date, List<Order>> orderBook = new HashMap<Date, List<Order>>();
		addToOrderBook(orderBook, new Order(df.parse("2011-01-14"), AAPL,
				Order.BUY, 1500));
		addToOrderBook(orderBook, new Order(df.parse("2011-01-19"), AAPL,
				Order.SELL, 1500));
		addToOrderBook(orderBook, new Order(df.parse("2011-01-19"), IBM,
				Order.BUY, 4000));
		addToOrderBook(orderBook, new Order(df.parse("2011-01-31"), GOOG,
				Order.BUY, 1000));
		addToOrderBook(orderBook, new Order(df.parse("2011-02-04"), XOM,
				Order.SELL, 4000));
		addToOrderBook(orderBook, new Order(df.parse("2011-02-11"), XOM,
				Order.BUY, 4000));
		addToOrderBook(orderBook, new Order(df.parse("2011-03-02"), GOOG,
				Order.SELL, 1000));
		addToOrderBook(orderBook, new Order(df.parse("2011-03-02"), IBM,
				Order.SELL, 2200));
		addToOrderBook(orderBook, new Order(df.parse("2011-06-02"), IBM,
				Order.SELL, 3300));
		addToOrderBook(orderBook, new Order(df.parse("2011-05-23"), IBM,
				Order.BUY, 1500));
		addToOrderBook(orderBook, new Order(df.parse("2011-06-10"), AAPL,
				Order.BUY, 1200));
		addToOrderBook(orderBook, new Order(df.parse("2011-08-09"), GOOG,
				Order.BUY, 55));
		addToOrderBook(orderBook, new Order(df.parse("2011-08-11"), GOOG,
				Order.SELL, 55));
		addToOrderBook(orderBook, new Order(df.parse("2011-12-14"), AAPL,
				Order.SELL, 1200));

		// Get the adj closing prices
		List<String> symbols = new ArrayList<String>();
		symbols.add(XOM);
		symbols.add(GOOG);
		symbols.add(IBM);
		symbols.add(AAPL);

		Map<String, String> map = new HashMap<String, String>();
		map.put(YahooDAO.FROM_DAY, "1");
		map.put(YahooDAO.FROM_MONTH, "1");
		map.put(YahooDAO.FROM_YEAR, "2011");
		map.put(YahooDAO.TO_DAY, "31");
		map.put(YahooDAO.TO_MONTH, "12");
		map.put(YahooDAO.TO_YEAR, "2011");
		map.put(YahooDAO.INTERVAL, YahooDAO.DAILY_TRADING_PERIOD);

		YahooDAO dao = new YahooDAO();
		dao.setCache(true,
				"/Users/mike/Documents/workspace/StockSATSBackTester/cache/");
		List<Date> tradingDays = dao.getTradingDays(map);
		List<StockQuotes> stockQuotesList = dao.getStockQuotes(map, symbols,
				tradingDays);
		DoubleDataFrame<Date> adjClosingPrices = StockQuotesUtil
				.toDoubleDataFrame(stockQuotesList, Quote.ADJ_CLOSE);
		
		MarketSimulator ms = new MarketSimulator(orderBook, adjClosingPrices);
		ms.run(1000000);
		DoubleDataFrame<Date> dailyPortfolioValue = ms.getDailyPortfolioValues();
		String label = "0";
		// Test case 1
		double exp1 = 1000000;
		double act1 = dailyPortfolioValue.get(df.parse("2011-01-10"), label);
		assertEquals(exp1, act1, 1e6);
		// Test case 2
		double exp2 = 1065755;
		double act2 = dailyPortfolioValue.get(df.parse("2011-02-10"), label);
		assertEquals(exp2, act2, 1e6);
		// Test case 3
		double exp3 = 1133860;
		double act3 = dailyPortfolioValue.get(df.parse("2011-12-20"), label);
		assertEquals(exp3, act3, 1e6);
		
	}

	private void addToOrderBook(Map<Date, List<Order>> orderBook, Order order) {
		List<Order> orders = orderBook.get(order.getOrderDate());
		if (orders == null) {
			orders = new ArrayList<Order>();
			orders.add(order);
		} else {
			orders.add(order);
		}
		orderBook.put(order.getOrderDate(), orders);
	}
}
