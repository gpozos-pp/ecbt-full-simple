package com.appify.ecbt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.braintreepayments.api.models.PayPalRequest.USER_ACTION_COMMIT;

public class CheckoutActivity extends AppCompatActivity implements PaymentMethodNonceCreatedListener,
        BraintreeCancelListener, BraintreeErrorListener {

    public static final String CART = "cart";
    public static final String ITEMS = "items";

    int amount = 0;

    EditText etFirstName;
    EditText etLastname;
    EditText etCompany;
    EditText etStreet;
    EditText etExtendedAddress;
    EditText etLocality;
    EditText etRegion;
    EditText etPostalCode;

    private ProgressDialog pDialog;

    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        thisActivity = this;

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        SharedPreferences cart = getApplicationContext().getSharedPreferences(CART, 0);
        String items = cart.getString(ITEMS, "[]");
        Log.d("***", items);

        try {

            JSONArray itemsArray = new JSONArray(items);

            for (int i = 0; i < itemsArray.length(); i++) {
                amount += Integer.parseInt(itemsArray.getJSONObject(i).getString("price"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        getSupportActionBar().setTitle("Por pagar: $" + amount);

        etFirstName = findViewById(R.id.et_first_name);
        etLastname = findViewById(R.id.et_lastname);
        etCompany = findViewById(R.id.et_company);
        etStreet = findViewById(R.id.et_street);
        etExtendedAddress = findViewById(R.id.et_extended_address);
        etLocality = findViewById(R.id.et_locality);
        etRegion = findViewById(R.id.et_region);
        etPostalCode = findViewById(R.id.et_postal_code);

        Button btnPayPp = findViewById(R.id.btn_pay_pp);
        btnPayPp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount == 0) {
                    Toast.makeText(getApplicationContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etFirstName.getText().length() == 0 || etLastname.getText().length() == 0 || etCompany.getText().length() == 0 || etStreet.getText().length() == 0 || etExtendedAddress.getText().length() == 0 || etLocality.getText().length() == 0 || etRegion.getText().length() == 0 || etPostalCode.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                pDialog.setMessage("Cargando...");
                showDialog();

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://tiendaejemplomx.com/gpozos/ecbt/client-token.php", new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String clientToken) {

                        try {

                            BraintreeFragment braintreeFragment = BraintreeFragment.newInstance(thisActivity, clientToken);

                            PayPalRequest request = new PayPalRequest(String.valueOf(amount))
                                    .currencyCode("MXN")
                                    .landingPageType(PayPalRequest.LANDING_PAGE_TYPE_LOGIN)
                                    .intent(PayPalRequest.INTENT_SALE);
                            PayPal.requestOneTimePayment(braintreeFragment, request);

                        } catch (InvalidArgumentException e) {
                            Toast.makeText(getApplicationContext(), "There was an issue with your authorization string", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("***", "onFailure - client-token");
                    }

                });
            }
        });

        Button btnPayCc = findViewById(R.id.btn_pay_cc);
        btnPayCc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount == 0) {
                    Toast.makeText(getApplicationContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etFirstName.getText().length() == 0 || etLastname.getText().length() == 0 || etCompany.getText().length() == 0 || etStreet.getText().length() == 0 || etExtendedAddress.getText().length() == 0 || etLocality.getText().length() == 0 || etRegion.getText().length() == 0 || etPostalCode.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                pDialog.setMessage("Cargando...");
                showDialog();

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("http://tiendaejemplomx.com/gpozos/ecbt/client-token.php", new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String clientToken) {

                        try {

                            BraintreeFragment braintreeFragment = BraintreeFragment.newInstance(thisActivity, clientToken);

                            PayPalRequest request = new PayPalRequest(String.valueOf(amount))
                                    .currencyCode("MXN")
                                    .userAction(USER_ACTION_COMMIT)
                                    .landingPageType(PayPalRequest.LANDING_PAGE_TYPE_BILLING)
                                    .intent(PayPalRequest.INTENT_SALE);
                            PayPal.requestOneTimePayment(braintreeFragment, request);

                        } catch (InvalidArgumentException e) {
                            Toast.makeText(getApplicationContext(), "There was an issue with your authorization string", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("***", "onFailure - client-token");
                    }

                });
            }
        });

    }

    public void postNonceToServer(String nonce) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("payment_method_nonce", nonce);
        params.put("amount", amount);
        params.put("first_name", etFirstName.getText());
        params.put("lastname", etLastname.getText());
        params.put("company", etCompany.getText());
        params.put("street", etStreet.getText());
        params.put("extended_address", etExtendedAddress.getText());
        params.put("locality", etLocality.getText());
        params.put("region", etRegion.getText());
        params.put("postal_code", etPostalCode.getText());
        params.put("description", "Compra desde la app de android");
        client.post("http://tiendaejemplomx.com/gpozos/ecbt/checkout.php", params, new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("***", "checkout response: " + responseString);

                        SharedPreferences cart = getApplicationContext().getSharedPreferences(CART, 0);
                        SharedPreferences.Editor editor = cart.edit();
                        editor.remove(ITEMS);
                        editor.apply();

                        AlertDialog alertDialog = new AlertDialog.Builder(CheckoutActivity.this).create();
                        alertDialog.setTitle("Pago completado");
                        alertDialog.setMessage(responseString);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        CheckoutActivity.this.finish();
                                    }
                                });
                        alertDialog.show();

                        hideDialog();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("***", "onFailure - checkout");
                    }

                }
        );
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        postNonceToServer(paymentMethodNonce.getNonce());
    }

    @Override
    public void onCancel(int requestCode) {
        hideDialog();
        // Toast.makeText(getApplicationContext(),"onCancel",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Exception error) {

    }
}
