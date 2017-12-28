package ovenues.com.ovenue.VenueOrderFoodStickyHeader;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderFoodMenuModel;

public final class HeaderItem extends VenueOrderFoodMenuModel implements StickyHeader {

    public HeaderItem(String menu_id, String menu_desc, String item_id, String item_name, String total_courses, String price_per_plate, int mType) {
        super(menu_id,menu_desc,item_id,item_name,total_courses ,price_per_plate,mType);
    }
}
