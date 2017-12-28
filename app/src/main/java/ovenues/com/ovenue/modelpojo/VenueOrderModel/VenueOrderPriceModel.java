package ovenues.com.ovenue.modelpojo.VenueOrderModel;

/**
 * Created by Jay-Andriod on 11-May-17.
 */

public class VenueOrderPriceModel {


    String id,package_name,charges,week_days,time_from ,time_to ,is_flat_charges,is_perperson_charges , is_group_charges,group_size_from,group_size_to,is_extra_person_charges ,extra_person_charges,chrages_inclusion ,
            is_perhour_charges,pacakage_hours ,is_hour_extension_charges,hour_extension_charges ,extension_hours,is_applicable;

    private boolean isSelected;


    public VenueOrderPriceModel(String id, String package_name, String charges, String  week_days,String time_from,String time_to, String is_flat_charges, String is_perperson_charges, String is_group_charges, String group_size_from,
                                String group_size_to, String is_extra_person_charges, String extra_person_charges, String chrages_inclusion, String is_perhour_charges, String pacakage_hours,
                                String is_hour_extension_charges, String hour_extension_charges, String extension_hours, String is_applicable, boolean isSelected) {
        this.id = id;
        this.package_name = package_name;
        this.charges = charges;
        this.week_days = week_days;
        this.time_from = time_from;
        this.time_to = time_to;
        this.is_flat_charges = is_flat_charges;
        this.is_perperson_charges = is_perperson_charges;
        this.is_group_charges = is_group_charges;
        this.group_size_from = group_size_from;
        this.group_size_to = group_size_to;
        this.is_extra_person_charges = is_extra_person_charges;
        this.extra_person_charges = extra_person_charges;
        this.chrages_inclusion = chrages_inclusion;
        this.is_perhour_charges = is_perhour_charges;
        this.pacakage_hours = pacakage_hours;
        this.is_hour_extension_charges = is_hour_extension_charges;
        this.hour_extension_charges = hour_extension_charges;
        this.extension_hours = extension_hours;
        this.is_applicable = is_applicable;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getWeek_days() {
        return week_days;
    }

    public void setWeek_days(String week_days) {
        this.week_days = week_days;
    }

    public String getTime_from() {
        return time_from;
    }

    public void setTime_from(String time_from) {
        this.time_from = time_from;
    }

    public String getTime_to() {
        return time_to;
    }

    public void setTime_to(String time_to) {
        this.time_to = time_to;
    }

    public String getIs_flat_charges() {
        return is_flat_charges;
    }

    public void setIs_flat_charges(String is_flat_charges) {
        this.is_flat_charges = is_flat_charges;
    }

    public String getIs_perperson_charges() {
        return is_perperson_charges;
    }

    public void setIs_perperson_charges(String is_perperson_charges) {
        this.is_perperson_charges = is_perperson_charges;
    }

    public String getIs_group_charges() {
        return is_group_charges;
    }

    public void setIs_group_charges(String is_group_charges) {
        this.is_group_charges = is_group_charges;
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

    public String getIs_extra_person_charges() {
        return is_extra_person_charges;
    }

    public void setIs_extra_person_charges(String is_extra_person_charges) {
        this.is_extra_person_charges = is_extra_person_charges;
    }

    public String getExtra_person_charges() {
        return extra_person_charges;
    }

    public void setExtra_person_charges(String extra_person_charges) {
        this.extra_person_charges = extra_person_charges;
    }

    public String getChrages_inclusion() {
        return chrages_inclusion;
    }

    public void setChrages_inclusion(String chrages_inclusion) {
        this.chrages_inclusion = chrages_inclusion;
    }

    public String getIs_perhour_charges() {
        return is_perhour_charges;
    }

    public void setIs_perhour_charges(String is_perhour_charges) {
        this.is_perhour_charges = is_perhour_charges;
    }

    public String getPacakage_hours() {
        return pacakage_hours;
    }

    public void setPacakage_hours(String pacakage_hours) {
        this.pacakage_hours = pacakage_hours;
    }

    public String getIs_hour_extension_charges() {
        return is_hour_extension_charges;
    }

    public void setIs_hour_extension_charges(String is_hour_extension_charges) {
        this.is_hour_extension_charges = is_hour_extension_charges;
    }

    public String getHour_extension_charges() {
        return hour_extension_charges;
    }

    public void setHour_extension_charges(String hour_extension_charges) {
        this.hour_extension_charges = hour_extension_charges;
    }

    public String getExtension_hours() {
        return extension_hours;
    }

    public void setExtension_hours(String extension_hours) {
        this.extension_hours = extension_hours;
    }

    public String getIs_applicable() {
        return is_applicable;
    }

    public void setIs_applicable(String is_applicable) {
        this.is_applicable = is_applicable;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
