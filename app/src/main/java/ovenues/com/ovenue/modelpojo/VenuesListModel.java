package ovenues.com.ovenue.modelpojo;

/**
 * Created by Testing on 18-Oct-16.
 */

public class VenuesListModel {
    String id, venue_id, city_id, address1, address2, statae, zipcode, venue_type_id, max_occupancy, min_occupancy, time_extention_allowed, t_and_c, is_active, security_deposit,
            venue_name, average_cost, cost_type,is_request_quote,package_hours, photo_url, city_name, services, fav_count, is_favourite,is_registered,yelp_rating
            ,yelp_review_count ,yelp_price_range,yelp_id;

    public VenuesListModel(String id, String venue_id, String city_id, String address1, String address2, String statae, String zipcode, String venue_type_id, String max_occupancy, String min_occupancy, String time_extention_allowed, String t_and_c, String is_active, String security_deposit, String venue_name, String average_cost, String cost_type, String is_request_quote, String package_hours, String photo_url, String city_name, String services, String fav_count, String is_favourite, String is_registered, String yelp_rating, String yelp_review_count, String yelp_price_range, String yelp_id) {
        this.id = id;
        this.venue_id = venue_id;
        this.city_id = city_id;
        this.address1 = address1;
        this.address2 = address2;
        this.statae = statae;
        this.zipcode = zipcode;
        this.venue_type_id = venue_type_id;
        this.max_occupancy = max_occupancy;
        this.min_occupancy = min_occupancy;
        this.time_extention_allowed = time_extention_allowed;
        this.t_and_c = t_and_c;
        this.is_active = is_active;
        this.security_deposit = security_deposit;
        this.venue_name = venue_name;
        this.average_cost = average_cost;
        this.cost_type = cost_type;
        this.is_request_quote = is_request_quote;
        this.package_hours = package_hours;
        this.photo_url = photo_url;
        this.city_name = city_name;
        this.services = services;
        this.fav_count = fav_count;
        this.is_favourite = is_favourite;
        this.is_registered = is_registered;
        this.yelp_rating = yelp_rating;
        this.yelp_review_count = yelp_review_count;
        this.yelp_price_range = yelp_price_range;
        this.yelp_id = yelp_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVenue_id() {
        return venue_id;
    }

    public void setVenue_id(String venue_id) {
        this.venue_id = venue_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getStatae() {
        return statae;
    }

    public void setStatae(String statae) {
        this.statae = statae;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getVenue_type_id() {
        return venue_type_id;
    }

    public void setVenue_type_id(String venue_type_id) {
        this.venue_type_id = venue_type_id;
    }

    public String getMax_occupancy() {
        return max_occupancy;
    }

    public void setMax_occupancy(String max_occupancy) {
        this.max_occupancy = max_occupancy;
    }

    public String getMin_occupancy() {
        return min_occupancy;
    }

    public void setMin_occupancy(String min_occupancy) {
        this.min_occupancy = min_occupancy;
    }

    public String getTime_extention_allowed() {
        return time_extention_allowed;
    }

    public void setTime_extention_allowed(String time_extention_allowed) {
        this.time_extention_allowed = time_extention_allowed;
    }

    public String getT_and_c() {
        return t_and_c;
    }

    public void setT_and_c(String t_and_c) {
        this.t_and_c = t_and_c;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getSecurity_deposit() {
        return security_deposit;
    }

    public void setSecurity_deposit(String security_deposit) {
        this.security_deposit = security_deposit;
    }

    public String getVenue_name() {
        return venue_name;
    }

    public void setVenue_name(String venue_name) {
        this.venue_name = venue_name;
    }

    public String getAverage_cost() {
        return average_cost;
    }

    public void setAverage_cost(String average_cost) {
        this.average_cost = average_cost;
    }

    public String getCost_type() {
        return cost_type;
    }

    public void setCost_type(String cost_type) {
        this.cost_type = cost_type;
    }

    public String getIs_request_quote() {
        return is_request_quote;
    }

    public void setIs_request_quote(String is_request_quote) {
        this.is_request_quote = is_request_quote;
    }

    public String getPackage_hours() {
        return package_hours;
    }

    public void setPackage_hours(String package_hours) {
        this.package_hours = package_hours;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getFav_count() {
        return fav_count;
    }

    public void setFav_count(String fav_count) {
        this.fav_count = fav_count;
    }

    public String getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(String is_favourite) {
        this.is_favourite = is_favourite;
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
}