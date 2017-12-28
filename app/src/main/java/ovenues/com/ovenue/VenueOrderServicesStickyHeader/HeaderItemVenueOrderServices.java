package ovenues.com.ovenue.VenueOrderServicesStickyHeader;

import com.brandongogetap.stickyheaders.exposed.StickyHeader;

import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderFoodMenuModel;
import ovenues.com.ovenue.modelpojo.VenueOrderModel.VenueOrderServiceModel;

public final class HeaderItemVenueOrderServices extends VenueOrderServiceModel implements StickyHeader {

    public HeaderItemVenueOrderServices(String service_id,String service_desc, String service_name, String service_option_id, String optiondesc, String option_charges, String option_details, String is_flat_charges,
                                        String is_per_person_charges, String is_per_hour_charges, String is_hour_extn_changes, String extension_charges, String is_group_size, String group_size_from,
                                        String group_size_to, String serviceType, int mUiType,boolean isSelected){
    super(service_id,service_desc,service_name,service_option_id,optiondesc,option_charges ,option_details,is_flat_charges ,is_per_person_charges,is_per_hour_charges ,is_hour_extn_changes,extension_charges ,is_group_size,
            group_size_from , group_size_to ,serviceType ,mUiType,isSelected);
    }
}
