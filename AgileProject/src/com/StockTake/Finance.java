package com.StockTake;

//import java.io.ObjectInputStream.GetField;
import java.util.Comparator;

//import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Finance
{
	public String name; 			// Stock name
	public float last  = 0;			// Last stock value
	public String market; 			// Market
	public float close;
	public int volume;				// Looks like a historic volume from x (4) days ago, using the CSV from yahoo api.
	public int instant_volume = 0;  // Current volume, set when the stock object is being created, uses the data from goole api.
	public float total; 			// total stock value.
	public boolean is_run;
	public boolean is_rocket;
	public boolean is_plummet;
	public int numberOfShares;
	
	private float RUN_CONST;//	 = 1.1f;    // Looks like some kind of a coeff for RUNs
	private float ROCKET_CONST;// = 1.1f;    // Looks like some kind of a coeff for ROCKETs
	private float PLUMMET_CONST;// = 0.8f;   
	
	public static final String PREFS_NAME = "strings";
	public Context context;
	

	public Finance(Context context)
	{
		this.context = context;
		initialiseValues();
		name = "Default";
		last = 0;
	}
	
	/*
	 * Get current value of floats for calculations
	 */
	public void initialiseValues()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		this.setRocketConst(Float.parseFloat(preferences.getString("ROCKET_CONST","1.1f")));
		this.setRunConst(Float.parseFloat(preferences.getString("RUN_CONST","1.1f")));
		this.setPlummetConst(Float.parseFloat(preferences.getString("PLUMMET_CONST","0.8f")));
	}

	public void setLast(float newLast)
	{
		//System.out.println("finance - setlast");
		last = newLast;
	}

	public float getLast()
	{
		//System.out.println("finacne - getlast");
		return last;
	}

	public void setName(String newName)
	{
		//System.out.println("finance - setname");
		name = newName;
	}

	public String getName()
	{
		return name;
	}

	public void setMarket(String newMarket)
	{
		//System.out.println("finance - setmarket");
		market = newMarket;
	}

	public String getMarket()
	{
//		System.out.println("finance - getmarket");
		return market;
	}

	public String getSummary()
	{
		//System.out.println("finacne - getsummary");
		return name + ":  " + last;
	}
	
	public void setClose(float newClose)
	{
		//System.out.println("finance - setclaose");
		close = newClose;
	}

	public float getClose()
	{
		//System.out.println("finance - getclose");
		return this.close;
	}
	
	public void setVolume(int newVol)
	{
		//System.out.println("finance - setvolume");
		volume = newVol;
	}

	public int getVolume()
	{
		//System.out.println("finance - getvolume");
		return this.volume;
	}
	
	public void setInstantVolume(int newVol)
	{
		//System.out.println("finance - setinstan");
		instant_volume = newVol;
	}

	public int getInstantVolume()
	{
		//System.out.println("finance - getinstant");
		return instant_volume;
	}	
	
	
	public float getTotal() {
		//System.out.println("finance - getTotal");
		return total;
	}

	public void setTotal(float total) {
		//System.out.println("finance - setTotl");
		this.total = total;
	}

	public boolean isRun() {
		//System.out.println("finance - isrun");
		return is_run;
	}
	
	public boolean isRocket() {
		//System.out.println("finance - isrocket");
		return is_rocket;
	}
	
	public boolean isPlummet() {
	//System.out.println("finance - isplummet");
		return is_plummet;
	}
	
	public void calcRun() {
		initialiseValues();

		//System.out.println("finance - calcrun");
		if (volume != 0 && instant_volume != 0) {
			if (instant_volume > (RUN_CONST * volume)) {
				is_run = true;
			} else {
				is_run = false;
			}
		}
	}
	
	public int getNumberOfShares()
	{
		return this.numberOfShares;
	}
	
	public void setNumberOfShares(int shareCount)
	{
		this.numberOfShares = shareCount;
	}
	
	public void calcRocketPlummet() {
		//System.out.println("finance - calcrocketplummet");
		initialiseValues();
		is_plummet = false;
		is_rocket = false;
		if (last != 0 && close != 0) {
			if (last > (ROCKET_CONST * close)) {
				is_rocket = true;
			} else if (last < (PLUMMET_CONST * close)) {
				is_plummet = true;
			}	
		}
	}
	
	public void setRocketConst(float newConst)
	{
		if(newConst > 0)
		{
			this.ROCKET_CONST = newConst;
		}
	}
	
	public void setRunConst(float newConst)
	{
		if(newConst > 0)
		{
			this.RUN_CONST = newConst;
		}
	}
	
	public void setPlummetConst(float newConst)
	{
		if(newConst > 0)
		{
			this.PLUMMET_CONST = newConst;
		}
	}
}
	
	class NameComparator implements Comparator<Finance> {
	    @Override
		public int compare(Finance o1, Finance o2) {
			return o1.name.compareTo(o2.name);
		}
	}
	
	class ValueComparator implements Comparator<Finance> {
	    @Override
		public int compare(Finance o1, Finance o2) {
			
				if (o1.last > o2.last) {
					return -1;
				} else if (o1.last == o2.last) {
					return 0;
				} else {
					return 1;
				}
		}
	}
	
	class TotalComparator implements Comparator<Finance> {
	    @Override
		public int compare(Finance o1, Finance o2) {
			
				if ((o1.total)> (o2.total)) {
					return -1;
				} else if ((o1.total) == (o2.total)) {
					return 0;
				} else {
					return 1;
				}
		}
	}
