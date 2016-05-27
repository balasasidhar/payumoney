package com.sasidhar.smaps.payu_money;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.sasidhar.smaps.payumoney.MakePaymentActivity;
import com.sasidhar.smaps.payumoney.PayUMoney_Constants;
import com.sasidhar.smaps.payumoney.Utils;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private HashMap<String, String> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        Intent intent = new Intent(this, MakePaymentActivity.class);
        intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_DEV);
        intent.putExtra(PayUMoney_Constants.PARAMS, params);

        startActivityForResult(intent, PayUMoney_Constants.PAYMENT_REQUEST);
    }

    private synchronized void init() {

        params.put(PayUMoney_Constants.KEY, "merchant_key>");
        params.put(PayUMoney_Constants.TXN_ID, "transaction_it");
        params.put(PayUMoney_Constants.AMOUNT, "amount");
        params.put(PayUMoney_Constants.PRODUCT_INFO, "product_info");
        params.put(PayUMoney_Constants.FIRST_NAME, "first_name");
        params.put(PayUMoney_Constants.EMAIL, "email");
        params.put(PayUMoney_Constants.PHONE, "phone_number");
        params.put(PayUMoney_Constants.SURL, "success_url");
        params.put(PayUMoney_Constants.FURL, "failure_url");
        params.put(PayUMoney_Constants.UDF1, "");
        params.put(PayUMoney_Constants.UDF2, "");
        params.put(PayUMoney_Constants.UDF3, "");
        params.put(PayUMoney_Constants.UDF4, "");
        params.put(PayUMoney_Constants.UDF5, "");

        String hash = Utils.generateHash(params, "salt");

        params.put(PayUMoney_Constants.HASH, hash);
        params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayUMoney_Constants.PAYMENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Payment Success,", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Payment Failed | Cancelled.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
