package ovenues.com.ovenue.modelpojo.VenueOrderModel;

/**
 * Created by Jay-Andriod on 08-May-17.
 */

public class VenueOrderFoodMenuModel {

    String menu_id,menu_desc,item_id,item_name,total_courses ,price_per_plate;
    private int mType;

    public VenueOrderFoodMenuModel(String menu_id, String menu_desc, String item_id, String item_name, String total_courses, String price_per_plate, int mType) {
        this.menu_id = menu_id;
        this.menu_desc = menu_desc;
        this.item_id = item_id;
        this.item_name = item_name;
        this.total_courses = total_courses;
        this.price_per_plate = price_per_plate;
        this.mType = mType;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getMenu_desc() {
        return menu_desc;
    }

    public void setMenu_desc(String menu_desc) {
        this.menu_desc = menu_desc;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getTotal_courses() {
        return total_courses;
    }

    public void setTotal_courses(String total_courses) {
        this.total_courses = total_courses;
    }

    public String getPrice_per_plate() {
        return price_per_plate;
    }

    public void setPrice_per_plate(String price_per_plate) {
        this.price_per_plate = price_per_plate;
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }
}
