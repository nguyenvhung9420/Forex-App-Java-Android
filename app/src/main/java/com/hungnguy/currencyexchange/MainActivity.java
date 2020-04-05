package com.hungnguy.currencyexchange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    android.widget.Spinner sourceCurrenciesSpinner;
    android.widget.Spinner destCurrenciesSpinner;
    android.widget.EditText amount;
    android.widget.TextView result;
    android.widget.TextView salute;
    android.widget.Button fetchButton;

    RateRawData rateData;

    ArrayList<String> devises = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sourceCurrenciesSpinner = findViewById(R.id.currencies_spinner);
        destCurrenciesSpinner = findViewById(R.id.currencies_spinner_result);
        amount = findViewById(R.id.amount_input);
        result = findViewById(R.id.textResult);
        salute = findViewById(R.id.textView);
        fetchButton = findViewById(R.id.fetchButton);

        devises.add("SGD");
        devises.add("AUD");
        devises.add("USD");
        devises.add("EUR");

        fetchRates();
        sourceCurrenciesSpinner.setOnItemSelectedListener(new SpinnerActivity(){
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                updateUI(amount.getText().toString());
            }
        });
        destCurrenciesSpinner.setOnItemSelectedListener(new SpinnerActivity() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                updateUI(amount.getText().toString());
            }
        });

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchRates();
            }
        });

        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Below is example of getting USD:
                if (s.length() == 0) {
                    salute.setText("0.0");
                } else {
                    Log.d("The edit text is: ", s.toString());
                    updateUI(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // Updates the information shown on screen.
    private void updateUI(String amount) {
        if (amount.length() == 0) {
        } else {
            Log.d("The edit text is: ", amount.toString());
            Log.d("Upading UI: ", "At this point, the UI will be update");

            try {
                Double amountInput = Double.parseDouble(amount);
                String sourceDevise = sourceCurrenciesSpinner.getSelectedItem().toString();
                String destDevise = destCurrenciesSpinner.getSelectedItem().toString();
                Double result = rateData.calcExchange(sourceDevise, amountInput, destDevise);
                salute.setText(result.toString());
            }
            catch(Exception e) {
                System.out.println(e.toString());
            }

        }
    }

    public void refreshSpinnerAdapter(RateRawData rateData){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rateData.getDeviseList());

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter and event listener to the spinner
        sourceCurrenciesSpinner.setAdapter(adapter);
        destCurrenciesSpinner.setAdapter(adapter);
        sourceCurrenciesSpinner.setSelection(rateData.getIndexOfBase());
        destCurrenciesSpinner.setSelection(rateData.getIndexOfUSD());
    }

    public void fetchRates() {
        String WEATHER_URL = "https://api.exchangeratesapi.io/latest?base=SGD";

        // AsyncHttpClient belongs to the loopj dependency.
        AsyncHttpClient client = new AsyncHttpClient();

        // Making an HTTP GET request by providing a URL and the parameters.
        client.get(WEATHER_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("Fetching API: ", "Success! JSON: " + response.toString());
                rateData = RateRawData.fromJson(response);
                devises = rateData.getDeviseList();
                refreshSpinnerAdapter(rateData);
                Log.d("devises in fetching ", devises.toString());
                salute.setText(rateData.getDate());
                amount.setVisibility(View.VISIBLE);
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

class SpinnerActivity extends Activity implements OnItemSelectedListener {
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        Log.d("Currency", parent.getItemAtPosition(pos).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}

