package com.StockTake;


public class Finance
{

	public String name; // Stock name
	public float last  = 0; // Last stock value
	public String market; // Market
	public float close = 0;
	public int volume = 0;
	public int instant_volume = 0;
	public boolean is_run;
	public boolean is_rocket;
	public boolean is_plummet;
	
	public final float RUN_CONST = 1.1f;
	public final float ROCKET_CONST = 1.1f;
	public final float PLUMMET_CONST = 0.8f;

	public Finance()
	{
		name = "Default";
		last = 0;
		System.out.println("Finance - constructor");
	}

	public void setLast(float newLast)
	{
		System.out.println("finance - setlast");
		last = newLast;
	}

	public float getLast()
	{
		System.out.println("finacne - getlast");
		return last;
	}

	public void setName(String newName)
	{
		System.out.println("finance - setname");
		name = newName;
	}

	public String getName()
	{
		System.out.println("finance - getname");
		return name;
	}

	public void setMarket(String newMarket)
	{
		System.out.println("finance - setmarket");
		market = newMarket;
	}

	public String getMarket()
	{
		System.out.println("finance - getmarket");
		return market;
	}

	public String getSummary()
	{
		System.out.println("finacne - getsummary");
		return name + ":  " + last;
	}
	
	public void setClose(float newClose)
	{
		System.out.println("finance - setclaose");
		close = newClose;
	}

	public float getClose()
	{
		System.out.println("finance - getclose");
		return close;
	}
	
	public void setVolume(int newVol)
	{
		System.out.println("finance - setvolume");
		volume = newVol;
	}

	public int getVolume()
	{
		System.out.println("finance - getvolume");
		return volume;
	}
	
	public void setInstantVolume(int newVol)
	{
		System.out.println("finance - setinstan");
		instant_volume = newVol;
	}

	public int getInstantVolume()
	{
		System.out.println("finance - getinstant");
		return instant_volume;
	}
	
	public boolean isRun() {
		System.out.println("finance - isrun");
		return is_run;
	}
	
	public boolean isRocket() {
		System.out.println("finance - isrocket");
		return is_rocket;
	}
	
	public boolean isPlummet() {
	System.out.println("finance - isplummet");
		return is_plummet;
	}
	
	public void calcRun() {
		System.out.println("finance - calcrun");
		if (volume != 0 && instant_volume != 0) {
			if (instant_volume > (RUN_CONST * volume)) {
				is_run = true;
			} else {
				is_run = false;
			}
		}
	}
	
	public void calcRocketPlummet() {
		System.out.println("finance - calcrocketplummet");
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
}
