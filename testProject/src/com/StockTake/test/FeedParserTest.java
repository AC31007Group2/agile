package com.StockTake.test;

import java.io.BufferedReader;

import android.content.Intent;
import android.test.AndroidTestCase;

import com.StockTake.FeedParser;
import com.StockTake.Finance;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

public class FeedParserTest extends AndroidTestCase
{
	FeedParser feedparse;
	Finance finance;
	Intent intent;
	
	protected void setUp() throws Exception {
		super.setUp();
		feedparse = new FeedParser(getContext());
		finance = new Finance(getContext());
	}
	
	public static Test suite()
	{
		return new TestSuite(FeedParserTest.class);
	}
	
	/**
	 * OK
	 */
	public void testNotNull() throws Throwable
	{
		Assert.assertNotSame(feedparse, null);
	}

	/**
	 * OK
	 * Test method with existing stock symbol.
	 */
	public void testJSONPars() throws Throwable
	{
		feedparse.parseJSON(finance, "BP");

		assertNotNull(finance.getClose());
		assertNotNull(finance.getName());
		assertNotNull(finance.getSummary());
		assertNotNull(finance.getInstantVolume());
		assertNotNull(finance.getLast());
		assertNotNull(finance.getMarket());
				
		Assert.assertEquals((String)finance.getName(), "BP");
	}

	/**
	 * OK
	 * Test method with NULL stock symbol.
	 */
	public void testGetHistoricNull() throws Throwable
	{
		Assert.assertNotNull(finance);
		finance.setName(null);
		Assert.assertEquals((String)finance.getName(), null);

		Assert.assertEquals(feedparse.getHistoric(finance, null), false);
		
		Assert.assertSame(finance.getVolume(),0);
		Assert.assertEquals(finance.getClose(), 0.0, 0.0);
	}
	
	/**
	 * OK
	 * Test method with existing stock symbol.
	 */
	public void testGetHistoricExisting() throws Throwable
	{
		Assert.assertNotNull(finance);
		finance.setName("BP");
		Assert.assertEquals((String)finance.getName(), "BP");

		Assert.assertEquals(feedparse.getHistoric(finance, "BP"), true);
		
		Assert.assertNotNull(finance.getVolume());
		Assert.assertNotNull(finance.getClose());
	}
	
	/**
	 * OK
	 * Test method with not existing stock symbol.
	 */
	public void testGetHistoricNotExisting() throws Throwable
	{
		Assert.assertNotNull(finance);
		finance.setName("THIS STOCK DOES NOT EXIST");
		Assert.assertEquals((String)finance.getName(), "THIS STOCK DOES NOT EXIST");

		Assert.assertEquals(feedparse.getHistoric(finance, "THIS STOCK DOES NOT EXIST"), false);
		
		Assert.assertNotNull(finance.getVolume());
		Assert.assertNotNull(finance.getClose());
	}
	
	/**
	 * OK
	 */
	public void testVolCharToInt() throws Throwable
	{
		String testAmount = "123M";
		Assert.assertEquals(123000000, feedparse.volCharToInt(testAmount));
		
		testAmount = "100K";
		Assert.assertEquals(100000, feedparse.volCharToInt(testAmount));
		
		testAmount = "0";
		Assert.assertEquals(0, feedparse.volCharToInt(testAmount));
		
		testAmount = "-1";
		Assert.assertEquals(0, feedparse.volCharToInt(testAmount));
		
		testAmount = "aWrongString";
		Assert.assertEquals(0, feedparse.volCharToInt(testAmount));
	}
	
	/**
	 * OK
	 * Test method with an existing stock symbol.
	 */
	public void testParseCsvStringExisting() throws Throwable
	{
		BufferedReader csvBr;
		csvBr   = feedparse.getCsvFeed("BP");

		
		Assert.assertNotNull(feedparse.parseCsvString(csvBr));
	}
	
	/**
	 * OK
	 * Test method with a null stock symbol.
	 */
	public void testParseCsvStringNull() throws Throwable
	{
		Assert.assertNull(feedparse.parseCsvString(null));
	}
}
