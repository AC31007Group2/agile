package com.StockTake;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;


import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StockManager extends Application
{
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		this.newParse = new FeedParser(getApplicationContext());
	}

	// private Finance stockObj;
	private HashMap<Finance, Float> portfolio = new HashMap<Finance, Float>();
	
	List<Finance> portfolioList = new LinkedList<Finance>();
	
	private HashMap<String, String> stockNamesLong = new HashMap<String, String>();
	
	FeedParser newParse;

	private String myState;

public enum SortParameter 
   {
        NAME, VALUE
   }
	    

	//gets or sets the state
	public String getState(){
		System.out.println("stockmanager - getstate");
		return myState;}
	public void setState(String s){
		System.out.println("stockmanager - setstate");
		myState = s;}


	//clear the portfolio hashmap - doesn't delete but keep overall structure without content
	public void clearPortfolio(){
		System.out.println("stockmanager - clearportfolio");
		portfolio.clear();
		portfolioList.clear();
		stockNamesLong.clear();
	}

	//create a new finance object
	public Finance createFinanceObject(String stockCode) throws IOException, JSONException{
		System.out.println("stockmanager - createfinance");
		Finance newStock = new Finance();
		System.out.println("stockmanager - createfinance - finance created");
		//create a new parser with the new Finance Object
		newParse.parseJSON(newStock, stockCode);
		newParse.getHistoric(newStock, stockCode);
		//initialise newStock
		System.out.println("stockmanager - createfinance - end JSON");
		
		newStock.calcRun();
		newStock.calcRocketPlummet();
		System.out.println("stockmanager - createfinance - before return");
		return newStock;
	}

	//add entry to portfolio hashmap
	public boolean addPortfolioEntry(String stockCode, String stockNameLong, int numberOfShares) throws IOException, JSONException{
		System.out.println("stockmanager - addportfolio");
		float shareQuantity = (float) numberOfShares;
		System.out.println("addportfolio");
		System.out.println("The Stockcode is: " + stockCode);
		Finance stockObj = createFinanceObject(stockCode); //doesnt work!
		stockObj.setTotal(stockObj.last*numberOfShares);
		
		System.out.println("finance object created");
		if (portfolio.containsKey(stockObj))
		{
			return false;
		}
		System.out.println("portfolio contains key");
		portfolio.put(stockObj, shareQuantity); //add new Finance object to portfolio hashmap
		portfolioList.add(stockObj);
		stockNamesLong.put(stockCode.substring(stockCode.indexOf(":") + 1), stockNameLong);
		return true;
	}
	
	public float getPortfolioTotal()
	{
		System.out.println("stockmanager - getprotfolio");
		float value = 0;
		if (portfolio.isEmpty())
		{
			return 0;
		}
		for (Finance stockObj : portfolio.keySet())
		{
			value += stockObj.getLast() * portfolio.get(stockObj);
		}
		
		return value;
	}

	public void summaryTable(Activity contextActivity,SortParameter sortBy)
	{
		System.out.println("Stockmanager - summarytable");
		TableLayout table = (TableLayout) contextActivity.findViewById(R.id.tableLayout1); // Find
																							// TableLayout
																							// defined
																							// in
																							// main.xml

		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);

		// Stock Loop
		int stockCount = portfolio.size();
		int stockCounter = 0;

		TableRow[] rowStock = new TableRow[stockCount];
		TextView[] stockName = new TextView[stockCount];
		TextView[] stockShares = new TextView[stockCount];
		TextView[] stockValue = new TextView[stockCount];
		TextView[] stockTotal = new TextView[stockCount];

		TableRow rowTotal = new TableRow(contextActivity);
		TextView portfolioTotal = new TextView(contextActivity);
		
		// Sort the list, depending on the sort parameter.
		switch(sortBy)
		{
			case NAME: 
				Collections.sort(portfolioList,new NameComparator());
				break;
			
			case VALUE:
				Collections.sort(portfolioList,new TotalComparator());
				break;
				
			default:
				Collections.sort(portfolioList,new NameComparator());
				break;
		}
		
		for (Finance stockObj : portfolioList)
		{
			
			System.out.println("3");
			rowStock[stockCounter] = new TableRow(contextActivity);
			stockName[stockCounter] = new TextView(contextActivity);
			stockShares[stockCounter] = new TextView(contextActivity);
			stockValue[stockCounter] = new TextView(contextActivity);
			stockTotal[stockCounter] = new TextView(contextActivity);

			float thisStockValue = stockObj.getLast();

			// half up rounding mode - so reduces errors to +/- £1
			BigDecimal stockValueRounded = new BigDecimal(Double.toString(thisStockValue));
			stockValueRounded = stockValueRounded.setScale(2, BigDecimal.ROUND_DOWN);
			
			float thisStockTotal = (stockValueRounded.floatValue() * portfolio.get(stockObj));;
			
			//rounding down the stock total.
			BigDecimal stockTotalRounded = new BigDecimal(Double.toString(thisStockTotal));
			stockTotalRounded = stockTotalRounded.setScale(0, BigDecimal.ROUND_DOWN);	
			
			//float subTotal = portfolio.get(stockObj) * thisStockValue;

			String longName = stockNamesLong.get(stockObj.getName().toString());

			stockName[stockCounter].setText(longName);
			stockName[stockCounter].setTypeface(Typeface.DEFAULT);
			stockName[stockCounter].setTextColor(Color.rgb(58, 128, 255));
			stockName[stockCounter].setTextSize(20f);
			stockName[stockCounter].setHeight(70);
			stockName[stockCounter].setWidth(200);
			stockName[stockCounter].setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

			stockShares[stockCounter].setText(String.format("%,3.0f", portfolio.get(stockObj)));
			stockShares[stockCounter].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			stockShares[stockCounter].setTextSize(20f);
			stockShares[stockCounter].setSingleLine(true);

			stockValue[stockCounter].setText("£" + String.format("%.2f", stockValueRounded));
			stockValue[stockCounter].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			stockValue[stockCounter].setTextSize(20f);
			stockValue[stockCounter].setSingleLine(true);

			stockTotal[stockCounter].setText("£" + String.format("%,3.0f", stockTotalRounded));
			stockTotal[stockCounter].setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			stockTotal[stockCounter].setTextSize(20f);
			stockTotal[stockCounter].setSingleLine(true);

			rowStock[stockCounter].addView(stockName[stockCounter]);
			rowStock[stockCounter].addView(stockShares[stockCounter]);
			rowStock[stockCounter].addView(stockValue[stockCounter]);
			rowStock[stockCounter].addView(stockTotal[stockCounter]);

			table.addView(rowStock[stockCounter]);

			stockCounter++;
			System.out.println("6");
		}
		
		float potfolioTotal = getPortfolioTotal();
		
		//rounding down the Portfolio Total.
		BigDecimal potfolioTotalRounded = new BigDecimal(Double.toString(potfolioTotal));
		potfolioTotalRounded = potfolioTotalRounded.setScale(0, BigDecimal.ROUND_DOWN);	
		
		System.out.println("1");
		String totalVal = "Total Portfolio Value:     £" + String.format("%,.0f", potfolioTotalRounded);
		portfolioTotal.setText(totalVal);
		portfolioTotal.setTextSize(20f);
		portfolioTotal.setHeight(100);
		portfolioTotal.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.span = 4;

		rowTotal.addView(portfolioTotal, params);
		table.addView(rowTotal);
		System.out.println("7");
	}

	
	public int volumeTable(Activity contextActivity)
	{
		System.out.println("stockmanager - volumnetable");
		TableLayout table = (TableLayout) contextActivity.findViewById(R.id.tableLayout2); // Find
																							// TableLayout
																							// defined
																							// in
																							// main.xml

		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);

		int stockCount = portfolio.size();
		int stockCounter = 0;
		int runs = 0;

		TableRow[] rowRun = new TableRow[stockCount];
		TextView[] runStock = new TextView[stockCount];
		TextView[] runLabel = new TextView[stockCount];

		// Now sort...
		Collections.sort(portfolioList,new NameComparator());
		
		for (Finance stockObj : portfolioList)
		{
			

			rowRun[stockCounter] = new TableRow(contextActivity);
			runStock[stockCounter] = new TextView(contextActivity);
			runLabel[stockCounter] = new TextView(contextActivity);

			if (stockObj.isRun())
			{
				runStock[stockCounter].setText(stockNamesLong.get(stockObj.getName().toString()));
				runStock[stockCounter].setTextSize(20f);
				runStock[stockCounter].setHeight(100);
				runStock[stockCounter].setGravity(Gravity.CENTER_VERTICAL);
				runLabel[stockCounter].setText("Run");
				runLabel[stockCounter].setTextSize(20f);
				runLabel[stockCounter].setHeight(100);
				runLabel[stockCounter].setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				rowRun[stockCounter].addView(runStock[stockCounter]);
				rowRun[stockCounter].addView(runLabel[stockCounter]);
				runs++;
			}

			table.addView(rowRun[stockCounter]);

			stockCounter++;

		}

		return runs;

	}

	public int rocketTable(Activity contextActivity)
	{
		System.out.println("stockmanager - rockettable");
		TableLayout table = (TableLayout) contextActivity.findViewById(R.id.tableLayout3); // Find
																							// TableLayout
																							// defined
																							// in
																							// main.xml

		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);

		int stockCount = portfolio.size();
		int stockCounter = 0;
		int rocketplummet = 0;

		TableRow[] rowRocket = new TableRow[stockCount];
		TextView[] rocketStock = new TextView[stockCount];
		TextView[] rocketState = new TextView[stockCount];

		// Now sort...
		Collections.sort(portfolioList,new NameComparator());
		
		for (Finance stockObj : portfolioList)
		{
			

			rowRocket[stockCounter] = new TableRow(contextActivity);
			rocketStock[stockCounter] = new TextView(contextActivity);
			rocketState[stockCounter] = new TextView(contextActivity);
			rocketState[stockCounter].setGravity(Gravity.RIGHT);
			rocketState[stockCounter].setTextSize(20f);

			rocketStock[stockCounter].setTextSize(20f);
			rocketStock[stockCounter].setHeight(100);
			rocketStock[stockCounter].setGravity(Gravity.CENTER_VERTICAL);
			rocketStock[stockCounter].setText(stockNamesLong.get(stockObj.getName().toString()));

			if (stockObj.isRocket())
			{
				rocketState[stockCounter].setText(Html.fromHtml("<font color='green'>Rocket</font>"));
				rowRocket[stockCounter].addView(rocketStock[stockCounter]);
				rowRocket[stockCounter].addView(rocketState[stockCounter]);
				rocketplummet++;
			}
			else
				if (stockObj.isPlummet())
				{
					rocketState[stockCounter].setText(Html.fromHtml("<font color='red'>Plummet</font>"));
					rowRocket[stockCounter].addView(rocketStock[stockCounter]);
					rowRocket[stockCounter].addView(rocketState[stockCounter]);
					rocketplummet++;
				}

			table.addView(rowRocket[stockCounter]);

			stockCounter++;

		}

		return rocketplummet;

	}

}
