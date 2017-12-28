package ovenues.com.ovenue.modelpojo;

/**
 * Created by Jay-Andriod on 03-Aug-17.
 */

public class FinalBookingConfirmationModel {

    String type,booking_id ,vendor,cart_id,item_name,error;

    public FinalBookingConfirmationModel(String type, String booking_id, String vendor, String cart_id, String item_name, String error) {
        this.type = type;
        this.booking_id = booking_id;
        this.vendor = vendor;
        this.cart_id = cart_id;
        this.item_name = item_name;
        this.error = error;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
