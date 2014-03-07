package com.StockTake;

import java.io.IOException;
import org.json.JSONException;
import com.StockTake.StockManager.SortParameter;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;

public class SummaryActivity extends Activity implements Param
{
	private StockManager myStockmanager;
	private TableLayout table; 
	private TableRow errorRow;
	private TextView error1;
	private TableRow.LayoutParams params;
	private static AsyncTask<Activity, Void, List<String>> taskWaiting = null;
	
	@Override
	/**
	 * @return sortParameter for sorting by name
	 */
	public StockManager.SortParameter getParam() {
		return SortParameter.NAME ;
	}
	
	@Override
	/**
     * Called by android on activity creation
	 */
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		myStockmanager = ((StockManager)getApplicationContext());
		setContentView(R.layout.summary);	
	    update();
	}
	
	/**
	 * Updates the visible tab
	 */
	public void update() {
		table = (TableLayout) this.findViewById(R.id.tableLayout1);
		if (taskWaiting == null) {
            if (checkInternetConnection()) {
                try {
                    errorRow = new TableRow(this);
                    error1 = new TextView(this);
                    params = new TableRow.LayoutParams();
                    params.span = 4;
                    onClick();
                } catch(Exception e) {
                    /* Parse Error */
                	error1.setText(Html.fromHtml(" <big>Oops!</big><br/><br/> Something went wrong when we tried to retrieve your share portfolio.<br/><br/> Please try again later."));
                	errorRow.addView(error1, params);
                    table.addView(errorRow);
                }
            } else {
                /* No Internet Connection */
                error1.setText(Html.fromHtml(" <big>Oops!</big><br/><br/> It seems there is a problem with your internet connection."));
                errorRow.addView(error1, params);
                table.addView(errorRow);
            }
        }
	}
	
	/**
	 * Refreshes the page
	 */
	public void onClick() throws IOException, JSONException {
        if (SummaryActivity.taskWaiting == null)
        {
            SummaryActivity.taskWaiting = new CreateFinanceObjectAsync().execute(this, null, null);
        }
    }
	
	/**
	 * Checks for an active internet connection
	 */
	private boolean checkInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * This class creates an Async finance object within the Tabactivity
	 */
	private class CreateFinanceObjectAsync extends AsyncTask<Activity, Void, List<String>>
	{
		private SummaryActivity parent;
		
		@Override
		/**
		 * Called by android before AsyncTask Execution
		 */
		protected void onPreExecute() {
			ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
			pb.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		/**
         * The method to run in the background
         * @param params var arg of activities, only uses the first
		 * @return List of Problems - one problem for each Object that failed to display/parse
		 */
		protected List<String> doInBackground(Activity... params) {
			parent = (SummaryActivity) params[0];
			myStockmanager.clearPortfolio();
			
			List<String> problems = new ArrayList<String>(); //create empty list of problems
            if (this.isCancelled())
            {
                return problems;
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			if (preferences.getBoolean("is_using_broken_data", false)) {
				problems.add(checkStock("BPEEE", "BP Amoco Plc", 192));
			}
			
			problems.add(checkStock("SN", "S & N", 1219));
			problems.add(checkStock("BP", "BP", 192));
			problems.add(checkStock("HSBA", "HSBC.", 343));
			problems.add(checkStock("EXPN", "Experian", 258));
			problems.add(checkStock("MKS", "M & S", 485));
			
			return deleteSpaces(problems);
		}

		/**
		 * Delete empty spaces in list
		 * @param problems of problems potentially containing spaces
		 * @return List of problems without spaces
		 */
		private List<String> deleteSpaces(List<String> problems) {
			List<String> list = new ArrayList<String>();
		    for(String s : problems) {
		       if(s != null && s.length() > 0) {
		          list.add(s);
		       }
		    }
			return list;
		}

		/**
		 * This method checks the availability of each stock and adds an error to the list of errors
		 * if the stock is not available
		 * 
		 *  @param  name stock name
		 *  @param  symbol of the stock
		 *  @param  amount of stocks
		 *  @return String error
		 */
		private String checkStock(String name, String symbol, int amount) {
            try
            {
				myStockmanager.addPortfolioEntry(name, symbol, amount);
                return "";
			} catch(Exception e)
			{
				return name;
			}
		}

        @Override
        /**
		 * Called when task is cancelled
		 */
        protected void onCancelled() {
            taskWaiting = null;
        }

		@Override
		/**
		 * method run after async task has completed
		 */
		protected void onPostExecute(List<String> result) {
			String theText="";
			if (result.isEmpty()) {
				TextView tv = (TextView)findViewById(R.id.errorText);
				tv.setVisibility(View.GONE);	
			}
			else {
				String[]printStore = new String[result.size()];				
				result.toArray(printStore);
				
				for (int i=0; i<result.size(); i++)
				{
					if (i > 0) { theText += ", "; }
					theText += printStore[i].toString() ;
				}
				TextView tv = (TextView)findViewById(R.id.errorText);
				tv.setText(theText + " shares currently unavailable");
				tv.setVisibility(View.VISIBLE);	
			}
			ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
			pb.setVisibility(View.GONE);
			myStockmanager.summaryTable(parent,getParam());
            SummaryActivity.taskWaiting = null;

			super.onPostExecute(result);
		}
	}
}