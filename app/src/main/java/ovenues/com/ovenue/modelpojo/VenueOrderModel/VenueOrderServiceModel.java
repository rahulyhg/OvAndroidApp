package ovenues.com.ovenue.modelpojo.VenueOrderModel;

/**
 * Created by Jay-Andriod on 16-May-17.
 */

public class VenueOrderServiceModel {

    /*service type
    * 0 = default service
    * 1 = extra service*/

    String service_id, service_desc, service_name, service_option_id, optiondesc, option_charges, option_details, is_flat_charges, is_per_person_charges, is_per_hour_charges, is_hour_extn_changes, extension_charges, is_group_size, group_size_from,
            group_size_to, serviceType;

    /*=====mUItype * 0 = header * 1 = body */
    private int mUiType;
    boolean isSelected;

    public VenueOrderServiceModel(String service_id, String service_desc, String service_name, String service_option_id, String optiondesc, String option_charges, String option_details,
                                  String is_flat_charges, String is_per_person_charges, String is_per_hour_charges, String is_hour_extn_changes, String extension_charges, String is_group_size,
                                  String group_size_from, String group_size_to, String serviceType, int mUiType,boolean isSelected) {
        this.service_id = service_id;
        this.service_desc = service_desc;
        this.service_name = service_name;
        this.service_option_id = service_option_id;
        this.optiondesc = optiondesc;
        this.option_charges = option_charges;
        this.option_details = option_details;
        this.is_flat_charges = is_flat_charges;
        this.is_per_person_charges = is_per_person_charges;
        this.is_per_hour_charges = is_per_hour_charges;
        this.is_hour_extn_changes = is_hour_extn_changes;
        this.extension_charges = extension_charges;
        this.is_group_size = is_group_size;
        this.group_size_from = group_size_from;
        this.group_size_to = group_size_to;
        this.serviceType = serviceType;
        this.mUiType = mUiType;
        this.isSelected = isSelected;
    }


    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_desc() {
        return service_desc;
    }

    public void setService_desc(String service_desc) {
        this.service_desc = service_desc;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_option_id() {
        return service_option_id;
    }

    public void setService_option_id(String service_option_id) {
        this.service_option_id = service_option_id;
    }

    public String getOptiondesc() {
        return optiondesc;
    }

    public void setOptiondesc(String optiondesc) {
        this.optiondesc = optiondesc;
    }

    public String getOption_charges() {
        return option_charges;
    }

    public void setOption_charges(String option_charges) {
        this.option_charges = option_charges;
    }

    public String getOption_details() {
        return option_details;
    }

    public void setOption_details(String option_details) {
        this.option_details = option_details;
    }

    public String getIs_flat_charges() {
        return is_flat_charges;
    }

    public void setIs_flat_charges(String is_flat_charges) {
        this.is_flat_charges = is_flat_charges;
    }

    public String getIs_per_person_charges() {
        return is_per_person_charges;
    }

    public void setIs_per_person_charges(String is_per_person_charges) {
        this.is_per_person_charges = is_per_person_charges;
    }

    public String getIs_per_hour_charges() {
        return is_per_hour_charges;
    }

    public void setIs_per_hour_charges(String is_per_hour_charges) {
        this.is_per_hour_charges = is_per_hour_charges;
    }

    public String getIs_hour_extn_changes() {
        return is_hour_extn_changes;
    }

    public void setIs_hour_extn_changes(String is_hour_extn_changes) {
        this.is_hour_extn_changes = is_hour_extn_changes;
    }

    public String getExtension_charges() {
        return extension_charges;
    }

    public void setExtension_charges(String extension_charges) {
        this.extension_charges = extension_charges;
    }

    public String getIs_group_size() {
        return is_group_size;
    }

    public void setIs_group_size(String is_group_size) {
        this.is_group_size = is_group_size;
    }

    public String getGroup_size_from() {
        return group_size_from;
    }

    public void setGroup_size_from(String group_size_from) {
        this.group_size_from = group_size_from;
    }

    public String getGroup_size_to() {
        return group_size_to;
    }

    public void setGroup_size_to(String group_size_to) {
        this.group_size_to = group_size_to;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getmUiType() {
        return mUiType;
    }

    public void setmUiType(int mUiType) {
        this.mUiType = mUiType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}