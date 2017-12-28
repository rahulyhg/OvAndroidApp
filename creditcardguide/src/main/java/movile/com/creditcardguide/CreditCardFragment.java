package movile.com.creditcardguide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
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
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

import movile.com.creditcardguide.autocompleteSearchviewCity.SearchCityAdapter;
import movile.com.creditcardguide.autocompleteSearchviewCity.SearchCityModel;
import movile.com.creditcardguide.model.CreditCardPaymentMethod;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PurchaseOption;
import movile.com.creditcardguide.util.FontUtils;
import movile.com.creditcardguide.util.FormatUtils;
import movile.com.creditcardguide.view.CreditCardView;
import movile.com.creditcardguide.view.LockableViewPager;

import static android.content.Context.MODE_PRIVATE;
import static movile.com.creditcardguide.APICall.post;

public class CreditCardFragment extends Fragment implements TextWatcher, TextView.OnEditorActionListener, View.OnFocusChangeListener , GoogleApiClient.OnConnectionFailedListener{

    SharedPreferences sharepref;
    String discount_amount="";
    Double bill_total;

    String CONST_API_URL = "http://13.56.92.252/api/";

    private GoogleApiClient mGoogleApiClient;
    String str_place_id;
    private ArrayList<String> placeID;
    private ArrayList<String> termsList = null;
    private String str_coupon_code,str_google_city_name,latitude, longitude;

    AutoCompleteTextView et_address_line,et_city;

    EditText et_state ,et_zipcode ,et_coupnCode;
    Button calc_clear_txt_Prise;

    String str_city_id="",str_city_name="";
    SearchCityAdapter adapter ;
    ArrayList<SearchCityModel> searchcitymodel = new ArrayList<>();


    private static final int AMEX_MAX_CVV = 4;
    private static final int COMMON_MAX_CVV = 3;

    public enum Step {
        FLAG, NUMBER, EXPIRE_DATE, CVV, NAME
    }

    private static final String PURCHASE_OPTION_SELECTED = "PURCHASE_OPTION_SELECTED";

    private View viewHolder;
    private List<Step> pages;
    private ListView listFlagCreditCard;
    private EditText editNumberCard;
    private EditText editExpireCard;
    private EditText editCVVCard;
    private EditText editNameCard;

    private CreditCardView creditCardView;

    private LockableViewPager pager;
    private LinearLayout layoutPayment;
    private FrameLayout layoutData;

    private ImageView btNext;
    private Button btEdit;
    private Spinner spInstallments;

    private Button btPay;
    private TextView txtValue,txtValueCart,txtValueDiscount;
    private Switch switchSaveCard;

    private Step lastStep = Step.FLAG;
    private PurchaseOption selectedPurchaseOption;
    private InstallmentsCardAdapter installmentsCardAdapter;
    private CreditCardValidator validator;
    private List<PurchaseOption> purchaseOptions;
    private Double valueTotal = new Double(0);
    private ActionOnPayListener actionOnPayListener;
    private AtomicBoolean backFinish = new AtomicBoolean(false);
    private int colorFieldWrong;
    private int colorField;

    private TextView textLabelNumber;
    private TextView textLabelExpireDate;
    private TextView textLabelCVV;
    private TextView textLabelOwnerName;
    private TextView textLabelTotal;
    private TextInputLayout til_coupnhint;
    private LinearLayout ll_payment_bottom;

