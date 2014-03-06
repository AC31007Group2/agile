package com.StockTake.test;

import com.StockTake.Finance;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import android.test.AndroidTestCase;

public class FinanceTest extends AndroidTestCase
{
	Finance finance;
	
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}

	protected void setUp()
	{
		finance = new Finance(getContext());
	}

	public static Test suite()
	{
		return new TestSuite(FinanceTest.class);
	}

	public void testNotNull() throws Throwable
	{
		Assert.assertNotSame("failure - finance is still null",finance, null);
	}

	public void testSetGetName() throws Throwable
	{
		finance.setName("Test Name");
		Assert.assertEquals("failure - setName didnt work", "Test Name", finance.getName());
	}
	
	public void testSetGetLast() throws Throwable
	{
		finance.setLast(1.234f);
		Assert.assertEquals("failure - setLast didnt work", 1.234f, finance.getLast(), 0);
	}
	
	public void testSetGetMarket() throws Throwable
	{
		finance.setMarket("Test Market Name");
		Assert.assertEquals("failure - setMarket didnt set the market correctly", "Test Market Name", finance.getMarket());
	}
	
	public void testGetSummary() throws Throwable
	{
		finance.setName("Test Name");
		finance.setLast(1.234f);
		Assert.assertEquals("failure - getSummary returned unexpected results", "Test Name:  1.234", finance.getSummary());
	}
	
	public void testSetGetClose() throws Throwable
	{
		finance.setClose(2.345f);
		Assert.assertEquals("failure - setClose did not work as anticipated", 2.345f, finance.getClose(), 0);
	}
	
	public void testSetGetVolume() throws Throwable
	{
		finance.setVolume(4321);
		Assert.assertEquals("failure - setVolume did not correctly alter the volume", 4321, finance.getVolume(), 0);
	}
	
	public void testSetGetInstantVolume() throws Throwable
	{
		finance.setInstantVolume(6354);
		Assert.assertEquals("failure - setInstant volume did not correctly set the colume", 6354, finance.getInstantVolume());
	}
	
	public void testRocketExtremeValues()
	{
		finance.setClose(1000f);
		finance.setLast(1099);
		finance.calcRocketPlummet();
		
		assertFalse("failure - result is a rocket, although it shouldnt be", finance.isRocket());
		
		finance.setRocketConst((float)-1.1);
		finance.calcRocketPlummet();
		
		assertFalse("failure - result still shouldnt be a rocket", finance.isRocket());
	}
	
	public void testPlummetExtremeValues()
	{
		finance.setClose(1000f);
		finance.setLast(1099);
		finance.calcRocketPlummet();
		
		assertFalse("failure - result plummets although it shouldnt", finance.isPlummet());
		
		finance.setRocketConst((float)-1.1);
		finance.calcRocketPlummet();
		
		assertFalse("failure - result still shouldnt plummet", finance.isPlummet());
	}
	
	public void testRunExtremeValues()
	{
		finance.setClose(1000f);
		finance.setLast(1099);
		finance.calcRocketPlummet();
		
		assertFalse("failure - stock is on a run although it shouldnt be", finance.isRun());
		
		finance.setRunConst((float)-1.1);
		finance.calcRocketPlummet();
		
		assertFalse("failure - stock still shouldnt be on run", finance.isRun());
	}
	
	
	public void testCalcRunAndIsRun() throws Throwable
	{
		finance.setVolume(1000);
		finance.setInstantVolume(1099);
		finance.calcRun();
		
		assertFalse("failure - should not be on a run", finance.isRun());
		
		finance.setInstantVolume(1100);
		finance.calcRun();
		
		assertFalse("failure - should not be on run", finance.is_run);
		
		finance.setInstantVolume(1101);
		finance.calcRun();
		
		assertTrue("failure - Should be on a run!", finance.is_run);		
	}
	
	public void testCalcRocketPlummetAndIsPlummetIsRocket() throws Throwable
	{
		// Rocket boundary tests...
		finance.setClose(1000f);
		finance.setLast(1099);
		finance.calcRocketPlummet();
		
		assertFalse("failure - should not rocket at the moment", finance.isRocket());
		
		finance.setLast(1100);
		finance.calcRocketPlummet();
		
		assertFalse("failure - should not rocket", finance.isRocket());
		
		finance.setLast(1101);
		finance.calcRocketPlummet();
		
		assertTrue("failure - should be rocketing at the moment", finance.isRocket());
		
		// Plummet boundary tests...
		finance.setLast(801);
		finance.calcRocketPlummet();
		assertFalse("failure - should not plummet at the moment", finance.isPlummet());
		
		finance.setLast(800);
		finance.calcRocketPlummet();
		assertFalse("failure - should not plummet", finance.isPlummet());
		
		finance.setLast(799);
		finance.calcRocketPlummet();
		assertTrue("failure - should plummet at the moment!", finance.isPlummet());
		
	}
}