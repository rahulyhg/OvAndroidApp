package ovenues.com.ovenue.bookingdetails;

import com.google.gson.JsonArray;

/**
 * Created by Jay-Andriod on 12-Jul-17.
 */

public class BookingServicesModel {


   String  booking_type,booking_type_icon ,booking_id,booked_on ,booking_date,booking_time ,service_hours,booking_status ,number_of_guests,quantity ,total_amount,coupon_id ,discount_amount,service_id ,note,service_provider_id ,provider_name,is_delivery ,is_delivery_paid,is_pickup ,is_onsite_service,is_delivery_na ,delivery_address,service_title;

    //====IF BOOKING TYPE = 3 THEN AND THEN ONLY THIS ARRAY VALUES IN MODEL OTHREWISE IT WILL BE ALWAYS NULL=====
    JsonArray catering_price,restaurant_food_price ,baverage_price,services ,extra_services;

    public BookingServicesModel(String booking_type, String booking_type_icon, String booking_id, String booked_on, String booking_date, String booking_time, String service_hours, String booking_status, String number_of_guests, String quantity, String total_amount, String coupon_id, String discount_amount, String service_id, String note, String service_provider_id, String provider_name, String is_delivery, String is_delivery_paid, String is_pickup, String is_onsite_service, String is_delivery_na, String delivery_address, String service_title, JsonArray catering_price, JsonArray restaurant_food_price, JsonArray baverage_price, JsonArray services, JsonArray extra_services) {
        this.booking_type = booking_type;
        this.booking_type_icon = booking_type_icon;
        this.booking_id = booking_id;
        this.booked_on = booked_on;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
        this.service_hours = service_hours;
        this.booking_status = booking_status;
        this.number_of_guests = number_of_guests;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.coupon_id = coupon_id;
        this.discount_amount = discount_amount;
        this.service_id = service_id;
        this.note = note;
        this.service_provider_id = service_provider_id;
        this.provider_name = provider_name;
        this.is_delivery = is_delivery;
        this.is_delivery_paid = is_delivery_paid;
        this.is_pickup = is_pickup;
        this.is_onsite_service = is_onsite_service;
        this.is_delivery_na = is_delivery_na;
        this.delivery_address = delivery_address;
        this.service_title = service_title;
        this.catering_price = catering_price;
        this.restaurant_food_price = restaurant_food_price;
        this.baverage_price = baverage_price;
        this.services = services;
        this.extra_services = extra_services;
    }

    public String getBooking_type() {
        return booking_type;
    }

    public void setBooking_type(String booking_type) {
        this.booking_type = booking_type;
    }

    public String getBooking_type_icon() {
        return booking_type_icon;
    }

    public void setBooking_type_icon(String booking_type_icon) {
        this.booking_type_icon = booking_type_icon;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooked_on() {
        return booked_on;
    }

    public void setBooked_on(String booked_on) {
        this.booked_on = booked_on;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getService_hours() {
        return service_hours;
    }

    public void setService_hours(String service_hours) {
        this.service_hours = service_hours;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getNumber_of_guests() {
        return number_of_guests;
    }

    public void setNumber_of_guests(String number_of_guests) {
        this.number_of_guests = number_of_guests;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getService_provider_id() {
        return service_provider_id;
    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(String is_delivery) {
        this.is_delivery = is_delivery;
    }

    public String getIs_delivery_paid() {
        return is_delivery_paid;
    }

    public void setIs_delivery_paid(String is_delivery_paid) {
        this.is_delivery_paid = is_delivery_paid;
    }

    public String getIs_pickup() {
        return is_pickup;
    }

    public void setIs_pickup(String is_pickup) {
        this.is_pickup = is_pickup;
    }

    public String getIs_onsite_service() {
        return is_onsite_service;
    }

    public void setIs_onsite_service(String is_onsite_service) {
        this.is_onsite_service = is_onsite_service;
    }

    public String getIs_delivery_na() {
        return is_delivery_na;
    }

    public void setIs_delivery_na(String is_delivery_na) {
        this.is_delivery_na = is_delivery_na;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
    }

    public JsonArray getCatering_price() {
        return catering_price;
    }

    public void setCatering_price(JsonArray catering_price) {
        this.catering_price = catering_price;
    }

    public JsonArray getRestaurant_food_price() {
        return restaurant_food_price;
    }

    public void setRestaurant_food_price(JsonArray restaurant_food_price) {
        this.restaurant_food_price = restaurant_food_price;
    }

    public JsonArray getBaverage_price() {
        return baverage_price;
    }

    public void setBaverage_price(JsonArray baverage_price) {
        this.baverage_price = baverage_price;
    }

    public JsonArray getServices() {
        return services;
    }

    public void setServices(JsonArray services) {
        this.services = services;
    }

    public JsonArray getExtra_services() {
        return extra_services;
    }

    public void setExtra_services(JsonArray extra_services) {
        this.extra_services = extra_services;
    }
}
