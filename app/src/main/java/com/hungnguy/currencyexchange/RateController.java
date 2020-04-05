package com.hungnguy.currencyexchange;


//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RateController  {

    public static void fetchRates() {

        String WEATHER_URL = "https://api.exchangeratesapi.io/latest?base=SGD";

        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // Making an HTTP GET request by providing a URL and the parameters.
        client.get(WEATHER_URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d("Fetching API: ", "Success! JSON: " + response.toString());
                RateRawData rateData = RateRawData.fromJson(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.e("Fetching API: ", "Fail " + e.toString());
                Log.d("Fetching API: ", "Status code " + statusCode);
                Log.d("Fetching API: ", "Why failed? " + response.toString());
            }

        });
    }


}
