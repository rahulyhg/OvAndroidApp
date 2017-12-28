package ovenues.com.ovenue.modelpojo.Cart;

/**
 * Created by Jay-Andriod on 16-Jun-17.
 */

public class CartSummaryServiceListModel {

    String cart_id,user_id,flag ,service_title,service_provider_id ,provider_name,booking_time ,booking_date,service_hours ,number_of_guests,quantity ,total_amount,extra_guests ,extra_minutes
            ,note,is_delivery,is_pickup ,is_onsite_service,delivery_address;

    /*=====mUItype * 0 = header * 1 = body */
    private int mUiType;

    public CartSummaryServiceListModel(String cart_id, String user_id, String flag, String service_title, String service_provider_id, String provider_name, String booking_time, String booking_date, String service_hours, String number_of_guests, String quantity, String total_amount, String extra_guests, String extra_minutes, String note, String is_delivery, String is_pickup, String is_onsite_service, String delivery_address, int mUiType) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.flag = flag;
        this.service_title = service_title;
        this.service_provider_id = service_provider_id;
        this.provider_name = provider_name;
        this.booking_time = booking_time;
        this.booking_date = booking_date;
        this.service_hours = service_hours;
        this.number_of_guests = number_of_guests;
        this.quantity = quantity;
        this.total_amount = total_amount;
        this.extra_guests = extra_guests;
        this.extra_minutes = extra_minutes;
        this.note = note;
        this.is_delivery = is_delivery;
        this.is_pickup = is_pickup;
        this.is_onsite_service = is_onsite_service;
        this.delivery_address = delivery_address;
        this.mUiType = mUiType;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
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

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getService_hours() {
        return service_hours;
    }

    public void setService_hours(String service_hours) {
        this.service_hours = service_hours;
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

    public String getExtra_guests() {
        return extra_guests;
    }

    public void setExtra_guests(String extra_guests) {
        this.extra_guests = extra_guests;
    }

    public String getExtra_minutes() {
        return extra_minutes;
    }

    public void setExtra_minutes(String extra_minutes) {
        this.extra_minutes = extra_minutes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getIs_onsite_service() {
        return is_onsite_service;
    }

    public void setIs_onsite_service(String is_onsite_service) {
        this.is_onsite_service = is_onsite_service;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public int getmUiType() {
        return mUiType;
    }

    public void setmUiType(int mUiType) {
        this.mUiType = mUiType;
    }
}
