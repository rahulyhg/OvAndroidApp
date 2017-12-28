package ovenues.com.ovenue;

import android.app.Dialog;
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
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ovenues.com.ovenue.bookingdetails.BookingListActivity;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class EditProfile extends AppCompatActivity {

    TextView tv_change_pwd;
    EditText et_username,et_email ,et_contact_number;
    String strold_password, strnew_password,res ;
    SharedPreferences sharepref;
    Button btn_bookings,btn_cancel_ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        et_username =(EditText)this.findViewById(R.id.et_username);
        et_email =(EditText)this.findViewById(R.id.et_email);
        et_contact_number =(EditText)this.findViewById(R.id.et_contact_number);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        et_username.setText(sharepref.getString(Const.PREF_USER_FULL_NAME ,""));
        et_email.setText(sharepref.getString(Const.PREF_USER_EMAIL,""));
        et_contact_number.setText(sharepref.getString(Const.PREF_USER_MOBILE_NO,""));
        btn_bookings=(Button)this.findViewById(R.id.btn_bookings);
        btn_cancel_ac=(Button)this.findViewById(R.id.btn_cancel_ac);

        btn_bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this,BookingListActivity.class));
            }
        });

        btn_cancel_ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(EditProfile.this, R.style.AppCompatAlertDialogStyle);
                builder.setTitle("Sure you want to cancel account?");
                builder.setMessage("We'll be sorry to see you go, but thanks for subscribing Ovenues.\n" +
                        "\n" +
                        "Once your account is cancelled, all of the information in your account will be permanently deleted. This can not be reversed.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new CancelAccount().execute();
                    }
                });
                //builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });


        tv_change_pwd= (TextView)this.findViewById(R.id.tv_change_pwd);
        tv_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(EditProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_dialog_change_pwd);

                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = 900;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.x=0;
                lp.y=0;
                lp.gravity = Gravity.CENTER;
                lp.dimAmount = 0;
                dialog.getWindow().setAttributes(lp);

                final EditText et_old_pwd =(EditText)dialog.findViewById(R.id.et_old_pwd);
                final EditText et_new_pwd =(EditText)dialog.findViewById(R.id.et_new_pwd);
                final EditText et_confirm_pwd =(EditText)dialog.findViewById(R.id.et_confirm_pwd);

                TextView btn_submit = (TextView) dialog.findViewById(R.id.change_pwd_tv_submit);


                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(et_old_pwd.getText().toString().length()<8 || et_new_pwd.getText().toString().length()<8
                                ||et_old_pwd.getText().toString().length()>16 || et_new_pwd.getText().toString().length()>16){
                            et_old_pwd.setError("Password must be 8 to 16 Characters !");
                        }else if(!et_new_pwd.getText().toString().equals(et_confirm_pwd.getText().toString())){
                            et_new_pwd.setError("Password not matched !");
                        }else{
                            strold_password= et_old_pwd.getText().toString();
                            strnew_password= et_new_pwd.getText().toString();
                            new ChangePassword().execute();
                        }

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });

    }


    class ChangePassword extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(EditProfile.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            Log.d("post execute", "Executando doInBackground   ingredients");
            try {



                JSONObject req = new JSONObject();
                req.put("old_password",strold_password);
                req.put("new_password",strnew_password);
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,""));

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/reset_password", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
                //Log.d("REsponce Json====",response);
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
        {  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res);
                Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi



                if(response_string.equals("success")){

                    String user_obj=obj.getString("message");

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(EditProfile.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Update !");
                    builder.setMessage(user_obj);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                            finish();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();
                    startActivity(new Intent(EditProfile.this,EditProfile.class));
                    finish();

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

                    startActivity(new Intent(EditProfile.this,EditProfile.class));
                    finish();

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

    class CancelAccount extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(EditProfile.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {
            Log.d("post execute", "Executando doInBackground   ingredients");
            try {



                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,""));

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/cancel_account", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
                //Log.d("REsponce Json====",response);
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
        {  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject obj = new JSONObject(res);
                Log.i("RESPONSE", res);

                // JSONArray array = obj.getJSONArray("results");//"" ma je key che tene pakadva mate aaj name thi aa key ne netbeans ma mukvi.

                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi



                if(response_string.equals("success")){

                    String user_obj=obj.getString("message");

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(EditProfile.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Deleted !");
                    builder.setMessage(user_obj);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Logout().execute();
                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();
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

                    startActivity(new Intent(EditProfile.this,EditProfile.class));
                    finish();

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


    class Logout extends AsyncTask<Object, Void, String> {
        private final static String TAG = "EntryActivity.EfetuaEntry";
        protected ProgressDialog progressDialog;
        String item_ingre;
        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(EditProfile.this, "Loading", "Please Wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }
        @Override
        protected String doInBackground(Object... parametros) {
            // System.out.println("On do in back ground----done-------");
            //Log.d("post execute", "Executando doInBackground   ingredients");
            try {

                JSONObject req = new JSONObject();
                req.put("user_id",sharepref.getString(Const.PREF_USER_ID,""));

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/mob_logout", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
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
        protected void onPostExecute(String result) {
            int Total_cart_amount = 0;
            int Total_cart_saving_amount = 0;
            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);

            progressDialog.dismiss();

            //Toast.makeText(MainNavigationScreen.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
            sharepref.edit().clear().commit();

            Intent intenta = new Intent(getApplicationContext(), Login.class);
            intenta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intenta);
            finish();
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "  Thank You.!!!!", Snackbar.LENGTH_LONG);
            // Changing message text color
            snackbar.setActionTextColor(Color.BLUE);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            Toast.makeText(EditProfile.this, "Sign out Done !", Toast.LENGTH_LONG).show();

            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                finish();
                return true;

        /*    case R.id.action_custom_indicator:
                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
                break;
            case R.id.action_custom_child_animation:
                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
                break;
            case R.id.action_restore_default:
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                break;
            case R.id.action_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
                startActivity(browserIntent);
                break;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
