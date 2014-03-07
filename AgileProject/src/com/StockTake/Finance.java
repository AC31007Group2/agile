package com.StockTake;

import java.util.Comparator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Finance
{
    private String stockSymbol;     // Stock stockSymbol
    private float lastValue;	    // Last stock value
    private String market;          // Market
    private float closingValue;     // The value of the share when the market was closing.
    private int volumeHistoric;     // Looks like a historic volume from x(was set to 4) days ago, using the CSV from yahoo api.
    private int volumeInstant;      // Current volume, set when the stock object is being created, uses the data from goole api.
    private float totalValue; 	    // Total stock value.
    private boolean is_run;
    private boolean is_rocket;
    private boolean is_plummet;
	private int numberOfShares;
	private float RUN_CONST;       // Looks like some kind of a coeff for RUNs
	private float ROCKET_CONST;    // Looks like some kind of a coeff for ROCKETs
	private float PLUMMET_CONST;

	public Context context;

	public Finance(Context context)
	{
        this.lastValue = 0;
        this.volumeInstant = 0;
        this.stockSymbol = "Default";
		this.context = context;
        this.getConstants();
	}

    /**
     * Method that gets constants form the UI / preferences
     */
	private void getConstants()
	{
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.setRocketConst(Float.parseFloat(preferences.getString("ROCKET_CONST","1.1f")));
		this.setRunConst(Float.parseFloat(preferences.getString("RUN_CONST","1.1f")));
		this.setPlummetConst(Float.parseFloat(preferences.getString("PLUMMET_CONST","0.8f")));
	}

    /**
     * Method that sets the lastValue value of the stock
     *
     * @param newLast
     */
	public void setLastValue(float newLast)
	{
		this.lastValue = newLast;
	}

    /**
     * Method which returns the last value of this stock.
     *
     * @return last value of this stock.
     */
	public float getLastValue()
	{
		return this.lastValue;
	}

    /**
     * Method which sets the
     * @param stockSymbol
     */
	public void setStockSymbol(String stockSymbol)
	{
		this.stockSymbol = stockSymbol;
	}

    /**
     * Method which returns the stock symbol for this stock
     *
     * @return the stock symbol
     */
	public String getStockSymbol()
	{
		return this.stockSymbol;
	}

    /**
     * Method which sets the market
     *
     * @param newMarket
     */
	public void setMarket(String newMarket)
	{
		this.market = newMarket;
	}

    /**
     * Method which returns the market
     *
     * @return market name
     */
	public String getMarket()
	{
		return this.market;
	}

    /**
     * Method which returns a summary of this share.
     *
     * @return a string containing the stock symbol and it's last value.
     */
	public String getSummary()
	{
		return this.stockSymbol + ":  " + this.lastValue;
	}

    /**
     * Method which sets the value of the share when the market was closing.
     *
     * @param newClosingValue
     */
	public void setClosingValue(float newClosingValue)
	{
		this.closingValue = newClosingValue;
	}

    /**
     * Method which returns the value of this share when the market closed.
     *
     * @return the share value when the market closed.
     */
	public float getClosingValue()
	{
		return this.closingValue;
	}

    /**
     * Method which sets the historic volume of this stock.
     *
     * @param newVol
     */
	public void setVolumeHistoric(int newVol)
	{
		this.volumeHistoric = newVol;
	}

    /**
     * Method which returns the historic volume (how many shares have been traded in a specific period)
     *
     * @return the number of shares traded (historic)
     */
	public int getVolumeHistoric()
	{
		return this.volumeHistoric;
	}

    /**
     * Method which sets the current volume of this share
     *
     * @param newVol
     */
	public void setInstantVolume(int newVol)
	{
        this.volumeInstant = newVol;
	}

    /**
     * Method which returns the current volume of this share
     *
     * @return
     */
	public int getInstantVolume()
	{
		return this.volumeInstant;
	}

    /**
     * Method which returns the current value of this share collection
     *
     * @return
     */
	public float getTotalValue()
    {
		return this.totalValue;
	}

    /**
     * Method which sets the total value of the shares
     *
     * @param totalValue
     */
	public void setTotalValue(float totalValue)
    {
		this.totalValue = totalValue;
	}

    /**
     * Method which calculates the total value of this share collection
     *
     */
    public void calculateTotalValue()
    {
        this.totalValue = this.numberOfShares*this.lastValue;
    }

    /**
     * Method which tells if this stock is on a "run"
     *
     * @return whether this share is on a "run"
     */
	public boolean isRun()
    {
		return this.is_run;
	}

    /**
     * Method which tells if this stock is on a "rocket"
     *
     * @return whether this share is on a "rocket"
     */
	public boolean isRocket()
    {
		return this.is_rocket;
	}

    /**
     * Method which tells if this stock is on a "plummet"
     *
     * @return whether this share is on a "plummet"
     */
	public boolean isPlummet()
    {
		return this.is_plummet;
	}

    /**
     * Method which returns how many shares of this stock object we have.
     *
     * @return the number of shares
     */
    public int getNumberOfShares()
    {
        return this.numberOfShares;
    }

    /**
     * Method which sets the number of shares that are being owned.
     *
     * @param shareCount
     */
    public void setNumberOfShares(int shareCount)
    {
        this.numberOfShares = shareCount;
    }

    /**
     * Method which determines whether this stock object is on a run
     */
	public void calcRun()
    {
		this.getConstants();

		if (this.volumeHistoric != 0 && this.volumeInstant != 0)
        {
			if (this.volumeInstant > (this.RUN_CONST * this.volumeHistoric))
            {
                this.is_run = true;
			}
            else
            {
                this.is_run = false;
			}
		}
	}

    /**
     * Method which determines whether this stock object is plummeting
     */
	public void calcRocketPlummet()
    {
		this.getConstants();

        this.is_plummet = false;
        this.is_rocket  = false;

		if (this.lastValue != 0 && this.closingValue != 0)
        {
			if ( this.lastValue > (this.ROCKET_CONST * this.closingValue))
            {
                this.is_rocket = true;
			}
            else if (this.lastValue < (this.PLUMMET_CONST * this.closingValue))
            {
                this.is_plummet = true;
			}	
		}
	}

    /**
     * Method which sets the "rocket" coefficient
     *
     * @param newConst
     */
	public void setRocketConst(float newConst)
	{
		if(newConst > 0)
		{
			this.ROCKET_CONST = newConst;
		}
	}

    /**
     * Method which sets the "run" coefficient
     *
     * @param newConst
     */
	public void setRunConst(float newConst)
	{
		if(newConst > 0)
		{
			this.RUN_CONST = newConst;
		}
	}

    /**
     * Method which sets the "plummet" coefficient
     *
     * @param newConst
     */
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
    public int compare(Finance o1, Finance o2)
    {
        return o1.getStockSymbol().compareTo(o2.getStockSymbol());
    }
}
	
class ValueComparator implements Comparator<Finance> {

    @Override
    public int compare(Finance o1, Finance o2)
    {
        if(o1.getLastValue() > o2.getLastValue())
        {
            return -1;
        }
        else if(o1.getLastValue() == o2.getLastValue())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
}

class TotalComparator implements Comparator<Finance> {

    @Override
    public int compare(Finance o1, Finance o2)
    {
        if((o1.getTotalValue())> (o2.getTotalValue()))
        {
            return -1;
        }
        else if((o1.getTotalValue()) == (o2.getTotalValue()))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
}
