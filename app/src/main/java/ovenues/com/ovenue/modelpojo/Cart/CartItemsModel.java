package ovenues.com.ovenue.modelpojo.Cart;

import com.google.gson.JsonArray;

/**
 * Created by Jay-Andriod on 06-Jun-17.
 */

public class CartItemsModel {

    String cart_id,flag ,venue_id,booking_date,booking_time_from ,booking_time_to,number_of_guests ,booking_rent,venue_name,mVenueOrdCatType,id1,id2,id3,name,count,item_price,total_itemprice,is_delivery,is_pickup,delivery_address;
    JsonArray booking_food_menu,booking_food ,booking_beverages,booking_services,booking_extra_services;

    int mUiType;
    /* mVenueOrdCatType
      1=pricingplan;2=food;3=catering;4=beverage;5=service;6=extra_service*/
    /*id types =
      * id1=menu_id; id2 = course_id;  id3=item_id(Generic ID);*/

    public CartItemsModel(String cart_id, String flag, String venue_id, String booking_date, String booking_time_from, String booking_time_to, String number_of_guests, String booking_rent, String venue_name, String mVenueOrdCatType, String id1, String id2, String id3, String name, String count, String item_price, String total_itemprice, String is_delivery, String is_pickup, String delivery_address, JsonArray booking_food_menu, JsonArray booking_food, JsonArray booking_beverages, JsonArray booking_services, JsonArray booking_extra_services, int mUiType) {
        this.cart_id = cart_id;
        this.flag = flag;
        this.venue_id = venue_id;
        this.booking_date = booking_date;
        this.booking_time_from = booking_time_from;
        this.booking_time_to = booking_time_to;
        this.number_of_guests = number_of_guests;
        this.booking_rent = booking_rent;
        this.venue_name = venue_name;
        this.mVenueOrdCatType = mVenueOrdCatType;
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.name = name;
        this.count = count;
        this.item_price = item_price;
        this.total_itemprice = total_itemprice;
        this.is_delivery = is_delivery;
        this.is_pickup = is_pickup;
        this.delivery_address = delivery_address;
        this.booking_food_menu = booking_food_menu;
        this.booking_food = booking_food;
        this.booking_beverages = booking_beverages;
        this.booking_services = booking_services;
        this.booking_extra_services = booking_extra_services;
        this.mUiType = mUiType;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time_from() {
        return booking_time_from;
    }

    public void setBooking_time_from(String booking_time_from) {
        this.booking_time_from = booking_time_from;
    }

    public String getBooking_time_to() {
        return booking_time_to;
    }

    public void setBooking_time_to(String booking_time_to) {
        this.booking_time_to = booking_time_to;
    }

    public String getNumber_of_guests() {
        return number_of_guests;
    }

    public void setNumber_of_guests(String number_of_guests) {
        this.number_of_guests = number_of_guests;
    }

    public String getBooking_rent() {
        return booking_rent;
    }

    public void setBooking_rent(String booking_rent) {
        this.booking_rent = booking_rent;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getmVenueOrdCatType() {
        return mVenueOrdCatType;
    }

    public void setmVenueOrdCatType(String mVenueOrdCatType) {
        this.mVenueOrdCatType = mVenueOrdCatType;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getTotal_itemprice() {
        return total_itemprice;
    }

    public void setTotal_itemprice(String total_itemprice) {
        this.total_itemprice = total_itemprice;
    }

    public String getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(String is_delivery) {
        this.is_delivery = is_delivery;
    }

    public String getIs_pickup() {
        return is_pickup;
    }

    public void setIs_pickup(String is_pickup) {
        this.is_pickup = is_pickup;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public JsonArray getBooking_food_menu() {
        return booking_food_menu;
    }

    public void setBooking_food_menu(JsonArray booking_food_menu) {
        this.booking_food_menu = booking_food_menu;
    }

    public JsonArray getBooking_food() {
        return booking_food;
    }

    public void setBooking_food(JsonArray booking_food) {
        this.booking_food = booking_food;
    }

    public JsonArray getBooking_beverages() {
        return booking_beverages;
    }

    public void setBooking_beverages(JsonArray booking_beverages) {
        this.booking_beverages = booking_beverages;
    }


    public JsonArray getBooking_services() {
        return booking_services;
    }

    public void setBooking_services(JsonArray booking_services) {
        this.booking_services = booking_services;
    }

    public JsonArray getBooking_extra_services() {
        return booking_extra_services;
    }

    public void setBooking_extra_services(JsonArray booking_extra_services) {
        this.booking_extra_services = booking_extra_services;
    }

    public int getmUiType() {
        return mUiType;
    }

    public void setmUiType(int mUiType) {
        this.mUiType = mUiType;
    }
}
