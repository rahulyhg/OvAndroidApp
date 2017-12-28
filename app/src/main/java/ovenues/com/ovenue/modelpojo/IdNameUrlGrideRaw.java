package ovenues.com.ovenue.modelpojo;

/**
 * Created by Shanni on 11/5/2016.
 */

public class IdNameUrlGrideRaw {

    String id,title,url,description,address,is_venue,service_id;

    public IdNameUrlGrideRaw(String id, String title, String url, String description, String address, String is_venue, String service_id) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.address = address;
        this.is_venue = is_venue;
        this.service_id = service_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIs_venue() {
        return is_venue;
    }

    public void setIs_venue(String is_venue) {
        this.is_venue = is_venue;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }
}
