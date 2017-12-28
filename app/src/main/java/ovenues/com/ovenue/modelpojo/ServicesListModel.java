package ovenues.com.ovenue.modelpojo;

/**
 * Created by Testing on 18-Oct-16.
 */

public class ServicesListModel {
    String id,service_providerName,address,cityname,imag_url,is_registered,yelp_rating
            ,yelp_review_count ,yelp_price_range,yelp_id,cuisines;;

    public ServicesListModel(String id, String service_providerName, String address, String cityname, String imag_url, String is_registered, String yelp_rating, String yelp_review_count, String yelp_price_range, String yelp_id,String cuisines) {
        this.id = id;
        this.service_providerName = service_providerName;
        this.address = address;
        this.cityname = cityname;
        this.imag_url = imag_url;
        this.is_registered = is_registered;
        this.yelp_rating = yelp_rating;
        this.yelp_review_count = yelp_review_count;
        this.yelp_price_range = yelp_price_range;
        this.yelp_id = yelp_id;
        this.cuisines= cuisines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_providerName() {
        return service_providerName;
    }

    public void setService_providerName(String service_providerName) {
        this.service_providerName = service_providerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getImag_url() {
        return imag_url;
    }

    public void setImag_url(String imag_url) {
        this.imag_url = imag_url;
    }


    public String getIs_registered() {
        return is_registered;
    }

    public void setIs_registered(String is_registered) {
        this.is_registered = is_registered;
    }

    public String getYelp_rating() {
        return yelp_rating;
    }

    public void setYelp_rating(String yelp_rating) {
        this.yelp_rating = yelp_rating;
    }

    public String getYelp_review_count() {
        return yelp_review_count;
    }

    public void setYelp_review_count(String yelp_review_count) {
        this.yelp_review_count = yelp_review_count;
    }

    public String getYelp_price_range() {
        return yelp_price_range;
    }

    public void setYelp_price_range(String yelp_price_range) {
        this.yelp_price_range = yelp_price_range;
    }

    public String getYelp_id() {
        return yelp_id;
    }

    public void setYelp_id(String yelp_id) {
        this.yelp_id = yelp_id;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }
}