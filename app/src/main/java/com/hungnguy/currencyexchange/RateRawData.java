package com.hungnguy.currencyexchange;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Double;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RateRawData {
    // Member variables that hold our relevant weather inforomation.
    private String mDate;
    private String mBase;
    private Hashtable<String, Double> mRateDict = new Hashtable<String, Double>();
    private ArrayList<String> devises = new ArrayList<String>();
    private int indexOfBase;
    private  int indexOfUSD;


    // Create a WeatherDataModel from a JSON.
    // We will call this instead of the standard constructor.
    //@RequiresApi(api = Build.VERSION_CODES.N)
    public static RateRawData fromJson(JSONObject jsonObject) {

        // JSON parsing is risky business. Need to surround the parsing code with a try-catch block.
        try {
            RateRawData rateRawData = new RateRawData();

            rateRawData.mDate = jsonObject.getString("date");
            rateRawData.mBase = jsonObject.getString("base");

            JSONObject objects = new JSONObject();
            objects = jsonObject.getJSONObject("rates");

            JSONArray key = objects.names();

            for (int i = 0; i < key.length(); ++i) {
                String devise = key.getString(i);
                Double value = Double.parseDouble(objects.getString(devise));
                rateRawData.mRateDict.put(devise, value);
                rateRawData.devises.add(devise);
                if (value == 1.0) {
                    rateRawData.indexOfBase = i;
                }
                if (devise == "USD"){
                    rateRawData.indexOfUSD = i;
                }
            }
            Log.d("Devises array: ", rateRawData.devises.toString());
            return rateRawData;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getDeviseList() {
        Log.d("Devises in function ", devises.toString());
        return devises;
    }

    // Get the weather image name from OpenWeatherMap's condition (marked by a number code)
    public Double getRate(String devise) {
        return mRateDict.get(devise);
    }

    public Double calcExchange(String sourceDevise, Double sourceAmount, String destDevise) {
        return (sourceAmount * getRate(sourceDevise) * getRate(destDevise));
    }

    public String getDate() {
        return mDate;
    }

    public String getBase() {
        return mBase;
    }

    public int getIndexOfBase() {
        return indexOfBase;
    }

    public int getIndexOfUSD() {
        return indexOfUSD;
    }
}
