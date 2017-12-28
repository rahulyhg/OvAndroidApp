package ovenues.com.ovenue.utils;

import android.os.Environment;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Const {

	public static String SD_CARD_PATH = Environment
			.getExternalStorageDirectory() + "/" + "Ovenues";
	public static String MyPREFERENCES = "OvenuesPref";

	public static String [] name = {"Who We Are?"};
	public static String [] subName = {"If you’ve ever planned an event, you know that it can be tedious, stressful, and often less rewarding than you hoped for. At Ovenues, we know exactly how you feel. That’s why we simplified the event-planning process so that you can focus on celebrating. Book with us, we’ll take care of the rest.\n" +
			"\n" +
			"Our dreams of simplifying the event-planning process was inspired by our past struggles. Even simple events can require endless phone calls, coordination with multiple businesses, and hours spent waiting. We faced the same problems in planning personal and corporate events. While some websites existed to fulfill our needs as hosts, no company could enable us to find and book the perfect venue and provide us with all the services that we needed for a great party. After one too many stressful and imperfect events, we’d had enough. That is when the idea for our marketplace was conceived.\n" +
			"\n" +
			"Ovenues provides comprehensive event-planning resources that can meet all of your needs no matter what you plan to host. In a world where technology can separate people and keep them in their own worlds, we use it to bring people together by allowing them to create memories of lifetime with their friends, colleagues, and communities. From the food on your plate to the music in the air and the dance floor under your feet, Ovenues has got you covered in just a few clicks."};
	public static String [] image = {""};


	public static String PREF_LOGINKEY= "PREF_LOGINKEY";
	public static String INTRO_DONE= "INTRO_DONE";
	public static String PREF_USER_ID = "PREF_USER_ID";
	public static String PREF_FACEBOOK_ID = "PREF_FACEBOOK_ID";
	public static String PREF_USER_MOBILE_NO = "PREF_USER_MOBILE_NO";
	public static String PREF_USER_FULL_NAME = "PREF_USER_FULL_NAME";
	public static String PREF_USER_PROFILE_PIC_URL = "PREF_USER_PROFILE_PIC_URL";
	public static String PREF_PASSWORD = "PREF_PASSWORD";
	public static String PREF_USER_EMAIL = "PREF_USER_EMAIL";
	public static String PREF_USER_TOKEN = "PREF_USER_TOKEN";
	public static String PREF_USER_CART_TOTAL_ITEMS = "PREF_USER_CART_TOTAL_ITEMS";
	public static String PREF_USER_CART_TOTAL_AMOUNT = "PREF_USER_CART_TOTAL_AMOUNT";

	public static String PREF_USER_CART_VENUES = "PREF_USER_CART_VENUES";
	public static String PREF_USER_CART_SERVICES = "PREF_USER_CART_SERVICES";
	public static String PREF_USER_CART_CATERINGS = "PREF_USER_CART_CATERINGS";

	//===type of pages

	public static String CONST_HOMESERVICE = "CONST_HOMESERVICE";
	public static String CONST_SERVICE = "CONST_SERVICE";
	public static String CONST_VENUE = "CONST_VENUE";
	public static String CONST_TOPFIVE = "CONST_TOPFIVE";


	//====Pref List for search venues acording filters
	public static String PREF_CITY_ID= "CITY_ID";
	public static String PREF_CITY_NAME= "PREF_CITY_NAME";
	public static String PREF_EVENT_TYPE_ID= "EVENT_TYPE_ID";
	public static String PREF_VENUE_TYPE_ID = "VENUE_TYPE_ID";
	public static String PREF_AMENITIES_ID = "PREF_AMENITIES_ID";
	public static String PREF_GUEST_COUNT_MIN = "GUEST_COUNT_MIN";
	public static String PREF_GUEST_COUNT_MAX = "GUEST_COUNT_MAX";
	public static String PREF_PRICE_MIN = "PRICE_MIN";
	public static String PREF_PRICE_MAX = "PRICE_MAX";
	public static String PREF_SORT_BY = "SORT_BY";

	//====Pref List for search services acording filters

	public static String PREF_STR_SERVICE_ID= "EVENT_TYPE_ID";
	public static String PREF_STR_SERVICE_NAME= "PREF_STR_SERVICE_NAME";
	//====Pref List for search serice provider acording filters
	public static String PREF_CITY_ID_SP= "PREF_CITY_ID_SP";
	public static String PREF_CITY_NAME_SP= "PREF_CITY_NAME_SP";
	public static String PREF_PRICE_PRICERANGE_SP = "PREF_PRICE_PRICERANGE_SP";
	public static String PREF_NEARBY_SP = "PREF_NEARBY_SP";
	public static String PREF_SORT_BY_SP = "PREF_SORT_BY_SP";

	//======Josn for venues packages cart item add call API

	public static JSONObject STATIC_JSON_VENUE_PRICING= new JSONObject();
	public static JSONObject STATIC_JSON_VENUE_FOOD_MENU= new JSONObject();
	public static JSONObject STATIC_JSON_VENUE_CATERING_MENU= new JSONObject();
	public static JSONObject STATIC_JSON_VENUE_BEVERAGES_MENU= new JSONObject();
	public static JSONObject STATIC_JSON_VENUE_SERVICE= new JSONObject();
	public static JSONObject STATIC_JSON_VENUE_EXTRA_SERVICE= new JSONObject();

	/*
	 * Cookie and SESSION
	 */
	// connection timeout is set to 30 seconds
	public static int TIMEOUT_CONNECTION = 30000;
	// SOCKET TIMEOUT IS SET TO 30 SECONDS
	public static int TIMEOUT_SOCKET = 30000;

	public static String PREF_SESSION_COOKIE = "sessionid";
	public static String SET_COOKIE_KEY = "Set-Cookie";
	public static String COOKIE_KEY = "Cookie";
	public static String SESSION_COOKIE = "sessionid";

	public static int API_SUCCESS = 0;
	public static int API_FAIL = 1;

	public static String API_RESULT_SUCCESS = "200";
	public static String API_RESULT_FAIL = "400";


	public static DecimalFormat GLOBAL_FORMATTER = new DecimalFormat("#,##0.00");

	/*** BACKEND VARIABLES */

	public static String SERVER_URL_ONLY ="http://13.56.92.252/"; //"http://54.153.127.215/api/";
	public static String SERVER_URL_API ="http://13.56.92.252/api/"; //"http://54.153.127.215/api/";
	public static String WEBSITE_PIC_URL = "https://www.ovenues.com/";// assets/images/";


	public static String AMAZON_PLACEHOLDER_IMAGE_URL = "https://s3-us-west-1.amazonaws.com/ovenues-placeholder-images/website-placeholdres/";

	public static String AMAZON_AMENITIES_IMAGE_URL ="https://s3-us-west-1.amazonaws.com/ovenues-amenities/orange/amenity_icon_";


	public static final String GOOGLE_PLACE_LOG_TAG = "Google Places Autocomplete";
	public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	public static final String OUT_JSON = "/json";
}