    private String labelCardOwner;
    private String labelCardDateExp;
    private String labelNumber;
    private String labelExpireDate;
    private String labelCVV;
    private String labelOwnerName;
    private String labelButtonPay;
    private String labelTotal;
    private Drawable payBackground;
    private ColorStateList btPayTextColor;
    private Boolean attrInstallments;
    private Boolean attrSaveCard;
    private CreditCardPaymentMethod savedCard;
    private boolean cardRestored;
    String LOG_TAG="LOG_TAG";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ActionOnPayListener) {
            actionOnPayListener = (ActionOnPayListener) activity;
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ActionOnPayListener) {
            actionOnPayListener = (ActionOnPayListener) context;
        }
    }

    @Override
    public void onInflate(Activity activity, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(activity, attrs, savedInstanceState);

        parseAttrs(activity, attrs);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);

        parseAttrs(context, attrs);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CreditCardFragment);

        if (typedArray != null) {
            this.labelCardOwner = typedArray.getString(R.styleable.CreditCardFragment_labelCardOwner);
            this.labelCardDateExp = typedArray.getString(R.styleable.CreditCardFragment_labelCardDateExp);
            this.labelNumber = typedArray.getString(R.styleable.CreditCardFragment_labelNumber);

            this.labelExpireDate = typedArray.getString(R.styleable.CreditCardFragment_labelExpireDate);
            this.labelCVV = typedArray.getString(R.styleable.CreditCardFragment_labelCVV);
            this.labelOwnerName = typedArray.getString(R.styleable.CreditCardFragment_labelOwnerName);
            this.labelButtonPay = typedArray.getString(R.styleable.CreditCardFragment_labelButtonPay);
            this.labelTotal = typedArray.getString(R.styleable.CreditCardFragment_labelTotal);
            this.payBackground = typedArray.getDrawable(R.styleable.CreditCardFragment_buttonPayBackground);
            this.btPayTextColor = typedArray.getColorStateList(R.styleable.CreditCardFragment_buttonPayTextColor);

            this.attrInstallments = typedArray.getBoolean(R.styleable.CreditCardFragment_installments, true);
            this.attrSaveCard = typedArray.getBoolean(R.styleable.CreditCardFragment_saveCard, true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(PURCHASE_OPTION_SELECTED, selectedPurchaseOption);
        super.onSaveInstanceState(outState);
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_credit_card2, null);
        FontUtils.loadFonts(view);

        sharepref = getContext().getSharedPreferences("MyPref", MODE_PRIVATE);



        if (savedInstanceState != null && savedInstanceState.containsKey(PURCHASE_OPTION_SELECTED)) {
            selectedPurchaseOption = (PurchaseOption) savedInstanceState.getSerializable(PURCHASE_OPTION_SELECTED);
        }

        colorFieldWrong = getActivity().getResources().getColor(R.color.red_wrong);
        colorField = getActivity().getResources().getColor(R.color.edittext_color);

        validator = new CreditCardValidatorProd();

        listFlagCreditCard = (ListView) view.findViewById(R.id.view_list_card_listview);
        creditCardView = (CreditCardView) view.findViewById(R.id.creditcard_view);
        ll_payment_bottom = (LinearLayout)view.findViewById(R.id.ll_payment_bottom);
        ll_payment_bottom.setVisibility(View.GONE);

        editNumberCard = (EditText) view.findViewById(R.id.ed_number_credit_card);
        editExpireCard = (EditText) view.findViewById(R.id.ed_expire_credit_card);
        editCVVCard = (EditText) view.findViewById(R.id.ed_cvv_credit_card);
        editNameCard = (EditText) view.findViewById(R.id.ed_name_credit_card);
        btEdit = (Button) view.findViewById(R.id.bt_edit);
        layoutPayment = (LinearLayout) view.findViewById(R.id.frg_input_card_layout_payment);
        layoutData = (FrameLayout) view.findViewById(R.id.frg_input_card_layout_data);
        spInstallments = (Spinner) view.findViewById(R.id.frg_payment_sp_portion);
        spInstallments.setVisibility(View.GONE);

        btNext = (ImageView) view.findViewById(R.id.frg_input_card_bt_next);
        btPay = (Button) view.findViewById(R.id.frg_input_card_bt_pay);
        txtValueCart = (TextView) view.findViewById(R.id.frg_input_card_txt_value_cart);
        txtValueDiscount = (TextView) view.findViewById(R.id.frg_input_card_txt_value_discount);
        txtValue = (TextView) view.findViewById(R.id.frg_input_card_txt_value);

        switchSaveCard = (Switch) view.findViewById(R.id.frg_input_card_sw_save_card);
        switchSaveCard.setVisibility(View.GONE);

        textLabelNumber = (TextView) view.findViewById(R.id.txt_label_number_card);
        textLabelExpireDate = (TextView) view.findViewById(R.id.txt_label_expire_date);
        textLabelCVV = (TextView) view.findViewById(R.id.txt_label_cvv);
        textLabelOwnerName = (TextView) view.findViewById(R.id.txt_label_owner_name);
        textLabelTotal = (TextView) view.findViewById(R.id.txt_label_total);
        til_coupnhint = (TextInputLayout)view.findViewById(R.id.til_coupnhint);

        pager = (LockableViewPager) view.findViewById(R.id.pager);

        et_address_line= (AutoCompleteTextView) view.findViewById(R.id.et_address_line);



        et_state = (EditText)view.findViewById(R.id.et_state);
        et_zipcode = (EditText)view.findViewById(R.id.et_zipcode);
        et_coupnCode = (EditText)view.findViewById(R.id.et_coupnCode);
        calc_clear_txt_Prise =(Button)view.findViewById(R.id.calc_clear_txt_Prise);
        calc_clear_txt_Prise.setVisibility(View.GONE);

        String coupon_str = getActivity().getIntent().getStringExtra("coupon_array");
        Log.d("got from Activity==",coupon_str);

        String billAmount = getActivity().getIntent().getStringExtra("billAmount");
        bill_total= Double.parseDouble(billAmount);

        JsonParser parser = new JsonParser();
        if(coupon_str.length()>1 && coupon_str!=null){


            JsonObject coupon_obj = parser.parse(coupon_str).getAsJsonObject();
            String coupon_id = coupon_obj.get("coupon_id").getAsString();
            String coupon_code = coupon_obj.get("coupon_code").getAsString();
            String discount_amount = coupon_obj.get("discount_amount").getAsString();
            String msg = coupon_obj.has("coupon_msg")?coupon_obj.get("coupon_msg").getAsString():"";

            if(Double.parseDouble(discount_amount) > bill_total){
                //til_coupnhint.setHint(msg);
                setTotalValue((double)0.0,Double.parseDouble(discount_amount),bill_total);
            }else{
                setTotalValue((double)(bill_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),bill_total);
            }
            //til_coupnhint.setHint(msg);
            //setTotalValue((double)(bill_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),bill_total);

            til_coupnhint.setHint(msg+"Discount Amount is : $"+discount_amount);
            et_coupnCode.setText(coupon_code);
            et_coupnCode.setEnabled(false);
            calc_clear_txt_Prise.setVisibility(View.VISIBLE);
            //savedCard.setCoupon(coupon_code);

        }else{

        }


        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage((FragmentActivity) getContext(), this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        et_city= (AutoCompleteTextView) view.findViewById(R.id.et_city);

        new GetPrimCity().execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewHolder = view;
        bindAttr();

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                backFinish.set(false);

                Step step = pages.get(position);

                if (step == Step.FLAG) {
                    hideKeyboard();
                }

                if (step != Step.CVV) {
                    cardRestored = false;
                }

                setNavigationButtons(step, false);
                lastStep = step;

                pageChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setIssuerCode(IssuerCode.OTHER);

        btNext.setVisibility(View.INVISIBLE);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btNext.isActivated()) {
                    nextPage();
                } else {
                    shakeField();
                }
            }
        });

        editNumberCard.addTextChangedListener(this);
        editNumberCard.setOnEditorActionListener(this);
        editNumberCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateCreditCardNumber(editNumberCard, false);
            }
        });

        editExpireCard.addTextChangedListener(this);
        editExpireCard.setOnEditorActionListener(this);
        editExpireCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateExpiredDate(editExpireCard, false);
            }
        });

        editCVVCard.addTextChangedListener(this);
        editCVVCard.setOnEditorActionListener(this);
        editCVVCard.setOnFocusChangeListener(this);
        editCVVCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateSecurityCode(selectedPurchaseOption.getIssuerCode(), editCVVCard, false);
            }
        });

        editNameCard.addTextChangedListener(this);
        editNameCard.setOnFocusChangeListener(this);
        editNameCard.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        editNameCard.setTag(new FieldValidator() {
            @Override
            public boolean isValid() {
                return validator.validateCreditCardName(editNameCard, false);
            }
        });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRestored = false;
                showEditCardLayout(true);
            }
        });

        et_coupnCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<1){
                    et_coupnCode.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_coupnCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    str_coupon_code= et_coupnCode.getText().toString();
                    et_coupnCode.performClick();
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_address_line.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    new GetCoupondiscount().execute();
                    return true;
                } if (actionId == EditorInfo.IME_FLAG_NO_ENTER_ACTION) {
                    str_coupon_code= et_coupnCode.getText().toString();
                    et_coupnCode.performClick();
                    InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_address_line.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    new GetCoupondiscount().execute();
                    return true;
                }
                return false;
            }
        });

        btPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer installments = (Integer) spInstallments.getSelectedItem();

                if(str_city_id!=null || !str_city_id.equalsIgnoreCase("")) {

                    if(editNumberCard.length()<16 || editExpireCard.length()<4 || editCVVCard.length()<3
                            || editNameCard.length()< 3){
                        Toast.makeText(getActivity(), "Credit Card Details Incomplete.", Toast.LENGTH_LONG).show();
                    }else {
                        if (validator.validateSecurityCode(selectedPurchaseOption.getIssuerCode(), editCVVCard, false)) {
                            if (validator.validateCreditCardFlag(selectedPurchaseOption, installments)) {
                                CreditCardPaymentMethod creditCardMethod = new CreditCardPaymentMethod();
                                creditCardMethod.setCreditCardNumber(editNumberCard.getText().toString().replaceAll(" ", ""));
                                creditCardMethod.setIssuerCode(selectedPurchaseOption.getIssuerCode());
                                creditCardMethod.setSecurityCode(editCVVCard.getText().toString());
                                creditCardMethod.setCreditCardName(editNameCard.getText().toString());
                                creditCardMethod.setInstallments(installments);

                                creditCardMethod.setAddress(et_address_line.getText().toString()+" , "+et_city.getText().toString());
                                creditCardMethod.setCity(/*str_city_name*/str_city_id);
                                creditCardMethod.setState(et_state.getText().toString());
                                creditCardMethod.setZip(et_zipcode.getText().toString());

                                //creditCardMethod.setCoupon(et_coupnCode.getText().toString());


                                applyExpiredDate(creditCardMethod);

                                if (actionOnPayListener != null) {
                                    actionOnPayListener.onComplete(creditCardMethod, switchSaveCard.isChecked());
                                }
                            } else {
                                //TODO: soft bug
                                Toast.makeText(getActivity(), R.string.invalid_credit_card_flag, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            pager.setCurrentItem(pages.indexOf(Step.CVV));
                            showEditCardLayout(false);
                            showKeyboard(editCVVCard);
                        }
                    }
                }else{
                    Toast.makeText(getActivity(), "Select City", Toast.LENGTH_LONG).show();
                }
            }
        });

        et_address_line.setAdapter(new GooglePlacesAutocompleteAdapter(getContext() , R.layout.text_row_list_item));
        et_address_line.setThreshold(1);


        et_address_line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Log.d("clicked place id",placeID.get(position));
                str_place_id =placeID.get(position).toString();


                new GetPlaceDteailsViaGooglePlace().execute();
                final InputMethodManager inputMethodManager = (InputMethodManager)getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(et_zipcode, InputMethodManager.SHOW_IMPLICIT);
                et_zipcode.requestFocusFromTouch();

            }
        });

        et_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getContext(), adapter.getItem(position).getId().toString(), Toast.LENGTH_SHORT).show();

                str_city_id=adapter.getItem(position).getId().toString();
                str_city_name =adapter.getItem(position).getName().toString()+adapter.getItem(position).getCounty().toString() ;
            }
        });

        calc_clear_txt_Prise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetRemoveCoupon().execute();

            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        EditText editText = null;

        if (editNumberCard.getText().hashCode() == s.hashCode()) {
            editText = editNumberCard;
            creditCardView.setTextNumber(s);
        } else if (editExpireCard.getText().hashCode() == s.hashCode()) {
            editText = editExpireCard;
            creditCardView.setTextExpDate(s);
        } else if (editCVVCard.getText().hashCode() == s.hashCode()) {
            editText = editExpireCard;
            creditCardView.setTextCVV(s);
        } else if (editNameCard.getText().hashCode() == s.hashCode()) {
            editText = editNameCard;
            creditCardView.setTextOwner(s);
        }

        if (editText != null) {
            checkColor(editText);
        }

        setNavigationButtons(lastStep, true);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT && v.getTag() instanceof FieldValidator) {
            FieldValidator fieldValidator = (FieldValidator) v.getTag();

            if (fieldValidator.isValid()) {
                nextPage();
            }

            return true;
        }

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == editCVVCard) {
            if (hasFocus) {
                creditCardView.flipToBack();
            } else {
                creditCardView.flipToFront();
            }
        } else if (v == editNameCard) {
            if (hasFocus) {
                editNameCard.setSelection(editNameCard.length());
            }
        }
    }

    private void nextPage() {
        if (pages.indexOf(lastStep) == pages.size() - 1|| cardRestored) {
            showPaymentLayout();

        } else {
            pager.setCurrentItem(pages.indexOf(lastStep) + 1);
        }
    }

    public void setLabelCardOwner(String label) {
        creditCardView.setTextLabelOwner(label);
    }

    public void setLabelCardDateExp(String label) {
        creditCardView.setTextLabelExpDate(label);
    }

    public void setLabelNumber(String label) {
        textLabelNumber.setText(label);
    }

    public void setLabelExpireDate(String label) {
        textLabelNumber.setText(label);
    }

    public void setLabelCVV(String label) {
        textLabelCVV.setText(label);
    }

    public void setLabelOwnerName(String label) {
        textLabelOwnerName.setText(label);
    }

    public void setTextButtonPay(String label) {
        //btPay.setText(label);
        btPay.setText("Confirm Booking & Pay");
    }

    public void setTextTotal(String label) {
        textLabelTotal.setText(label);
    }

    public void setBackgroundButtonPay(Drawable drawable) {
        btPay.setBackground(drawable);
    }

    public void setTextColorButtonPay(ColorStateList colorStateList) {
        btPay.setTextColor(colorStateList);
    }

    public void setVisibleInstallments(boolean visible) {
        if (visible) {
            viewHolder.findViewById(R.id.frg_input_lnl_portion).setVisibility(View.VISIBLE);
        } else {
            viewHolder.findViewById(R.id.frg_input_lnl_portion).setVisibility(View.GONE);
        }
    }

    public void setVisibleSaveCard(boolean visible) {
        if (visible) {
            viewHolder.findViewById(R.id.frg_input_frm_save_card).setVisibility(View.VISIBLE);
        } else {
            viewHolder.findViewById(R.id.frg_input_frm_save_card).setVisibility(View.GONE);
        }
    }

    private void bindAttr() {
        if (!TextUtils.isEmpty(labelCardOwner)) {
            setLabelCardOwner(labelCardOwner);
        }

        if (!TextUtils.isEmpty(labelCardDateExp)) {
            setLabelCardDateExp(labelCardDateExp);
        }

        if (!TextUtils.isEmpty(labelNumber)) {
            setLabelNumber(labelNumber);
        }

        if (!TextUtils.isEmpty(labelExpireDate)) {
            setLabelExpireDate(labelExpireDate);
        }

        if (!TextUtils.isEmpty(labelCVV)) {
            setLabelCVV(labelCVV);
        }

        if (!TextUtils.isEmpty(labelOwnerName)) {
            setLabelOwnerName(labelOwnerName);
        }

        if (!TextUtils.isEmpty(labelButtonPay)) {
            setTextButtonPay(labelButtonPay);
        }

        if (!TextUtils.isEmpty(labelTotal)) {
            setTextTotal(labelTotal);
        }

        if (payBackground != null) {
            setBackgroundButtonPay(payBackground);
        }

        if (btPayTextColor != null) {
            setTextColorButtonPay(btPayTextColor);
        }

        setVisibleInstallments(attrInstallments == null || attrInstallments);
        setVisibleSaveCard(attrSaveCard == null || attrSaveCard);
    }

    private void pageChanged() {
        if (actionOnPayListener != null) {
            actionOnPayListener.onChangedPage(lastStep);
        }
    }

    private void checkColor(EditText edit) {
        if (edit.getCurrentTextColor() == colorFieldWrong) {
            edit.setTextColor(colorField);
        }
    }

    private void shakeField() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);

        switch (lastStep) {
            case NUMBER : {
                viewHolder.findViewById(R.id.page_one).startAnimation(anim);
                editNumberCard.setTextColor(colorFieldWrong);
            } break;

            case EXPIRE_DATE : {
                viewHolder.findViewById(R.id.page_two).startAnimation(anim);
                editExpireCard.setTextColor(colorFieldWrong);
            } break;

            case CVV : {
                viewHolder.findViewById(R.id.page_three).startAnimation(anim);
                editCVVCard.setTextColor(colorFieldWrong);
            } break;

            case NAME : {
                viewHolder.findViewById(R.id.page_four).startAnimation(anim);
                editNameCard.setTextColor(colorFieldWrong);
            } break;
        }
    }

    private void setNavigationButtons(Step page, boolean isEditingText) {

        switch (page) {
            case FLAG: {
                btNext.setVisibility(View.INVISIBLE);
            } break;
            case NUMBER: {
                btNext.setVisibility(View.VISIBLE);

                if (validator.validateCreditCardNumber(editNumberCard, false)) {
                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;

            case EXPIRE_DATE: {
                btNext.setVisibility(View.VISIBLE);

                if (validator.validateExpiredDate(editExpireCard, false)) {

                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;

            case CVV: {
                btNext.setVisibility(View.VISIBLE);

                if (selectedPurchaseOption == null) {
                    // TODO: soft bug
//                    Crashlytics.log("Selected purchase Option is null");
                }

                if (validator == null) {
                    // TODO: soft bug
//                    Crashlytics.log("validator credit card is null");
                }

                if (validator.validateSecurityCode(selectedPurchaseOption.getIssuerCode(), editCVVCard, false)) {
                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;

            case NAME: {
                btNext.setVisibility(View.VISIBLE);

                if (validator.validateCreditCardName(editNameCard, false)) {
                    if (isEditingText) {
                        animBtNext();
                    }

                    btNext.setActivated(true);
                } else {
                    btNext.setActivated(false);
                }
            } break;
        }

    }


    private void showEditCardLayout(final boolean editFlagCard) {
        backFinish.set(false);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_rigth_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutPayment.setVisibility(View.GONE);

                if (editFlagCard) {
                    switchSaveCard.setVisibility(View.VISIBLE);
                    btEdit.setVisibility(View.INVISIBLE);
                }

                pageChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        if (editFlagCard) {
            pager.setCurrentItem(pages.indexOf(Step.FLAG));
            clearFields();

            Snackbar snackbar = Snackbar.make(viewHolder, R.string.option_pay_using_other_card_chosen, Snackbar.LENGTH_LONG)
                    .setActionTextColor(getActivity().getResources().getColor(R.color.white))
                    .setAction(R.string.undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            restoreSavedCard(getCurrentCcv());
                        }
                    });

            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }

        layoutPayment.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_rigth_in);
        layoutData.startAnimation(anim2);
        layoutData.setVisibility(View.VISIBLE);
    }

    private void animBtNext() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.pop_bounce_show);

        if (!btNext.isActivated()) {
            btNext.startAnimation(animation);
        }
    }

    private void clearFields() {
        editNumberCard.setText("");
        editExpireCard.setText("");
        editCVVCard.setText("");
        editNameCard.setText("");
        creditCardView.clear();

        setIssuerCode(IssuerCode.OTHER);
    }

    private void showPaymentLayout() {
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_left_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutData.setVisibility(View.GONE);
                ll_payment_bottom.setVisibility(View.VISIBLE);
                btPay.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.pop_bounce_show));
                pageChanged();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        layoutData.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.trans_left_in);
        layoutPayment.startAnimation(anim2);
        layoutPayment.setVisibility(View.VISIBLE);

        hideKeyboard();
    }

    private void hideKeyboard() {
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    public void setPagesOrder(Step... pages) throws IllegalArgumentException {
        if (pages.length < Step.values().length) {
            throw new IllegalArgumentException("You need provide at least " + Step.values().length + " pages");
        } else {
            this.pages = new ArrayList<>();

            for (int i = 0; i < Step.values().length; i++) {
                this.pages.add(pages[i]);
            }

            pager.setOffscreenPageLimit(5);
            FieldsPageAdapter adapter = new FieldsPageAdapter(viewHolder, this.pages);
            pager.setAdapter(adapter);
        }
    }

    public void setListPurchaseOptions(List<PurchaseOption> optionList) {
        setListPurchaseOptions(optionList, 0D);
    }

    public void setListPurchaseOptions(List<PurchaseOption> optionList, Double totalValue) {
        this.purchaseOptions = optionList;
        FlagCardAdapter flagCardAdapter = new FlagCardAdapter(getActivity(), purchaseOptions);
        listFlagCreditCard.setAdapter(flagCardAdapter);
        listFlagCreditCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                purchaseOptionSelected(purchaseOptions.get(i));
            }
        });

        if(et_coupnCode.length()<1) {
            setTotalValue(totalValue,0.0,totalValue);
            bill_total = totalValue;
        }
    }

    public void setTotalValue(Double net_totalValue,Double discount,Double cart_total) {
        valueTotal = net_totalValue;

        txtValueCart.setText(String.format("%s %s","", FormatUtils.getCurrencyFormat().format(cart_total)));
        txtValueDiscount.setText(String.format("%s %s", "-",FormatUtils.getCurrencyFormat().format(discount)));
        txtValue.setText(String.format("%s %s","", FormatUtils.getCurrencyFormat().format(valueTotal)));
    }

    private void purchaseOptionSelected(PurchaseOption purchaseOption) {
        selectedPurchaseOption = purchaseOption;

        List<Integer> installments = new ArrayList<>();

        for (int i = 1; purchaseOption.getType() != null && i <= purchaseOption.getInstallments(); i++) {
            installments.add(i);
        }

        if (installments.isEmpty()) {
            spInstallments.setClickable(false);
        } else {
            spInstallments.setClickable(true);
        }

        installmentsCardAdapter = new InstallmentsCardAdapter(getActivity(), installments);
        spInstallments.setAdapter(installmentsCardAdapter);

        if (purchaseOption.getIssuerCode() == IssuerCode.AMEX) {
            int maxLength = AMEX_MAX_CVV;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editCVVCard.setFilters(fArray);
        } else {
            int maxLength = COMMON_MAX_CVV;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editCVVCard.setFilters(fArray);
        }

        setIssuerCode(purchaseOption.getIssuerCode());
        showKeyboard(editNumberCard);

        nextPage();
    }

    private void showKeyboard(final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 400);
    }

    private void setIssuerCode(IssuerCode issuerCode) {
        editCVVCard.setText("");
        creditCardView.chooseFlag(issuerCode);
    }

    public void restoreSavedCard(CreditCardPaymentMethod creditCardPaymentMethod) {
        this.savedCard = creditCardPaymentMethod;
        boolean canPayWithSaveCard = false;
        int installments = 0;

        if (creditCardPaymentMethod != null) {
            for (PurchaseOption purchaseOption : purchaseOptions) {
                if (creditCardPaymentMethod.getIssuerCode() == purchaseOption.getIssuerCode()) {
                    canPayWithSaveCard = true;
                    installments = purchaseOption.getInstallments();
                    break;
                }
            }
        }

        if (creditCardPaymentMethod != null && canPayWithSaveCard) {
            cardRestored = true;
            backFinish.set(true);
            editNumberCard.setText(creditCardPaymentMethod.getCreditCardNumber());
            creditCardView.setTextNumber(editNumberCard.getText());
            creditCardView.setTextOwner(creditCardPaymentMethod.getCreditCardName());
            editNameCard.setText(creditCardPaymentMethod.getCreditCardName());
            String expireDateS = String.format("%s/%s",
                    String.format("%02d", creditCardPaymentMethod.getExpireMonth() != null ?
                            creditCardPaymentMethod.getExpireMonth() : 0),
                    creditCardPaymentMethod.getExpireYear());

            creditCardView.setTextExpDate(expireDateS);
            editExpireCard.setText(expireDateS);
            btEdit.setVisibility(View.VISIBLE);
            switchSaveCard.setChecked(true);
            switchSaveCard.setVisibility(View.GONE);

            List<Integer> installmentsList = new ArrayList<>();

            for (int i = 1; i <= installments; i++) {
                installmentsList.add(i);
            }

            if (installmentsList.isEmpty()) {
                spInstallments.setClickable(false);
            } else {
                spInstallments.setClickable(true);
            }

            installmentsCardAdapter = new InstallmentsCardAdapter(getActivity(), installmentsList);
            spInstallments.setAdapter(installmentsCardAdapter);


            selectedPurchaseOption = new PurchaseOption(creditCardPaymentMethod.getType(),
                    creditCardPaymentMethod.getIssuerCode(),
                    creditCardPaymentMethod.getInstallments());

            if (creditCardPaymentMethod.getIssuerCode() == IssuerCode.AMEX) {
                int maxLength = 4;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                editCVVCard.setFilters(fArray);
            } else {
                int maxLength = 3;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                editCVVCard.setFilters(fArray);
            }

            layoutPayment.setVisibility(View.VISIBLE);
            layoutData.setVisibility(View.GONE);

            pageChanged();

            setIssuerCode(selectedPurchaseOption.getIssuerCode());
        }
    }

    private CreditCardPaymentMethod getCurrentCcv() {
        return savedCard;
    }

    private void applyExpiredDate(CreditCardPaymentMethod creditCardMethod) {
        String value = editExpireCard.getText().toString();
        value = value.replace("/", "");

        String expire = TextUtils.substring(value, 0, 2);

        if (TextUtils.isDigitsOnly(expire)) {
            creditCardMethod.setExpireMonth(Integer.valueOf(expire));
        }

        expire = TextUtils.substring(value, 2, value.length());

        if (TextUtils.isDigitsOnly(expire)) {
            creditCardMethod.setExpireYear(Integer.valueOf(expire));
        }
    }

    public void backPressed() {
        if (layoutData.getVisibility() == View.VISIBLE) {
            if (pages.indexOf(lastStep) > 0) {
                pager.setCurrentItem(pages.indexOf(lastStep) - 1);
            } else {
                /*if(discount_amount!=null || discount_amount.length()>0){
                    //new GetRemoveCoupon().execute();
                }*/
                getActivity().finish();
            }
        } else {
            if (backFinish.compareAndSet(true, false)) {
                getActivity().finish();
            } else {
                pager.setCurrentItem(pages.size() - 1);
                showEditCardLayout(false);
            }
        }
    }


    private class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements
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

    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";

    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE
                    + TYPE_AUTOCOMPLETE + OUT_JSON);
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
                //System.out.println("whole response of API =="+jsonObj.toString());

                // Extract the Place descriptions from the results
                resultList = new ArrayList(predsJsonArray.length());
                termsList = new ArrayList<String>();

                for (int i = 0; i < predsJsonArray.length(); i++) {
                    //System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                    //System.out.println("============================================================");
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

                    placeID.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                    //System.out.println("place id"+predsJsonArray.getJSONObject(i).getString("place_id"));
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    String res_coupn;
    class GetCoupondiscount extends AsyncTask<Object, Void, String> {

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
              /*  http://ovenues.co.us/api/apply_coupen/coupon_code/testflat/user_id/38*/
                String response = post(CONST_API_URL+"apply_coupen/coupon_code/"+str_coupon_code+"/user_id/"+sharepref.getString("PREF_USER_ID",""), "","get");
                Log.d("req Json====",CONST_API_URL+"apply_coupen/coupon_code/"+str_coupon_code+"/user_id/"+sharepref.getString("PREF_USER_ID",""));
                res_coupn=response;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_coupn;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);



            try{JSONObject obj = new JSONObject(res_coupn);
                Log.i("RESPONSE", res_coupn);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_coupn).getAsJsonObject();

                    if(!rootObj.get("message").getAsJsonObject().isJsonNull()){
                        String  msg  = rootObj.getAsJsonObject("message").get("msg").getAsString();
                        til_coupnhint.setHint(msg);
                        til_coupnhint.setEnabled(false);
                        discount_amount  = rootObj.getAsJsonObject("message").get("discount_amount").getAsString();
                        setTotalValue((double)(bill_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),bill_total);

                        til_coupnhint.setHint(msg+"Discount Amount is : $"+discount_amount);
                        calc_clear_txt_Prise.setVisibility(View.VISIBLE);
                        savedCard.setCoupon(str_coupon_code);
                    }else {

                    }
                } else if(response_string.equals("fail")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_coupn).getAsJsonObject();

                    String  msg  = rootObj.getAsJsonObject().get("message").getAsString();
                    til_coupnhint.setHint(msg);
                    til_coupnhint.setEnabled(false);
                    discount_amount  = "0.0";
                    setTotalValue((double)(bill_total- Double.parseDouble(discount_amount)),Double.parseDouble(discount_amount),bill_total);

                    til_coupnhint.setHint(msg);
                    calc_clear_txt_Prise.setVisibility(View.VISIBLE);
                    savedCard.setCoupon(str_coupon_code);

                } else{
                    /*String message=obj.getString("message");
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
                    snackbar.show();*/
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    class GetRemoveCoupon extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute()//execute thaya pela
        {
            super.onPreExecute();
            // Log.d("pre execute", "Executando onPreExecute ingredients");
            //pg_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... parametros) {
            http://ovenues.co.us/api//user_id/38

            try {
              /*  http://ovenues.co.us/api/apply_coupen/coupon_code/testflat/user_id/38*/
                String response = post(CONST_API_URL+"remove_coupen/user_id/"+sharepref.getString("PREF_USER_ID",""), "","get");
               // Log.d("req Json====",CONST_API_URL+"remove_coupen/user_id/"+sharepref.getString("PREF_USER_ID",""));
                res_coupn=response;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return res_coupn;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);



            try{JSONObject obj = new JSONObject(res_coupn);
                Log.i("RESPONSE", res_coupn);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){


                    et_coupnCode.setText("");
                    til_coupnhint.setHint("Coupon Code");
                    til_coupnhint.setEnabled(true);
                    calc_clear_txt_Prise.setVisibility(View.GONE);

                    //setTotalValue((double)(valueTotal + Double.parseDouble(discount_amount)),0.0,bill_total);
                    setTotalValue(bill_total,0.0,bill_total);
                    /*JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_coupn).getAsJsonObject();

                    if(!rootObj.get("message").getAsJsonObject().isJsonNull()){
                        String  msg  = rootObj.getAsJsonObject("message").get("msg").getAsString();
                        til_coupnhint.setHint(msg);
                        discount_amount  = rootObj.getAsJsonObject("message").get("discount_amount").getAsString();
                        setTotalValue((double)(bill_total- Double.parseDouble(discount_amount)));

                        til_coupnhint.setHint(msg+"Discount Amount is : $"+discount_amount);
                        calc_clear_txt_Prise.setVisibility(View.VISIBLE);

                    }else {

                    }*/

                } else{
                    til_coupnhint.setEnabled(true);
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
    }

    String res;
    class GetPlaceDteailsViaGooglePlace extends AsyncTask<Object, Void, String> {

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

                String response = post("https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4", "","get");
                Log.d("URL====","https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4");
                https://maps.googleapis.com/maps/api/place/details/json?placeid="+str_place_id+"&key=AIzaSyDtr3qaj_rlFGjLyPW4OClB7r7oO6S5jj4
                res=response;
                //Log.d("RESPONCE PLACE====",response);
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try {
                JsonParser parser = new JsonParser();
                JsonObject rootObj = parser.parse(res).getAsJsonObject();
                if (rootObj.get("status").getAsString().equalsIgnoreCase("NOT_FOUND")) {
                    Toast.makeText(getContext(),"Sorry , All address details not found.\nAdd manually. ",Toast.LENGTH_LONG).show();
                } else {

                    String street_number = "", route = "";
                    JsonArray address_components = rootObj.getAsJsonObject("result").getAsJsonArray("address_components");
                    for (int a = 0; a < address_components.size(); a++) {

                        Gson gson = new Gson();
                        JsonArray types_array = address_components.get(a).getAsJsonObject().getAsJsonArray("types");

                        //Log.d("address type array",types_array.toString());

                        Type type = new TypeToken<List<String>>() {
                        }.getType();
                        List<String> typeList = gson.fromJson(types_array.toString(), type);

                        if (typeList.get(0).equalsIgnoreCase("street_number")) {
                            street_number = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                        }
                        if (typeList.get(0).equalsIgnoreCase("route")) {
                            route = address_components.get(a).getAsJsonObject().get("long_name").getAsString();
                        }
                        if (typeList.get(0).equalsIgnoreCase("locality")) {
                            et_city.setText(address_components.get(a).getAsJsonObject().get("long_name").getAsString());
                            str_google_city_name = et_city.getText().toString();
                            new GetgoogleToPrimeCity().execute();
                        }
                        if (typeList.get(0).equalsIgnoreCase("administrative_area_level_1")) {
                            et_state.setText(address_components.get(a).getAsJsonObject().get("long_name").getAsString());
                        }
                        if (typeList.get(0).equalsIgnoreCase("postal_code")) {
                            et_zipcode.setText(address_components.get(a).getAsJsonObject().get("long_name").getAsString());
                        }
                    }
                    et_address_line.setText(street_number + " " + route);

                    //===California VALIDATION , ONLLY California CITY PLACE IS ALLOWED==========
                    if (!et_state.getText().toString().equalsIgnoreCase("California")) {
                        et_address_line.setText("");
                        et_city.setText("");
                        et_state.setText("");
                        et_zipcode.setText("");
                    }


                /*String adr_address = rootObj.getAsJsonObject("result").get("adr_address").getAsString();
                adr_address=Html.fromHtml(adr_address)+"";
                Log.d("got address is ==",adr_address);
                String[] addre = adr_address.split(",");
                for(int j=0;j<addre.length;j++){
                    if(j==0){et_address_line.setText(addre[0].toString());                    }
                    if(j==1){et_city.setText(addre[1].toString());}
                    //if(j==2){et_state.setText(addre[2].toString());}
                    if(j==3){et_zipcode.setText(addre[2].toString().substring(addre[2].toString().length()-6));}
                }*/
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }

    String res_custom_city;
    class GetPrimCity extends AsyncTask<Object, Void, String> {

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

                String response = post(CONST_API_URL +"prime_cities", "","get");
                //Log.d("REsponce Json====",response);
                res_custom_city=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_custom_city;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{JSONObject obj = new JSONObject(res_custom_city);
                Log.i("RESPONSE", res_custom_city);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_custom_city).getAsJsonObject();

                    JsonArray citiesObj = rootObj.getAsJsonArray("message");
                   
                    for (int j = 0; j < citiesObj.size(); j++) {
                        String city_id=citiesObj.get(j).getAsJsonObject().get("city_id").getAsString();
                        String city_name=citiesObj.get(j).getAsJsonObject().get("city_name").getAsString();
                        String county=citiesObj.get(j).getAsJsonObject().get("county").getAsString();

                        searchcitymodel.add(new SearchCityModel(city_id,city_name,county));

                    }

                    adapter = new SearchCityAdapter(getContext(), searchcitymodel);
                    et_city.setAdapter(adapter);
                    adapter.notifyDataSetChanged();





                } else{
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(getView().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
    }

    String res_googleToPrimCIty;
    class GetgoogleToPrimeCity extends AsyncTask<Object, Void, String> {

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

                String response = post(CONST_API_URL+"city_by_name/"+str_google_city_name, "","get");
                Log.d("googel to rpime URL=",CONST_API_URL+"city_by_name/"+str_google_city_name);
                res_googleToPrimCIty=response;
            }/*catch (JSONException e) {
                e.printStackTrace();
            }*/ catch (IOException e) {
                e.printStackTrace();
            }


            return res_googleToPrimCIty;

        }


        @Override
        protected void onPostExecute(String result) {

            String response_string = "";
            // System.out.println("OnpostExecute----done-------");
            super.onPostExecute(result);


            try{JSONObject obj = new JSONObject(res_googleToPrimCIty);
                Log.i("res googletoprimcode", res_googleToPrimCIty);
                response_string=obj.getString("status");//"" ma je key hoi tej key nb ma rakvi

                if(response_string.equals("success")){

                    JsonParser parser = new JsonParser();

                    JsonObject rootObj = parser.parse(res_googleToPrimCIty).getAsJsonObject();

                    if(!rootObj.get("message").getAsJsonObject().isJsonNull()){
                        String  city_id  = rootObj.getAsJsonObject("message").get("city_id").getAsString();
                        String city_name  = rootObj.getAsJsonObject("message").get("city_name").getAsString();
                        String county  = rootObj.getAsJsonObject("message").get("county").getAsString();
                        str_city_id=city_id;
                        et_city.setText(city_name);
                    }else {
                        et_city.setError("enter city name here!");
                    }
                } else{
                    et_city.setError("enter city name here!");
                    String message=obj.getString("message");
                    Snackbar snackbar = Snackbar
                            .make(getActivity().getCurrentFocus().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
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
    }
}
