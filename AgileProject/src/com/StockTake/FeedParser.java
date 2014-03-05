package com.StockTake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class FeedParser
{
	private Context m_context;

	public FeedParser(Context m_context) {
		this.m_context = m_context;
	}

	public void parseJSON(Finance toPopulate, String currentStock) throws IOException, JSONException
	{
		// Create JSON and Finance objects

		JSONObject jObject;
		
		// Generate URL
		URL feedUrl = new URL("http://finance.google.com/finance/info?client=ig&infotype=infoquoteall&q=LON:" + currentStock);
		// Read JSON
		InputStream is = feedUrl.openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1)
		{
			sb.append((char) cp);
		}
		String jsonText = sb.toString();
		jsonText = jsonText.substring(5, jsonText.length() - 2);
		is.close();
		// Init object
		
		jObject = new JSONObject(jsonText);
		
		// Use this, just because some shares (expn) use comma to separate large values.
		String tmpString = jObject.getString("l");
		tmpString = tmpString.replace(",","");

		// Set 'Last' value
		toPopulate.setLast(Float.parseFloat(tmpString) / 100f);

		//Log.v("LOGCATZ", " " + toPopulate.getLast());
		// Set 'Company' name
		toPopulate.setName(jObject.getString("t"));

		// Set 'Market'
		toPopulate.setMarket(jObject.getString("e"));
		// Set 'Instant Volume'
		int instantVolume = volCharToInt(jObject.getString("vo"));
		toPopulate.setInstantVolume(instantVolume);

	}
	
	public boolean getHistoric(Finance toPopulate, String stockToGet) {


			BufferedReader csvBr;
			String csvData[] = null;
			
			try {
				csvBr   = getCsvFeed(stockToGet);
				csvData = parseCsvString(csvBr);

			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return false;
			}
			if(csvData != null)
			{
				toPopulate.setClose(Float.parseFloat(csvData[0]) / 100f);
				toPopulate.setVolume(Integer.parseInt(csvData[1]));
				return true;
			}
			else
			{
				return false;
			}
	}
	
	public BufferedReader getCsvFeed(String stockSymbol) throws IOException {
		
		// Check dates
		Calendar cal = Calendar.getInstance();
		
		int day   = cal.get(Calendar.DATE) - 4;
		int month = cal.get(Calendar.MONTH);
		int year  = cal.get(Calendar.YEAR);

		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(m_context);

		boolean useTestData = preferences.getBoolean("is_using_mock_data", false);
		URL feedUrl = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + stockSymbol + ".L" + "&a=" + month + "&b=" + day + "&c="+year);
		
		if(useTestData)
		{
			feedUrl = new URL("http://beberry.lv/stocks/" + stockSymbol + ".csv");
		};

		InputStream is = feedUrl.openStream();
		
		return new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));			
	}
	
	public String[] parseCsvString(BufferedReader csvToParse) throws IOException 
	{
		String strLine 	   = "";
		StringTokenizer st = null;
		int lineNumber     = 0;
		int tokenNumber    = 0;
		
		String csvdata[] = null;
		
		if(csvToParse != null)
		{
			while(((strLine = csvToParse.readLine()) != null))
			{
				lineNumber++;
				
				if (lineNumber == 2)
				{
					st = new StringTokenizer(strLine, ",");
					String token;
					
					while(st.hasMoreTokens())
					{
						if(csvdata == null)
						{
							csvdata = new String[2];
						}
						
						tokenNumber++;
						token = st.nextToken();
							
						if (tokenNumber == 5)
						{
							csvdata[0] = token;
						}
						
						if (tokenNumber == 6)
						{
							csvdata[1] = token;
						}
					}
					
					tokenNumber = 0;
				}
			}
		}
		
		return csvdata;	
	}
	
	public int volCharToInt(String amount)
	{
		float convertedVal = 0;
		int multiplier = 1;
		int returnValue = 0;
		
		try
		{
			amount = amount.replaceAll(",", "");
			
			String valComponent = amount.substring(0, amount.length()-1);
			String multComponent = amount.substring(amount.length()-1);
			convertedVal = Float.parseFloat(valComponent);
			multComponent = multComponent.toUpperCase();
			
			if (multComponent.equals("M"))
			{
				multiplier = 1000000;
			}
			if (multComponent.equals("K"))
			{
				multiplier = 1000;
			}
			
			convertedVal = convertedVal * (float)multiplier;
			returnValue = (int) convertedVal;
		}
		catch(Exception e)
		{
			returnValue = 0;
		}
		
		if(returnValue < 0)
		{		
			return 0;
		}
		else
		{
			return returnValue;
		}
	}
	
	
	
}
