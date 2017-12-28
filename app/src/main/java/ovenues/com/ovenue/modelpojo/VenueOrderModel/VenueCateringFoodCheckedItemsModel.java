package ovenues.com.ovenue.modelpojo.VenueOrderModel;

/**
 * Created by Jay-Andriod on 06-Jun-17.
 */

public class VenueCateringFoodCheckedItemsModel {

    String menu_id,course_id,item_id;

    public VenueCateringFoodCheckedItemsModel(String menu_id, String course_id, String item_id) {
        this.menu_id = menu_id;
        this.course_id = course_id;
        this.item_id = item_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
