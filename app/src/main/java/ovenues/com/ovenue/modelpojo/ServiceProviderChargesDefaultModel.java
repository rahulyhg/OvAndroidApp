package ovenues.com.ovenue.modelpojo;

/**
 * Created by Jay-Andriod on 02-May-17.
 */

public class ServiceProviderChargesDefaultModel {

    String id, service_id, service_title, service_inclusions, charges, is_flat_charges, is_per_person_charges, is_per_item, pacakage_hours,
            hour_extension_charges, extension_duration, extra_person_charges, max_guest_extension, max_hour_extension, is_group_size, group_size_from, group_size_to, photo_url
            ,is_delivery,is_delivery_paid,delivery_charges ,is_pickup,is_onsite_service ,is_delivery_na;


    public ServiceProviderChargesDefaultModel
            (String id, String service_id, String service_title, String service_inclusions, String charges, String is_flat_charges, String is_per_person_charges, String is_per_item, String pacakage_hours,
             String hour_extension_charges, String extension_duration, String extra_person_charges, String max_guest_extension, String max_hour_extension, String is_group_size, String group_size_from,
             String group_size_to, String photo_url, String is_delivery, String is_delivery_paid, String delivery_charges, String is_pickup, String is_onsite_service, String is_delivery_na)
    {
        this.id = id;
        this.service_id = service_id;
        this.service_title = service_title;
        this.service_inclusions = service_inclusions;
        this.charges = charges;
        this.is_flat_charges = is_flat_charges;
        this.is_per_person_charges = is_per_person_charges;
        this.is_per_item = is_per_item;
        this.pacakage_hours = pacakage_hours;
        this.hour_extension_charges = hour_extension_charges;
        this.extension_duration = extension_duration;
        this.extra_person_charges = extra_person_charges;
        this.max_guest_extension = max_guest_extension;
        this.max_hour_extension = max_hour_extension;
        this.is_group_size = is_group_size;
        this.group_size_from = group_size_from;
        this.group_size_to = group_size_to;
        this.photo_url = photo_url;
        this.is_delivery = is_delivery;
        this.is_delivery_paid = is_delivery_paid;
        this.delivery_charges = delivery_charges;
        this.is_pickup = is_pickup;
        this.is_onsite_service = is_onsite_service;
        this.is_delivery_na = is_delivery_na;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_title() {
        return service_title;
    }

    public void setService_title(String service_title) {
        this.service_title = service_title;
    }

    public String getService_inclusions() {
        return service_inclusions;
    }

    public void setService_inclusions(String service_inclusions) {
        this.service_inclusions = service_inclusions;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
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

    public String getIs_per_item() {
        return is_per_item;
    }

    public void setIs_per_item(String is_per_item) {
        this.is_per_item = is_per_item;
    }

    public String getPacakage_hours() {
        return pacakage_hours;
    }

    public void setPacakage_hours(String pacakage_hours) {
        this.pacakage_hours = pacakage_hours;
    }

    public String getHour_extension_charges() {
        return hour_extension_charges;
    }

    public void setHour_extension_charges(String hour_extension_charges) {
        this.hour_extension_charges = hour_extension_charges;
    }

    public String getExtension_duration() {
        return extension_duration;
    }

    public void setExtension_duration(String extension_duration) {
        this.extension_duration = extension_duration;
    }

    public String getExtra_person_charges() {
        return extra_person_charges;
    }

    public void setExtra_person_charges(String extra_person_charges) {
        this.extra_person_charges = extra_person_charges;
    }

    public String getMax_guest_extension() {
        return max_guest_extension;
    }

    public void setMax_guest_extension(String max_guest_extension) {
        this.max_guest_extension = max_guest_extension;
    }

    public String getMax_hour_extension() {
        return max_hour_extension;
    }

    public void setMax_hour_extension(String max_hour_extension) {
        this.max_hour_extension = max_hour_extension;
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

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
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

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
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
}
