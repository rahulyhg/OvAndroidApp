package ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel;

/**
 * Created by Jay-Andriod on 08-May-17.
 */

public class FoodMenuModel {

    String menu_id,courseID,menu_desc,total_courses ,price_per_plate,is_group_size ,group_size_from ,group_size_to;


    public FoodMenuModel(String menu_id, String menu_desc, String total_courses, String price_per_plate, String is_group_size, String group_size_from, String group_size_to) {
        this.menu_id = menu_id;
        this.menu_desc = menu_desc;
        this.total_courses = total_courses;
        this.price_per_plate = price_per_plate;
        this.is_group_size = is_group_size;
        this.group_size_from = group_size_from;
        this.group_size_to = group_size_to;
    }

    public FoodMenuModel(String menu_id, String courseID, String menu_desc, String total_courses, String price_per_plate, String is_group_size, String group_size_from, String group_size_to) {
        this.menu_id = menu_id;
        this.courseID = courseID;
        this.menu_desc = menu_desc;
        this.total_courses = total_courses;
        this.price_per_plate = price_per_plate;
        this.is_group_size = is_group_size;
        this.group_size_from = group_size_from;
        this.group_size_to = group_size_to;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getMenu_desc() {
        return menu_desc;
    }

    public void setMenu_desc(String menu_desc) {
        this.menu_desc = menu_desc;
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
}
