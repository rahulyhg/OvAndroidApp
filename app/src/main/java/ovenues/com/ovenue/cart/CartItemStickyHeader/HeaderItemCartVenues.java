package ovenues.com.ovenue.cart.CartItemStickyHeader;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;
import com.google.gson.JsonArray;

import ovenues.com.ovenue.modelpojo.Cart.CartItemsModel;

public final class HeaderItemCartVenues extends CartItemsModel implements StickyHeader {

    public HeaderItemCartVenues(String cart_id, String flag, String venue_id, String booking_date, String booking_time_from, String booking_time_to, String number_of_guests, String booking_rent, String venue_name, String mVenueOrdCatType, String id1, String id2, String id3, String name, String count, String item_price, String total_itemprice, String is_delivery, String is_pickup, String delivery_address, JsonArray booking_food_menu, JsonArray booking_food, JsonArray booking_beverages, JsonArray booking_services, JsonArray booking_extra_services, int mUiType){

        super( cart_id,  flag,  venue_id,  booking_date,  booking_time_from,  booking_time_to,  number_of_guests,  booking_rent,  venue_name,  mVenueOrdCatType,  id1,  id2,  id3,  name,  count,  item_price,  total_itemprice,is_delivery, is_pickup, delivery_address,  booking_food_menu,  booking_food,  booking_beverages,  booking_services,  booking_extra_services,  mUiType);
    }
}
