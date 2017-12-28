package ovenues.com.ovenue.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import movile.com.creditcardguide.APICall;
import ovenues.com.ovenue.MainNavigationScreen;
import ovenues.com.ovenue.R;
import ovenues.com.ovenue.adapter.Spiners.CustomSpinnerAdapterWhiteText;
import ovenues.com.ovenue.utils.Const;

import static ovenues.com.ovenue.utils.APICall.post;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListBusiness extends Fragment {
    Spinner spin_businessType;

    public static String str_place_id;

    public static ArrayList<String> termsList = null;
    public static GoogleApiClient mGoogleApiClient;
    public static ArrayList<String> placeID;

    String businetitle[] = {"Select Business Type" , "Catering" , "Bakery" , "Photography" , "Music & Dance" , "Decoration" ,"Entertainer" ,
            "Invitations","Event Planner" ,"Gifts & Party Favours","Makeup & Beauty",
            "Party Supplies","Electronic Rentals","Other"};

    EditText et_businessName,et_website ,et_phoneNumber ,et_email,et_contactManager ,et_description ;
    AutoCompleteTextView et_address;
    TextView tv_submit;
    CheckBox cb_terms;

    String str_et_businessName,str_et_website,str_et_address,str_et_phoneNumber,str_et_email,str_et_contactManager,str_et_description,str_spin_businessType;

    public ListBusiness() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_list_business, container, false);


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        spin_businessType = (Spinner)view.findViewById(R.id.sp_businesstype);

        et_businessName = (EditText)view.findViewById(R.id.et_businessName);
        et_website = (EditText)view.findViewById(R.id.et_website);
        et_address = (AutoCompleteTextView)view.findViewById(R.id.et_address);
        et_phoneNumber = (EditText)view.findViewById(R.id.et_phoneNumber);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_contactManager = (EditText)view.findViewById(R.id.et_contactManager);
        et_description = (EditText)view.findViewById(R.id.et_description);


        et_address.setAdapter(new GooglePlacesAutocompleteAdapter(getContext() , R.layout.text_row_list_item));
        et_address.setThreshold(1);
        et_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Log.d("clicked place id",placeID.get(position));
                str_place_id =placeID.get(position).toString();

                //Log.d("Log place clicked===",str_place_id);
                new GetPlaceDteailsViaGooglePlace().execute();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_address.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }
        });



        cb_terms =(CheckBox)view.findViewById(R.id.cb_terms);

        tv_submit = (TextView)view.findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!cb_terms.isChecked()){
                    Toast.makeText(getContext(),"Please accept Terms and Conditions.",Toast.LENGTH_LONG).show();
                }else {
                    if (et_businessName.getText().toString().length() == 0) {
                        et_businessName.setError("Please Enter Name !");
                    }else if (et_website.getText().toString().length() == 0) {
                        et_website.setError("Please Enter Website!");
                    } else if (et_address.getText().toString().length() == 0) {
                        et_address.setError("Please Enter Address !");
                    }  else if (et_email.getText().toString().length() == 0
                            || !et_email.getText().toString().matches(emailPattern)
                            || et_email.getText().toString().matches("[0-9]+@[0-9]+@[0-9]")) {
                        et_email.setError("Please Enter Email Address !");
                    } else if (et_phoneNumber.getText().toString().length() < 10
                            || et_phoneNumber.getText().toString().length() > 12) {
                        et_phoneNumber.setError("Please Enter Correct Contact Number !");
                    } else if (et_contactManager.getText().toString().length() == 0) {
                        et_contactManager.setError("Please Enter Manager Name !");
                    } else if (et_description.getText().toString().length() == 0) {
                        et_description.setError("Please Enter Description !");
                    } else {
                        str_et_businessName= et_businessName.getText().toString();
                        str_et_website = et_website.getText().toString();
                        str_et_address = et_address.getText().toString();
                        str_et_phoneNumber = et_phoneNumber.getText().toString();
                        str_et_email = et_email.getText().toString();
                        str_et_contactManager = et_contactManager.getText().toString();
                        str_et_description = et_description.getText().toString();
                        str_spin_businessType = spin_businessType.getSelectedItem().toString(); //String.valueOf(spin_businessType.getSelectedItemPosition());

                        new ListBusinessAsync().execute();
                    }
                }

            }
        });

        CustomSpinnerAdapterWhiteText customSpinnerAdapterWhiteText =new CustomSpinnerAdapterWhiteText(getContext() , businetitle);
        spin_businessType.setAdapter(customSpinnerAdapterWhiteText);

        return  view;
    }

    String res;
    class ListBusinessAsync extends AsyncTask<Object, Void, String> {

        private final static String TAG = "EntryActivity.EfetuaEntry";

        protected ProgressDialog progressDialog;
        String response_string;
        @Override
        protected void onPreExecute()//execute thaya pela
        {

            super.onPreExecute();
            Log.d("pre execute", "Pre execute Done");

            //inicia diálogo de progress, mostranto processamento com servidor.
            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...", true, false);
            //progressDialog no use gol chakadu lavava mate thay.
        }

        @Override
        protected String doInBackground(Object... parametros) {

            System.out.println("On do in back ground----done-------");


            Log.d("post execute", "Executando doInBackground   ingredients");
            try {


                JSONObject req = new JSONObject();
                req.put("name_of_business",str_et_businessName);
                req.put("type_of_business",str_spin_businessType);
                req.put("address",str_et_address);
                req.put("phone",str_et_phoneNumber);
                req.put("email",str_et_email);
                req.put("contact_manager",str_et_contactManager);
                req.put("website",str_et_website);
                req.put("description",str_et_description);
                req.put("terms","true");
                Log.d("REq Json======", req.toString());
                String response = post(Const.SERVER_URL_API +"/list_your_business", req.toString(),"post");//post("http://54.153.127.215/api/login", req.toString());
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
                Toast.makeText(getActivity(), "Network connection ERROR or ERROR", Toast.LENGTH_LONG).show();
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
                            new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                    builder.setTitle("Successfully Sent !");
                    builder.setMessage(user_obj);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), MainNavigationScreen.class));

                        }
                    });
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();


                }

                else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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

    public static String res_google_place;
    public static class GetPlaceDteailsViaGooglePlace extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... parametros) {

            try {

                String response = APICall.post("https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4", "","get");
                // Log.d("URL====","https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
                res_google_place=response;
                //Log.d("RESPONCE PLACE====",response);
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_google_place;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res_google_place).getAsJsonObject();

                String street_number="",route="",city="",state="",postalcode="";
                JsonArray address_components = rootObj.getAsJsonObject("result").getAsJsonArray("address_components");
                for(int a=0;a<address_components.size();a++){

                    Gson gson = new Gson();
                    JsonArray types_array = address_components.get(a).getAsJsonObject().getAsJsonArray("types");

                    //Log.d("address type array",types_array.toString());

                    Type type = new TypeToken<List<String>>(){}.getType();
                    List<String> typeList = gson.fromJson(types_array.toString(), type);

                    if(typeList.get(0).equalsIgnoreCase("street_number")){
                        street_number=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("route")){
                        route=address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("locality")){
                        city = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                    if(typeList.get(0).equalsIgnoreCase("administrative_area_level_1")){
                        state = address_components.get(a).getAsJsonObject().get("long_name").getAsString() ;
                    }
                    if(typeList.get(0).equalsIgnoreCase("postal_code")){
                        postalcode = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                    }
                }

               /* if(et_address_line_balkery_custom_request.getVisibility()==View.VISIBLE){
                    et_address_line_balkery_custom_request.setText(street_number+" "+route+","+city+","+state+","+postalcode);
                }*/

                //===California VALIDATION , ONLLY California CITY PLACE IS ALLOWED==========
                if(!state.equalsIgnoreCase("California")){
                    /*if(et_address_line_balkery_custom_request.getVisibility()==View.VISIBLE){
                        et_address_line_balkery_custom_request.setText("");
                    }*/
                    //Toast.makeText(,"sorry! now we are serving only in california .",Toast.LENGTH_LONG).show();
                }else{
                    //new GetDeliveryRate().execute();
                }


                /*String adr_address = rootObj.getAsJsonObject("result").get("adr_address").getAsString();
                adr_address=Html.fromHtml(adr_address)+"";
                Log.d("got address is ==",adr_address);
                String[] addre = adr_address.split(",");
                for(int j=0;j<addre.length;j++){
                    if(j==0){et_address_line_balkery_custom_request.setText(addre[0].toString());                    }
                    if(j==1){et_city.setText(addre[1].toString());}
                    //if(j==2){et_state.setText(addre[2].toString());}
                    if(j==3){et_zipcode.setText(addre[2].toString().substring(addre[2].toString().length()-6));}
                }*/
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public static class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements
            Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public android.widget.Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // String filterString = constraint.toString();
                    // System.out.println("Text:::::::::::"+filterString);
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        //System.out.println("Text:::::::::::" + constraint.toString());
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }
    }


    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(Const.PLACES_API_BASE
                    + Const.TYPE_AUTOCOMPLETE + Const.OUT_JSON);
            sb.append("?key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
            // sb.append("&components=country:il");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            URL url = new URL(sb.toString());
            //System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            if (placeID == null)
            {placeID = new ArrayList<String>();}
            else{
                placeID.clear();

                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
                JSONArray termsJsonArray = null;
                // System.out.println(predsJsonArray);

                // Extract the Place descriptions from the results
                resultList = new ArrayList(predsJsonArray.length());
                termsList = new ArrayList<String>();

                for (int i = 0; i < predsJsonArray.length(); i++) {
                    //System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                    //System.out.println("============================================================");
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

                    placeID.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                    //Log.d("place id",predsJsonArray.getJSONObject(i).getString("place_id"));
                    termsJsonArray = predsJsonArray.getJSONObject(i).getJSONArray(
                            "terms");

                    //System.out.println("termsJsonArray:::::::" + termsJsonArray);

                    int lenght = termsJsonArray.length() - 1;

                    //System.out.println("lenght:::::::" + lenght);

                    termsList.add(termsJsonArray.getJSONObject(lenght).getString("value"));
                    // System.out.println("termsList:::::::::::" + termsList);
                    // }
                }
            }
        } catch (JSONException e) {

        }

        return resultList;
    }


}
