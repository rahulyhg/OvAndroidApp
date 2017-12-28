package ovenues.com.ovenue;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ovenues.com.ovenue.utils.ConnectionDetector;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class Signup extends AppCompatActivity {

    EditText et_fullname,et_email,et_contactno,et_password,et_cnf_password;
    TextView tv_signup,tv_loginpage;
    CheckBox cb_terms;

    String name,email,before_attherate,
            after_attherate,str_contactno,password,cnf_password,res;

    Boolean isInternetPresent = false;
    SharedPreferences sharepref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        et_fullname = (EditText)this.findViewById(R.id.et_fullname);
        et_email = (EditText)this.findViewById(R.id.et_email);
        et_contactno = (EditText)this.findViewById(R.id.et_contactno);
        et_password = (EditText)this.findViewById(R.id.et_password);
        et_cnf_password = (EditText)this.findViewById(R.id.et_cnf_password);

        cb_terms=(CheckBox)this.findViewById(R.id.cb_terms);

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        et_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()==0 && charSequence.toString().contains(" ")){
                    charSequence.toString().replaceAll(" ", "");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()!=0 && s.toString().contains(" ")){
                    s.toString().replaceAll(" ", "");
                }
                //Log.d("less than 4","less then 4");

                if(s.toString().length()<5) {
                    String result = s.toString().replaceAll(" ", "");
                    if (!s.toString().equals(result)) {
                        et_fullname.setText(result);
                        et_fullname.setSelection(result.length());
                        // alert the user
                        et_fullname.setError("First 4 character not be Null");
                    }
                }
            }
        });





        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();


        tv_signup = (TextView)this.findViewById(R.id.tv_signup);
        tv_loginpage  = (TextView)this.findViewById(R.id.tv_loginpage);
        tv_loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cb_terms.isChecked()==true){

                    if(et_email.getText().toString().isEmpty()) {
                        et_email.setError("Enter Email Address!");
                    } /*else if(!et_email.getText().toString().isEmpty()
                            && et_email.getText().toString().contains("\u0040")) {

                        before_attherate = et_email.getText().toString();
                        before_attherate = before_attherate.substring(0, before_attherate.indexOf("\u0040"));

                        after_attherate = et_email.getText().toString();
                        after_attherate = after_attherate.substring(after_attherate.indexOf("\u0040") + 1, after_attherate.indexOf("."));

                         if (et_email.getText().toString().length() == 0 || !et_email.getText().toString().matches(emailPattern)
                                || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")
                                || et_email.getText().toString().equalsIgnoreCase("abc@abc.com")
                                || before_attherate.equalsIgnoreCase(after_attherate)) {
                            et_email.setError("Enter Valid Email Address!");
                        } else if (before_attherate.length() <= 4 || before_attherate.equalsIgnoreCase("12345")
                                || before_attherate.equalsIgnoreCase("12345456789")
                                || before_attherate.equalsIgnoreCase("1234567890") ||
                                before_attherate.equalsIgnoreCase("abcde") ||
                                before_attherate.equalsIgnoreCase("abcdef") ||
                                before_attherate.equalsIgnoreCase("qwert") ||
                                before_attherate.equalsIgnoreCase("qwerty") ||
                                before_attherate.equalsIgnoreCase("asdfg") ||
                                before_attherate.equalsIgnoreCase("asdfgh")) {
                            et_email.setError("Enter Valid Email Address!");
                        } else if (after_attherate.length() <= 4 || after_attherate.equalsIgnoreCase("12345")
                                || after_attherate.equalsIgnoreCase("12345456789")
                                || after_attherate.equalsIgnoreCase("1234567890") ||
                                after_attherate.equalsIgnoreCase("abcde") ||
                                after_attherate.equalsIgnoreCase("abcdef") ||
                                after_attherate.equalsIgnoreCase("qwert") ||
                                after_attherate.equalsIgnoreCase("qwerty") ||
                                after_attherate.equalsIgnoreCase("asdfg") ||
                                after_attherate.equalsIgnoreCase("asdfgh") ||
                                after_attherate.equalsIgnoreCase("email")) {
                            et_email.setError("Enter Valid Email Address!");
                        }
                    }*/else if (et_fullname.getText().toString().equalsIgnoreCase("")) {

                            et_fullname.setError("Enter Name");
                        } else if (et_fullname.getText().length() > 4
                                && et_fullname.getText().toString().substring(0, 1).contains(" ")) {

                            String estring = "Space not allowed";
                           /* ForegroundColorSpan fgcspan = new ForegroundColorSpan(getResources().getColor(R.color.white));
                            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(estring);
                            ssbuilder.setSpan(fgcspan, 0, estring.length(), 0);
                            et_fullname.requestFocus();*/

                            et_fullname.setError(estring);
                        } else if (et_contactno.getText().toString().length() == 0
                                || et_contactno.getText().toString().length() < 10
                                || et_contactno.getText().toString().equalsIgnoreCase("1234567890")) {
                            et_contactno.setError("Enter Correct Contact Number!");
                        } else if (et_password.getText().toString().length() < 8 || et_password.getText().toString().length() > 16
                                || et_password.getText().toString().matches("[0-9]")
                                || et_password.getText().toString().equalsIgnoreCase("123456")) {
                            et_password.setError("Password must be 8 to 16 character !");
                        }/* else if (et_password.getText().toString().contains("#")) {
                        et_password.setError("Please dont use #,% in your password.!");
                    }*/ else if (et_cnf_password.getText().toString().length() == 0
                                || !et_password.getText().toString().equals(et_cnf_password.getText().toString())) {
                            et_cnf_password.setError("Password Not matched!!!");
                        }
                        else {
                            name = et_fullname.getText().toString();
                            email = et_email.getText().toString();
                            str_contactno = et_contactno.getText().toString();
                            password = et_password.getText().toString();
                            if (isInternetPresent) {
                                // Internet Connection is Present
                                // make HTTP requests
                                new SignupScreen_async().execute();

                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);
                                // Changing message text color
                                snackbar.setActionTextColor(Color.BLUE);

                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                                Toast.makeText(Signup.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();
                            }
                        }
                }else{
                    Toast.makeText(Signup.this, "Accept Terms and Conditions.", Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    class SignupScreen_async extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Signup.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("user_name",name);
                req.put("email_id",email);
                req.put("password",password);
                req.put("contact_num",str_contactno);
                req.put("$resolved","true");

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"signup_request", req.toString(),"put");//post("http://54.153.127.215/api/login", req.toString());
                Log.d("REsponce Json====",response);
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
            System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(Signup.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res);
                Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi



                if(response_string.equals("success")){

                    String user_obj=obj.getString("message");

                    JSONObject obj_user_details = new JSONObject(user_obj);
                   /* String user_name=obj_user_details.getString("user_name");
                    String email_id=obj_user_details.getString("email_id");
                    String contact_num=obj_user_details.getString("contact_num");*/
                    String user_id=obj_user_details.getString("user_id");

                    /*sharepref.edit().putString(Const.PREF_USER_ID,user_id).commit();
                    sharepref.edit().putString(Const.PREF_EMAIL,email).commit();
                    //sharepref.edit().putString(Const.PREF_FACEBOOK_ID,facebook_id).commit();
                    sharepref.edit().putString(Const.PREF_MOBILE_NO,str_contactno).commit();
                    sharepref.edit().putString(Const.PREF_FULL_NAME,name).commit();
                    //sharepref.edit().putString(Const.PREF_PROFILE_PIC_URL,profile_pic).commit();
                    //sharepref.edit().putString(Const.PREF_PASSWORD,et_password.getText().toString()).commit();
                    sharepref.edit().putString(Const.PREF_LOGINKEY,"yes").commit();*/


                   /* Intent login_pg = new Intent(Signup.this,MainNavigationScreen.class);
                    startActivity(login_pg);
                    finish();*/

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Signup.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Registered !");
                    builder.setMessage("Verification link sent on mail , please check mail and verify to Login.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                              Intent login_pg = new Intent(Signup.this,Login.class);
                    startActivity(login_pg);
                    overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
                    finish();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();

                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Successfully Login!", Snackbar.LENGTH_LONG)
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

                else{
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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Signup.this,Login.class));
        finish();
    }
}
