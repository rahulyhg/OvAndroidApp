package ovenues.com.ovenue;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import ovenues.com.ovenue.utils.ConnectionDetector;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class Login extends AppCompatActivity {


    private RadioGroup radioTypeGroup;
    private RadioButton radioTypeButton;
    TextView tv_signin,tv_signup,tv_forget_pwd,tv_guestLogin;
    EditText et_email,et_password;

    Boolean isInternetPresent = false;
    String email_frg_pwd="";


    String res;
    String username,email,password;

    SharedPreferences sharepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        radioTypeGroup=(RadioGroup)findViewById(R.id.radioGroup);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sharepref.edit().putString(Const.PREF_USER_TOKEN,refreshedToken).apply();
        //Log.d("firebase token===",sharepref.getString(Const.PREF_USER_TOKEN,""));

        // Set up the login form.
        et_email = (EditText)this.findViewById(R.id.et_email);
        et_password = (EditText)this.findViewById(R.id.et_password);
        //populateAutoComplete();

        tv_guestLogin=(TextView)this.findViewById(R.id.tv_guestLogin);
        tv_guestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharepref.edit().putString(Const.PREF_USER_ID,"0").apply();
                sharepref.edit().putString(Const.PREF_USER_EMAIL,"guest").apply();
                //sharepref.edit().putString(Const.PREF_FACEBOOK_ID,facebook_id).commit();
                sharepref.edit().putString(Const.PREF_USER_MOBILE_NO,"0").apply();
                sharepref.edit().putString(Const.PREF_USER_FULL_NAME,"Guest").apply();
                //sharepref.edit().putString(Const.PREF_PROFILE_PIC_URL,profile_pic).commit();
                //sharepref.edit().putString(Const.PREF_PASSWORD,et_password.getText().toString()).commit();
                sharepref.edit().putString(Const.PREF_LOGINKEY,"yes").apply();


                Intent login_pg = new Intent(Login.this,MainNavigationScreen.class);
                startActivity(login_pg);
                finish();
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "Login as Guest !", Snackbar.LENGTH_LONG)
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
        });



        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // creating connection detector class instance
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        // get Internet status
        isInternetPresent = cd.isConnectingToInternet();



        tv_forget_pwd= (TextView)this.findViewById(R.id.tv_forget_pwd);
        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Login.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_dialog_forget_pwd);

                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = 900;
                lp.height = 900;
                lp.gravity = Gravity.CENTER;
                lp.dimAmount = 0;
                dialog.getWindow().setAttributes(lp);

                final EditText et_frg_pwd =(EditText)dialog.findViewById(R.id.frg_et_email);
                TextView btn_submit = (TextView) dialog.findViewById(R.id.frg_tv_submit);


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        email_frg_pwd=et_frg_pwd.getText().toString();

                        if(et_frg_pwd.getText().toString().length()==0 || !et_frg_pwd.getText().toString().matches(emailPattern)
                                || et_frg_pwd.getText().toString().matches("[0-9]+@[0-9]+@[0-9]") || et_frg_pwd.getText().toString().equalsIgnoreCase("abc@abc.com"))
                        {
                            et_frg_pwd.setError("Enter Valid Email Address.");

                        }else{
                            email_frg_pwd=et_frg_pwd.getText().toString();
                            new GetPassword().execute();
                            dialog.dismiss();
                        }

                    }
                });

                dialog.show();
            }
        });


        tv_signin=(TextView)this.findViewById(R.id.tv_signin);
        tv_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    // Internet Connection is Present
                    // make HTTP requests


                    if(et_email.getText().toString().length()==0 || !et_email.getText().toString().matches(emailPattern)
                            || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")
                            || et_email.getText().toString().equalsIgnoreCase("abc@abc.com")){
                        et_email.setError("Enter Valid Email Address!");
                    }else if(et_password.getText().toString().length()<6){
                        et_password.setError("Password must be 6 digit or more!");
                    }else {

                        email=et_email.getText().toString();
                        password=et_password.getText().toString();

                        int selectedId=radioTypeGroup.getCheckedRadioButtonId();
                        radioTypeButton=(RadioButton)findViewById(selectedId);
                        //Toast.makeText(Login.this,radioTypeButton.getText(),Toast.LENGTH_SHORT).show();

                        new UserLoginTask().execute();

                    }
                }else{

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), " Sorry! No Internet!!!", Snackbar.LENGTH_LONG);

                    // Changing message text color
                    snackbar.setActionTextColor(Color.BLUE);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();

                    Toast.makeText(Login.this, "  No Internet Connection!!!.  ", Toast.LENGTH_LONG).show();


                }
            }
        });


        tv_signup=(TextView)this.findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,
                        Signup.class));
                //finish();
            }
        });
    }

    public class UserLoginTask extends AsyncTask<Object, Void, String> {


        protected ProgressDialog progressDialog;
        String response_string;

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Login.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }


        @Override
        protected String doInBackground(Object... parametros) {
            // TODO: attempt authentication against a network service.
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("email_id",email);
                req.put("password",password);
                req.put("token",sharepref.getString(Const.PREF_USER_TOKEN,""));
                req.put("device_id",sharepref.getString(Const.PREF_USER_TOKEN,""));
                req.put("status","1");
                Log.d("REq Json======", req.toString());
                String response = post(Const.SERVER_URL_API +"login", req.toString(),"post");//mob_login_post;
                Log.d("REsponce Json====",response);
                res = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                super.onPostExecute(result);
            }
            //System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();
            //Log.i("RESPONSE", res);

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(Login.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res);
                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi



                if(response_string.equals("success")){

                    String user_obj=obj.getString("message");

                    JSONObject obj_user_details = new JSONObject(user_obj);
                    String user_name=obj_user_details.getString("user_name");
                    String email_id=obj_user_details.getString("email_id");
                    String contact_num=obj_user_details.getString("contact_num");
                    String user_id=obj_user_details.getString("user_id");



                    sharepref.edit().putString(Const.PREF_USER_ID,user_id).commit();
                    sharepref.edit().putString(Const.PREF_USER_EMAIL,email_id).commit();
                    //sharepref.edit().putString(Const.PREF_FACEBOOK_ID,facebook_id).commit();
                    sharepref.edit().putString(Const.PREF_USER_MOBILE_NO,contact_num).commit();
                    sharepref.edit().putString(Const.PREF_USER_FULL_NAME,user_name).commit();
                    //sharepref.edit().putString(Const.PREF_PROFILE_PIC_URL,profile_pic).commit();
                    //sharepref.edit().putString(Const.PREF_PASSWORD,et_password.getText().toString()).commit();
                    sharepref.edit().putString(Const.PREF_LOGINKEY,"yes").commit();


                    Intent login_pg = new Intent(Login.this,MainNavigationScreen.class);
                    startActivity(login_pg);
                    finish();
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



    public class GetPassword extends AsyncTask<Object, Void, String> {


        protected ProgressDialog progressDialog;
        String response_string;

        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(Login.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }


        @Override
        protected String doInBackground(Object... parametros) {
            // TODO: attempt authentication against a network service.
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("email_id",email_frg_pwd);
                req.put("role","host");
                Log.d("REq Json======", req.toString());
                String response = post(Const.SERVER_URL_API +"forgot_password", req.toString(),"post");//mob_login_post;
                 Log.d("REsponce Json====",response);
                res = response;
            }catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                super.onPostExecute(result);
            }
            //System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();
            //Log.i("RESPONSE", res);

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(Login.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res);
                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi



                if(response_string.equals("success")){

                    String message=obj.getString("message");

                    /*JSONObject obj_user_details = new JSONObject(user_obj);
                    String user_name=obj_user_details.getString("user_name");
                    String email_id=obj_user_details.getString("email_id");
                    String contact_num=obj_user_details.getString("contact_num");
                    String user_id=obj_user_details.getString("user_id");



                    sharepref.edit().putString(Const.PREF_USER_ID,user_id).commit();
                    sharepref.edit().putString(Const.PREF_USER_EMAIL,email_id).commit();
                    //sharepref.edit().putString(Const.PREF_FACEBOOK_ID,facebook_id).commit();
                    sharepref.edit().putString(Const.PREF_USER_MOBILE_NO,contact_num).commit();
                    sharepref.edit().putString(Const.PREF_USER_FULL_NAME,user_name).commit();
                    //sharepref.edit().putString(Const.PREF_PROFILE_PIC_URL,profile_pic).commit();
                    //sharepref.edit().putString(Const.PREF_PASSWORD,et_password.getText().toString()).commit();
                    sharepref.edit().putString(Const.PREF_LOGINKEY,"yes").commit();*/

                    Toast.makeText(Login.this,"Mail sent to your e-mail address.",Toast.LENGTH_LONG).show();

                    Intent login_pg = new Intent(Login.this,Login.class);
                    startActivity(login_pg);
                    finish();
                    Snackbar snackbar = Snackbar
                            .make(findViewById(android.R.id.content), "Mail sent to registered email address.", Snackbar.LENGTH_LONG)
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

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
}
