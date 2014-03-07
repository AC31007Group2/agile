package com.StockTake;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RocketActivity extends Activity
{
		StockManager myStockmanager;
		
		@Override
        /**
         * Method called by android on activity creation
         */
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			myStockmanager = ((StockManager)getApplicationContext());
			setContentView(R.layout.rocket);
			
			if (checkInternetConnection()) {
				try {
					int rocketplummet = myStockmanager.rocketTable(this);
					if (rocketplummet == 0) { 
						writeError(" <big>No Alerts</big><br/><br/>There are no rockets or plummets on any of your share portfolio today.");
					}
				} catch(Exception e) {
					/* Parse Error */ 
					writeError(" <big>Oops!</big><br/><br/> Something went wrong when we tried to retrieve your share portfolio.<br/><br/> Please try again later.");
				}
					
			} else {
				/* No Internet Connection */
				writeError(" <big>Oops!</big><br/><br/> It seems there is a problem with your internet connection.");
			}
		}

		/**
		 * Creates a error message for the user
		 * @param message - message to write in error
		 */
		private void writeError(String message) {
			/* Create Error Messages */
			TableLayout table = (TableLayout) this.findViewById(R.id.tableLayout3); 
			TableRow errorRow = new TableRow(this);
			TextView error1 = new TextView(this);
			TableRow.LayoutParams params = new TableRow.LayoutParams();  
		    params.span = 4;
			error1.setText(Html.fromHtml(message));
			errorRow.addView(error1, params);
			table.addView(errorRow);
		}

		/**
		 * Checks for a active internet connection
		 * @return connection
		 */
		private boolean checkInternetConnection() {
			System.out.println("rocketactivity - checkinternetconnection");
			ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
			// ARE WE CONNECTED TO THE INTERNET
			if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
				return true;
			} else {
				return false;
			}
		}
	}