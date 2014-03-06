package com.StockTake;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;

import android.R.layout;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class StockManager extends Application {
	public FeedParser newParse;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		this.newParse = new FeedParser(getApplicationContext());

	}

	private List<Finance> portfolioList = new LinkedList<Finance>();

	private HashMap<String, String> stockNamesLong = new HashMap<String, String>();

	private String myState;

	public enum SortParameter {
		NAME, VALUE
	}

	// gets or sets the state
	public String getState() {
		return myState;
	}

	public void setState(String s) {
		myState = s;
	}

	
	// clear the portfolio hashmap - doesn't delete but keep overall structure
	// without content
	public void clearPortfolio() {
		portfolioList.clear();
		stockNamesLong.clear();
	}

	// create a new finance object
	public Finance createFinanceObject(String stockCode) throws IOException,
			JSONException {
		Finance newStock = new Finance(StockManager.this);

		// create a new parser with the new Finance Object
		this.newParse.parseJSON(newStock, stockCode);
		this.newParse.getHistoric(newStock, stockCode);

		// initialise newStock
		newStock.calcRun();
		newStock.calcRocketPlummet();

		return newStock;
	}

	// add entry to portfolio hashmap
	public boolean addPortfolioEntry(String stockCode, String stockNameLong,
			int numberOfShares) throws IOException, JSONException {
		Finance stockObj = createFinanceObject(stockCode); // doesnt work!

		stockObj.setTotal(stockObj.last * numberOfShares);
		stockObj.setNumberOfShares(numberOfShares);

		//System.out.println(stockObj.last + "Z1z1" + stockObj.getName()
		//		+ stockObj.getTotal());

		if (this.portfolioList.contains(stockObj)) {
			return false;
		}

		// portfolio.put(stockObj, shareQuantity); //add new Finance object to
		// portfolio hashmap
		portfolioList.add(stockObj);
		stockNamesLong.put(stockCode.substring(stockCode.indexOf(":") + 1),
				stockNameLong);
		return true;
	}

	public float getPortfolioTotal() {
		//System.out.println("stockmanager - getprotfolio");
		float value = 0;
		if (this.portfolioList.isEmpty()) {
			return 0;
		}

		for (Finance stockObj : portfolioList) {
			value += stockObj.getTotal();
		}

		return value;
	}

	public void summaryTable(Activity contextActivity, SortParameter sortBy) {
		// Find TableLayout defined in main.xml
		TableLayout table = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout2);
		
		DisplayMetrics screenSize = ScreenSize(contextActivity);

		TextView NameHeader = (TextView) contextActivity
				.findViewById(R.id.summaryNameHeader);
		NameHeader.setVisibility(View.VISIBLE);
		
		if(screenSize.widthPixels > 480){
			TextView ValuesHeader = (TextView) contextActivity
					.findViewById(R.id.summaryValueHeader);
			ValuesHeader.setVisibility(View.VISIBLE);
			TextView SharesHeader = (TextView) contextActivity
					.findViewById(R.id.summarySharesHeader);
			SharesHeader.setVisibility(View.VISIBLE);
		}
		
		TextView TotalHeader = (TextView) contextActivity
				.findViewById(R.id.summaryTotalHeader);
		TotalHeader.setVisibility(View.VISIBLE);
		
		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);

		// Stock Loop
		int stockCount = portfolioList.size();
		int stockCounter = 0;

		TableRow[] rowStock = new TableRow[stockCount];
		TextView[] stockName = new TextView[stockCount];
		TextView[] stockShares = new TextView[stockCount];
		TextView[] stockValue = new TextView[stockCount];
		TextView[] stockTotal = new TextView[stockCount];

		TableRow rowTotal = new TableRow(contextActivity);
		TextView portfolioTotal = new TextView(contextActivity);

		// Sort the list, depending on the sort parameter.
		switch (sortBy) {
		case NAME:
			Collections.sort(portfolioList, new NameComparator());
			break;

		case VALUE:
			Collections.sort(portfolioList, new TotalComparator());
			break;

		default:
			Collections.sort(portfolioList, new NameComparator());
			break;
		}

		for (Finance stockObj : portfolioList) {
			rowStock[stockCounter] = new TableRow(contextActivity);
			stockName[stockCounter] = new TextView(contextActivity);
			stockShares[stockCounter] = new TextView(contextActivity);
			stockValue[stockCounter] = new TextView(contextActivity);
			stockTotal[stockCounter] = new TextView(contextActivity);

			float thisStockValue = stockObj.getLast();

			// half up rounding mode - so reduces errors to +/- £1
			BigDecimal stockValueRounded = new BigDecimal(
					Double.toString(thisStockValue));
			stockValueRounded = stockValueRounded.setScale(2,
					BigDecimal.ROUND_DOWN);

			float thisStockTotal = stockObj.getTotal();

			// rounding down the stock total.
			BigDecimal stockTotalRounded = new BigDecimal(
					Double.toString(thisStockTotal));
			stockTotalRounded = stockTotalRounded.setScale(0,
					BigDecimal.ROUND_DOWN);

			// float subTotal = portfolio.get(stockObj) * thisStockValue;

			String longName = stockNamesLong.get(stockObj.getName().toString());

			stockName[stockCounter].setText(longName);
			stockName[stockCounter].setTypeface(Typeface.DEFAULT);
			stockName[stockCounter].setTextColor(Color.rgb(58, 128, 255));
			stockName[stockCounter].setTextSize(20f);
			stockName[stockCounter].setHeight(80);
			// stockName[stockCounter].setWidth(200);
			stockName[stockCounter].setGravity(Gravity.LEFT
					| Gravity.CENTER_VERTICAL);

			if(screenSize.widthPixels > 480){
				stockShares[stockCounter].setText(String.format("%,3.0f",
						(float) stockObj.getNumberOfShares()));
				stockShares[stockCounter].setGravity(Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);
				stockShares[stockCounter].setTextSize(20f);
				stockShares[stockCounter].setSingleLine(true);

				stockValue[stockCounter].setText("£"
						+ String.format("%.2f", stockValueRounded));
				stockValue[stockCounter].setGravity(Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);
				stockValue[stockCounter].setTextSize(20f);
				stockValue[stockCounter].setSingleLine(true);
			}

			stockTotal[stockCounter].setText("£"
					+ String.format("%,3.0f", stockTotalRounded));
			stockTotal[stockCounter].setGravity(Gravity.RIGHT
					| Gravity.CENTER_VERTICAL);
			stockTotal[stockCounter].setTextSize(20f);
			stockTotal[stockCounter].setSingleLine(true);


			rowStock[stockCounter].addView(stockName[stockCounter]);
			if(screenSize.widthPixels >= 480){
				rowStock[stockCounter].addView(stockShares[stockCounter]);
				rowStock[stockCounter].addView(stockValue[stockCounter]);
				}
			rowStock[stockCounter].addView(stockTotal[stockCounter]);

			table.addView(rowStock[stockCounter]);

			stockCounter++;
		}

		float potfolioTotal = getPortfolioTotal();

		// rounding down the Portfolio Total.
		BigDecimal potfolioTotalRounded = new BigDecimal(
				Double.toString(potfolioTotal));
		potfolioTotalRounded = potfolioTotalRounded.setScale(0,
				BigDecimal.ROUND_DOWN);

		String totalVal = "Total Portfolio Value:     £"
				+ String.format("%,.0f", potfolioTotalRounded);
		portfolioTotal.setText(totalVal);
		portfolioTotal.setTextSize(20f);
		portfolioTotal.setHeight(100);
		portfolioTotal.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

		TableRow.LayoutParams params = new TableRow.LayoutParams();
		params.span = 4;

		rowTotal.addView(portfolioTotal, params);
		table.addView(rowTotal);
	}

	public int volumeTable(Activity contextActivity) {
		// Find TableLayout defined in main.xml
		TableLayout table = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout2);

		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);

		int stockCount = portfolioList.size();
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

	public int rocketTable(Activity contextActivity) {
		// Find TableLayout defined in main.xml
		TableLayout table = (TableLayout) contextActivity
				.findViewById(R.id.tableLayout3);

		table.setStretchAllColumns(true);
		table.setShrinkAllColumns(true);

		int stockCount = portfolioList.size();
		int stockCounter = 0;
		int rocketplummet = 0;

		TableRow[] rowRocket = new TableRow[stockCount];
		TextView[] rocketStock = new TextView[stockCount];
		TextView[] rocketState = new TextView[stockCount];


    // Now sort...
        Collections.sort(portfolioList,new NameComparator());

        for (Finance stockObj : portfolioList)
        {
            rowRocket[stockCounter]   = new TableRow(contextActivity);
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
            else if (stockObj.isPlummet())
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

	private DisplayMetrics ScreenSize(Activity context){
		DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        
        return metrics;
	}
}
