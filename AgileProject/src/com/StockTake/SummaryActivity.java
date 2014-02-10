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


enum status { success, failure };


public class SummaryActivity extends Activity implements Param
{
	/** Called when the activity is first created. */
	@Override
	public StockManager.SortParameter getParam() {
		return SortParameter.NAME ;
	}

	StockManager myStockmanager;
	
	/* Create Error Messages */
	TableLayout table; 
	TableRow errorRow;
	TextView error1;
	TableRow.LayoutParams params;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		
		System.out.println("summaryactivity - oncreate");
		// Get the StockManager
		myStockmanager = ((StockManager)getApplicationContext());
		
		setContentView(R.layout.summary);	

	    update();
	}
	
	
	public void update() {
		System.out.println("summaryactivity - update");
		table = (TableLayout) this.findViewById(R.id.tableLayout1); 
		
		errorRow = new TableRow(this);
		error1 = new TextView(this);
		params = new TableRow.LayoutParams();  
	    params.span = 4;
		
		if (checkInternetConnection()) {
			try {
				System.out.println("do this!");
				onClick();
				System.out.println("and this!");
				
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
	private class CreateFinanceObjectAsync extends AsyncTask<Activity, Void, List<String>>
	{
		Activity parent;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			ProgressBar pb = (ProgressBar)findViewById(R.id.progressBar1);
			pb.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected List<String> doInBackground(Activity... params) {
			// TODO Auto-generated method stub
			parent = params[0];
			System.out.println("summaryactivity - onclick");
			myStockmanager.clearPortfolio();
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			boolean useBrokenData = preferences.getBoolean("is_using_broken_data", false);
			
			List<String> problems = new ArrayList<String>();
			

			try {
			myStockmanager.addPortfolioEntry("SN", "Smith & Nephew Plc Ord.", 1219);
			}catch(Exception e)
			{
				problems.add("SN");
			}
			try{
				if (useBrokenData) {
					myStockmanager.addPortfolioEntry("BPEEE", "BP Amoco Plc", 192);
				}
				else {
					myStockmanager.addPortfolioEntry("BP", "BP Amoco Plc", 192);
				}
			}catch(Exception e)
			{
				problems.add("BP");
				
			}
			try{
			myStockmanager.addPortfolioEntry("HSBA", "HSBC Holdings Plc Ord.", 343);
			}catch(Exception e)
			{
				problems.add("HSBA");
			}
			try{
			myStockmanager.addPortfolioEntry("EXPN", "Experian", 258);
			}catch(Exception e)
			{
				problems.add("EXPN");
			}
			try {
				myStockmanager.addPortfolioEntry("MKS", "Marks & Spencer Ord.", 485);
			} catch(Exception e)
			{
				problems.add("MKS");
			}
					
				
			return problems;
				
		
		}
		
		@Override
		protected void onPostExecute(List<String> result) {
			String theText="";
					
			// TODO Auto-generated method stub
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
			super.onPostExecute(result);
		}
		
	}
	/* Click Refresh */
	public void onClick() throws IOException, JSONException {
		new CreateFinanceObjectAsync().execute(this, null, null);
		System.out.println("summaryactivity - onclick");
	}
	
	private boolean checkInternetConnection() {
		System.out.println("summaryactivity - checkinternetconnection");
		
	
		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	
		// ARE WE CONNECTED TO THE INTERNET
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}
}
