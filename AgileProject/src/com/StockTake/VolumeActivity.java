package com.StockTake;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class VolumeActivity extends Activity
{
		/** Called when the activity is first created. */
		StockManager myStockmanager;
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			myStockmanager = ((StockManager)getApplicationContext());
			
			setContentView(R.layout.volume);
			
			/* Create Error Table */
			TableLayout table = (TableLayout) this.findViewById(R.id.tableLayout2);

			if (checkInternetConnection())
            {
				try {
					int runs = myStockmanager.volumeTable(this);

                    if (runs == 0)
                    {
                        showError(table," <big>No Share Runs</big><br/><br/>There are no runs on any of your share portfolio for the past trading day.");
					}
					
				}
                catch(Exception e) {
					/* Parse Error */
                    showError(table," <big>Oops!</big><br/><br/> Something went wrong when we tried to retrieve your share portfolio.<br/><br/> Please try again later.");
				}
			}
            else
            {
				/* No Internet Connection */
                showError(table," <big>Oops!</big><br/><br/> It seems there is a problem with your internet connection.");
			}
		}

    /**
     * Method which adds a row to a table with an error text.
     *
     * @param table
     * @param errorText
     */
    private void showError(TableLayout table, String errorText)
    {
        TableRow errorRow = new TableRow(this);
        TextView error1 = new TextView(this);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 4;

        error1.setText(Html.fromHtml(errorText));
        errorRow.addView(error1, params);
        table.addView(errorRow);
    }

    /**
     * Check Connectivity to Internet
     */
    private boolean checkInternetConnection()
    {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // ARE WE CONNECTED TO THE INTERNET
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}