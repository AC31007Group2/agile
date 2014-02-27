package com.StockTake;

import com.StockTake.StockManager.SortParameter;

public class SummaryActivityRank extends SummaryActivity implements Param
{
	/** Called when the activity is first created. */
	@Override
	public StockManager.SortParameter getParam() {
		return SortParameter.VALUE ;
	}


}