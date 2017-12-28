package ovenues.com.ovenue;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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

import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;

public class ContactUs extends AppCompatActivity {


    TextView et_fullname,et_email,et_phoneno ,et_purpose,et_description ,btn_submit;
    String str_et_fullname, str_et_email, str_et_phoneno, str_et_purpose, str_et_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        et_fullname = (TextView)this.findViewById(R.id.et_fullname);
        et_email = (TextView)this.findViewById(R.id.et_email);
        et_phoneno = (TextView)this.findViewById(R.id.et_phoneno);
        et_purpose = (TextView)this.findViewById(R.id.et_purpose);
        et_description = (TextView)this.findViewById(R.id.et_description);

        btn_submit = (TextView)this.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(et_fullname.getText().toString().length()==0){
                    et_fullname.setError("Please Enter Name !");
                }else if(et_email.getText().toString().length()==0
                        || !et_email.getText().toString().matches(emailPattern)
                        || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")){
                    et_email.setError("Please Enter Email Address !");
                }else if(et_phoneno.getText().toString().length()<10
                        || et_phoneno.getText().toString().length()>10){
                    et_phoneno.setError("Please Enter Correct Contact Number !");
                }else if(et_purpose.getText().toString().length()==0){
                    et_purpose.setError("Please Enter Purpose !");
                }else if(et_description.getText().toString().length()==0){
                    et_description.setError("Please Enter Description !");
                }else {
                    str_et_fullname = et_fullname.getText().toString();
                    str_et_email = et_email.getText().toString();
                    str_et_phoneno = et_phoneno.getText().toString();
                    str_et_purpose = et_purpose.getText().toString();
                    str_et_description = et_description.getText().toString();

                    new ContactUsSync().execute();
                }
            }
        });

    }


    String res;
    class ContactUsSync extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(ContactUs.this, "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            Log.d("post execute", "Executando doInBackground   ingredients");
            try {




                JSONObject req = new JSONObject();
                req.put("first_name",str_et_fullname);
                req.put("email",str_et_email);
                req.put("phone",str_et_phoneno);
                req.put("purpose",str_et_purpose);
                req.put("description",str_et_description);

                Log.d("REq Json======", req.toString());

                String response = post(Const.SERVER_URL_API +"/contactus", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
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
        { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            super.onPostExecute(result);
        }
            System.out.println("OnpostExecute----done-------");
            progressDialog.dismiss();

            if (res == null || res.equals("")) {

                progressDialog.dismiss();
                Toast.makeText(ContactUs.this, "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
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
                            new AlertDialog.Builder(ContactUs.this, R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Sent !");
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
       /* if(sharepref.getString("key_login","yes").equals("yes")){

            finish();
        }else{
            System.exit(0);
            finish();
        }*/
    }


}
