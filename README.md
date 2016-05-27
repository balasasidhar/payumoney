# payumoney
<h5> PayU Money Android SDK </h5>

<p> A simple & Easiest way to integrate PayU Biz Payment Gateway with your Android Application </p>

<h5>Step by step guide to integrate PayU Money SDK with your Android Application </h5>

<ol>

<li> Add Maven repository and compile dependency in <b> build.gradle </b> file.
<pre>
android {
  repositories {
    maven {
        url  "https://dl.bintray.com/sasidhar-678/maven"
    }
  }
}

dependencies {
    compile 'com.sasidhar.smaps.payumoney:payumoney:0.0.2'
}
</pre>
</li>

<li> Add Internt Permissions in your Android Application Manifest file
<pre>
    &lt;uses-permission android:name="android.permission.INTERNET" /&gt;
</pre>
</li>

<li> Create an object for <b> HashMap </b> and obtain all the required parameters
<pre>
HashMap<String, String> params = new HashMap<>();

params.put(PayUMoney_Constants.KEY, "merchant_key>"); // Get merchant key from PayU Money Account
params.put(PayUMoney_Constants.TXN_ID, "transaction_id");
params.put(PayUMoney_Constants.AMOUNT, "amount");
params.put(PayUMoney_Constants.PRODUCT_INFO, "product_info");
params.put(PayUMoney_Constants.FIRST_NAME, "first_name");
params.put(PayUMoney_Constants.EMAIL, "email");
params.put(PayUMoney_Constants.PHONE, "phone_number");
params.put(PayUMoney_Constants.SURL, "success_url");
params.put(PayUMoney_Constants.FURL, "failure_url");

// User defined fields are optional (pass empty string)
params.put(PayUMoney_Constants.UDF1, "");
params.put(PayUMoney_Constants.UDF2, "");
params.put(PayUMoney_Constants.UDF3, "");
params.put(PayUMoney_Constants.UDF4, "");
params.put(PayUMoney_Constants.UDF5, "");

// generate hash by passing params and salt 
String hash = Utils.generateHash(params,"salt"); // Get Salt from PayU Money Account 
params.put(PayUMoney_Constants.HASH, hash);

// SERVICE PROVIDER VALUE IS ALWAYS "payu_paisa".
params.put(PayUMoney_Constants.SERVICE_PROVIDER, "payu_paisa");
</pre>
</li>

<li> Create an Intent to a <b> MakePaymentActivity </b> and pass ENVIRONMENT (DEV/PRODUCATION) and PaymentParams objects that we are create in the above steps as intent extras.
<pre>
Intent intent = new Intent(this, MakePaymentActivity.class);
intent.putExtra(PayUMoney_Constants.ENVIRONMENT, PayUMoney_Constants.ENV_DEV);
intent.putExtra(PayUMoney_Constants.PARAMS, params);

</pre>
</li>

<li> Start Activity for Result
<pre>
startActivityForResult(intent, 01);
</pre>
</li>

<li> Handle response at onActivityResult
<pre>
if (requestCode == 01) {
    switch (resultCode) {
        case RESULT_OK:
            Toast.makeText(MainActivity.this, "Payment Success.", Toast.LENGTH_SHORT).show();
            break;

        case RESULT_CANCELED:
            Toast.makeText(MainActivity.this, "Payment Cancelled | Failed.", Toast.LENGTH_SHORT).show();
            break;
    }
}
</pre>
</li>
