package com.StockTake;

import com.StockTake.StockManager.SortParameter;

public class SummaryActivityRank extends SummaryActivity implements Param
{
	/** Called when the activity is first created. */
	@Override
	/**
	 * @return sortParameter for sorting by rank
	 */
	public StockManager.SortParameter getParam() {
		return SortParameter.VALUE ;
	}
}