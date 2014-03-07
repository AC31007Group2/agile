package com.StockTake;

import com.StockTake.StockManager.SortParameter;

public class SummaryActivityAlphabetical extends SummaryActivity implements Param
{
	/** Called when the activity is first created. */
	@Override
	/**
	 * @return sortParameter for sorting alphabetically
	 */
	public StockManager.SortParameter getParam() {
		return SortParameter.NAME ;
	}
}