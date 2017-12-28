package ovenues.com.ovenue.modelpojo;

/**
 * Created by Jay-Andriod on 18-Aug-17.
 */

public class ServiceproviderCateringPricingMenuModel {


    String id,title,flag;
    boolean isVenue; //flag 0= venues , 1 = food menui,2 = resturant menu,3= beverages

    public ServiceproviderCateringPricingMenuModel(String id, String title, String flag, boolean isVenue) {
        this.id = id;
        this.title = title;
        this.flag = flag;
        this.isVenue = isVenue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isVenue() {
        return isVenue;
    }

    public void setVenue(boolean venue) {
        isVenue = venue;
    }
}
