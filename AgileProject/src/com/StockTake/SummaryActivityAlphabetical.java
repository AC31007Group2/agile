package com.StockTake;


import com.StockTake.StockManager.SortParameter;

public class SummaryActivityAlphabetical extends SummaryActivity implements Param
{
	/** Called when the activity is first created. */
	@Override
	public StockManager.SortParameter getParam() {
		return SortParameter.NAME ;
	}

}