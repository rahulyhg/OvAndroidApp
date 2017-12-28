package ovenues.com.ovenue.CreditCard;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import movile.com.creditcardguide.ActionOnPayListener;
import movile.com.creditcardguide.CreditCardFragment;
import movile.com.creditcardguide.model.CreditCardPaymentMethod;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PaymentMethod;
import movile.com.creditcardguide.model.PurchaseOption;
import ovenues.com.ovenue.FinalBookingConfirmation;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class CreditCardFragmentActivity extends AppCompatActivity implements ActionOnPayListener {

    private CreditCardFragment inputCardFragment;
    SharedPreferences sharepref;
    JSONObject bookingJSONObj;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    public String coupon_Obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_fragment);

        android.support.v7.app.AlertDialog.Builder builder =
                new android.support.v7.app.AlertDialog.Builder(CreditCardFragmentActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Please Note !");
        builder.setMessage("We are not charging your credit card at this time. \n" +
                "Your card will be charged when the vendor confirms the order.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //builder.setNegativeButton("Cancel", null);
        builder.show();

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        getSupportActionBar().setTitle("Credit Card Details");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String billAmount = getIntent().getStringExtra("billAmount");

        inputCardFragment = (CreditCardFragment) getFragmentManager().findFragmentById(R.id.frg_pay);
        inputCardFragment.setPagesOrder(CreditCardFragment.Step.FLAG, CreditCardFragment.Step.NUMBER,
                CreditCardFragment.Step.EXPIRE_DATE, CreditCardFragment.Step.CVV, CreditCardFragment.Step.NAME);

        inputCardFragment.setListPurchaseOptions(getList(), Double.parseDouble(billAmount));

        bookingJSONObj = new JSONObject();




    }

    private List<PurchaseOption> getList() {
        List<PurchaseOption> list = new ArrayList<>();
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.MASTERCARD, 1));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.VISACREDITO, 1));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.DISCOVER, 1));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AMEX, 1));
       // list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.PAYPAL, 1));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.DINERS, 1));
        //list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.NUBANK, 1));
        //list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AURA, 1));
        //list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.ELO, 1));
        //list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.HIPERCARD, 1));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.OTHER, 1));

        return list;
    }

    @Override
    public void onChangedPage(CreditCardFragment.Step page) {

    }

    @Override
    public void onComplete(CreditCardPaymentMethod purchaseOption, boolean saveCard) {

        //{"user_id":38,"":"","":{"":"MasterCard","":"12345678","":"vani","":"1023","":"322","":"2099, Gateway Place","city":"368","":"California","":"95110"}}

        if(purchaseOption.getIssuerCode()==null ||
        purchaseOption.getCreditCardNumber().length()<1 ||
        purchaseOption.getCreditCardName().length()<1 ||
        purchaseOption.getExpireMonth()==null  ||
        purchaseOption.getExpireYear()==null  ||
        purchaseOption.getSecurityCode().length()<1 ||
        purchaseOption.getAddress().length()<1 ||
        purchaseOption.getState().length()<1 ||
        purchaseOption.getCity().length()<1 ||
        purchaseOption.getZip().length()<1){

            Toast.makeText(CreditCardFragmentActivity.this,"Enter All Billing Details.",Toast.LENGTH_LONG).show();
        }else {

            try {
                bookingJSONObj.put("card_type", purchaseOption.getIssuerCode().toString());
                bookingJSONObj.put("credit_card_num", purchaseOption.getCreditCardNumber().toString());
                bookingJSONObj.put("name_on_card", purchaseOption.getCreditCardName().toString());
                bookingJSONObj.put("card_exp_date", purchaseOption.getExpireMonth().toString() + purchaseOption.getExpireYear());
                bookingJSONObj.put("cvv_num", purchaseOption.getSecurityCode().toString());

                bookingJSONObj.put("billing_address1", purchaseOption.getAddress().toString());
                bookingJSONObj.put("state", purchaseOption.getState().toString());
                bookingJSONObj.put("city", purchaseOption.getCity().toString());
                bookingJSONObj.put("zipcode", Integer.parseInt(purchaseOption.getZip().toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        /*try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }*/

            new FinalOrderBookingAPI().execute();
            //Toast.makeText(this, purchaseOption.toString(), Toast.LENGTH_LONG).show();

            if (saveCard) {
                // TODO: Persist the card
                // WARNING: It's not recommended persist the security code CVV, please remove it before persist, calling:
                purchaseOption.setSecurityCode(null);
            }
        }
    }

    String res;
    private class FinalOrderBookingAPI extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(CreditCardFragmentActivity.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {



                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,null));
                req.put("booking_source","Mobile");

                req.put("booking_payment_details",bookingJSONObj);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"booking_all", req.toString(),"put");
                res = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res;

        }
        @Override
        protected void onPostExecute(String result)
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            //System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(CreditCardFragmentActivity.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res);
                Log.i("RESPONSE pymnt", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    /*String user_obj=obj.getString("message");

                    android.support.v7.app.AlertDialog.Builder builder =
                            new android.support.v7.app.AlertDialog.Builder(CreditCardFragmentActivity.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Sent !");
                    builder.setMessage("Thank you for making order request online with ovenues.com. Your request has been sent to the vendor and we will mail you the Confirmation soon.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(CreditCardFragmentActivity.this, MainNavigationScreen.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();*/

                    startActivity( new Intent(CreditCardFragmentActivity.this, FinalBookingConfirmation.class).
                            putExtra("JSON",res));
                    finish();

                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected void onCancelled() {
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        inputCardFragment.backPressed();
    }
}
