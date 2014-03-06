package com.StockTake.test;

import java.io.IOException;
import org.json.JSONException;

import android.test.AndroidTestCase;

import com.StockTake.FeedParser;
import com.StockTake.Finance;
import com.StockTake.StockManager;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

public class StockManagerTest extends AndroidTestCase
{
	StockManager stockManager;
	
	protected void setUp() throws Exception {
		super.setUp();
		stockManager = ((StockManager)getContext().getApplicationContext());

		if(stockManager.newParse == null)
		{
			// Need to do this as in the test case getApplicationContext returns null in the onCreate method.
			stockManager.newParse = new FeedParser(getContext());
		}
	}
	
	public static Test suite()
	{
		return new TestSuite(FeedParserTest.class);
	}
	
	public void testSetGetState()
	{
		stockManager.setState("Stock Manager State Test");
		Assert.assertEquals("failure - setState or getState did not work", "Stock Manager State Test", stockManager.getState());
	
		Assert.assertNotNull("failure -newParse is null! ", stockManager.newParse);
	}	

	public void testAddPortfolioEntry() throws IOException, JSONException
	{
		Assert.assertTrue("failure - adding a portfolioentry failed", stockManager.addPortfolioEntry("BP", "BP Amaco", 1234));		
	}
	
	public void testGetPortfolioTotal() throws IOException, JSONException
	{
		Assert.assertTrue("failure - failed to insert portfolioentry", stockManager.addPortfolioEntry("BP", "BP Amaco", 1234));
		Assert.assertTrue("failure - failed to get portfolioentry", stockManager.getPortfolioTotal() != 0f);
	}

	public void testClearPortfolio() throws IOException, JSONException
	{
		Assert.assertTrue("failure - failed to add portfolioentry", stockManager.addPortfolioEntry("BP", "BP Amaco", 1234));
		stockManager.clearPortfolio();
		Assert.assertTrue("failure - failed to get portfolioentry", stockManager.getPortfolioTotal() == 0f);
	}

	public void testCreateFinanceObject() throws IOException, JSONException
	{
		Finance finance;
		finance = stockManager.createFinanceObject("BP");
		Assert.assertNotNull("failure - finance object is null!", finance);
		Assert.assertEquals("failure - finance stockSymbol is not as expected", "BP", finance.getStockSymbol());
	}
}