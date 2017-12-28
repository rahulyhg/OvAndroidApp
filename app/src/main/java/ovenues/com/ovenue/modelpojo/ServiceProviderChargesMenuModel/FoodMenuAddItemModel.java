package ovenues.com.ovenue.modelpojo.ServiceProviderChargesMenuModel;

import java.io.Serializable;

/**
 * Created by Jay-Andriod on 10-May-17.
 */

public class FoodMenuAddItemModel implements Serializable {


    String menu_id,menu_desc,course_id,course_title,item_id,item_name,is_additional_charges,price_per_item,max_item_selectn_allowed,price_per_plate,is_group_size ,group_size_from ,group_size_to;;

    private boolean isSelected;

    public FoodMenuAddItemModel() {
    }

    public FoodMenuAddItemModel(String menu_id, String menu_desc, String course_id, String course_title, String item_id, String item_name, String is_additional_charges, String price_per_item,
                                String max_item_selectn_allowed, String price_per_plate, String is_group_size, String group_size_from, String group_size_to, boolean isSelected) {
        this.menu_id = menu_id;
        this.menu_desc = menu_desc;
        this.course_id = course_id;
        this.course_title = course_title;
        this.item_id = item_id;
        this.item_name = item_name;
        this.is_additional_charges = is_additional_charges;
        this.price_per_item = price_per_item;
        this.max_item_selectn_allowed = max_item_selectn_allowed;
        this.price_per_plate = price_per_plate;
        this.is_group_size = is_group_size;
        this.group_size_from = group_size_from;
        this.group_size_to = group_size_to;
        this.isSelected = isSelected;
    }

    public String getMenu_desc() {
        return menu_desc;
    }

    public void setMenu_desc(String menu_desc) {
        this.menu_desc = menu_desc;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getPrice_per_item() {
        return price_per_item;
    }

    public void setPrice_per_item(String price_per_item) {
        this.price_per_item = price_per_item;
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

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getIs_additional_charges() {
        return is_additional_charges;
    }

    public void setIs_additional_charges(String is_additional_charges) {
        this.is_additional_charges = is_additional_charges;
    }

    public String getMax_item_selectn_allowed() {
        return max_item_selectn_allowed;
    }

    public void setMax_item_selectn_allowed(String max_item_selectn_allowed) {
        this.max_item_selectn_allowed = max_item_selectn_allowed;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
