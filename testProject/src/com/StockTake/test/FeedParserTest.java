package com.StockTake.test;

//import java.lang.reflect.Method;

//import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;
//import android.test.InstrumentationTestCase;
//import android.test.ServiceTestCase;

import com.StockTake.FeedParser;
//import com.StockTake.FeedParserTest;
import com.StockTake.Finance;

import junit.framework.Assert;
import junit.framework.Test;
//import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FeedParserTest extends AndroidTestCase {

	
	FeedParser feedparse;
	Finance finance;
	Intent intent;// = new Intent(getInstrumentation().getTargetContext(),
	
	protected void setUp() throws Exception {
		super.setUp();
		feedparse = new FeedParser(getContext());
		finance = new Finance();
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
	 */
	public void testJSONParse() throws Throwable
	{
		feedparse.parseJSON(finance, "BP");
		//System.out.println(finance.getName());
		assertNotNull(finance.getClose());
		assertNotNull(finance.getName());
		assertNotNull(finance.getSummary());
		assertNotNull(finance.getInstantVolume());
		assertNotNull(finance.getLast());
		assertNotNull(finance.getMarket());
				
		Assert.assertEquals((String)finance.getName(), "BP");
	}

	
	public void testGetHistoric() throws Throwable
	{
		Assert.assertNotNull(finance);
		finance.setName("BP");
		Assert.assertEquals((String)finance.getName(), "BP");

		Assert.assertEquals(feedparse.getHistoric(finance, "BP"), true);
		
		Assert.assertNotNull(finance.getVolume());
		Assert.assertNotNull(finance.getClose());
	}
		
	public void testVolCharToInt() throws Throwable
	{
		String testAmount = "123M";
		Assert.assertEquals(123000000, feedparse.volCharToInt(testAmount));
	}
}
