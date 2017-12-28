package ovenues.com.ovenue.modelpojo.VenueOrderModel;

/**
 * Created by Jay-Andriod on 15-May-17.
 */

public class VenueOrderBeveragesModel {

    String beverage_id,venue_id,option_desc ,option_charges,is_flat_charges ,is_per_person_charges,is_per_hour_charges ,is_hour_extn_changes,extension_charges ,is_group_size,group_size_from ,group_size_to;
    private boolean isSelected;
    String qty;

    public VenueOrderBeveragesModel(String beverage_id, String venue_id, String option_desc, String option_charges, String is_flat_charges, String is_per_person_charges, String is_per_hour_charges,
                                    String is_hour_extn_changes, String extension_charges, String is_group_size, String group_size_from, String group_size_to, boolean isSelected) {
        this.beverage_id = beverage_id;
        this.venue_id = venue_id;
        this.option_desc = option_desc;
        this.option_charges = option_charges;
        this.is_flat_charges = is_flat_charges;
        this.is_per_person_charges = is_per_person_charges;
        this.is_per_hour_charges = is_per_hour_charges;
        this.is_hour_extn_changes = is_hour_extn_changes;
        this.extension_charges = extension_charges;
        this.is_group_size = is_group_size;
        this.group_size_from = group_size_from;
        this.group_size_to = group_size_to;
        this.isSelected = isSelected;
    }

    public String getBeverage_id() {
        return beverage_id;
    }

    public void setBeverage_id(String beverage_id) {
        this.beverage_id = beverage_id;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getOption_desc() {
        return option_desc;
    }

    public void setOption_desc(String option_desc) {
        this.option_desc = option_desc;
    }

    public String getOption_charges() {
        return option_charges;
    }

    public void setOption_charges(String option_charges) {
        this.option_charges = option_charges;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
