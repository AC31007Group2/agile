package com.StockTake;

import com.StockTake.StockManager.SortParameter;

public class SummaryActivityRank extends SummaryActivity implements Param
{
	/** Called when the activity is first created. */
	@Override
	/*
	 * (non-Javadoc)
	 * @see com.StockTake.SummaryActivity#getParam()
	 * @return sortParameter for sorting by rank
	 */
	public StockManager.SortParameter getParam() {
		return SortParameter.VALUE ;
	}
}