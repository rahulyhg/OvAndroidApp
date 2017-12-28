package ovenues.com.ovenue.cart.CartSummaryServicesStickyList;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

import ovenues.com.ovenue.modelpojo.Cart.CartSummaryServiceListModel;

public final class HeaderItemCartSummaryServiceList extends CartSummaryServiceListModel implements StickyHeader {

    public HeaderItemCartSummaryServiceList(String cart_id, String user_id, String flag, String service_title, String service_provider_id, String provider_name, String booking_time, String booking_date, String service_hours, String number_of_guests, String quantity, String total_amount, String extra_guests, String extra_minutes, String note, String is_delivery, String is_pickup, String is_onsite_service, String delivery_address, int mUiType){

        super(cart_id,user_id,flag ,service_title,service_provider_id ,provider_name,booking_time ,booking_date,service_hours ,number_of_guests,quantity ,total_amount,extra_guests ,extra_minutes
                ,note,is_delivery,is_pickup ,is_onsite_service,delivery_address ,mUiType);
    }
}
