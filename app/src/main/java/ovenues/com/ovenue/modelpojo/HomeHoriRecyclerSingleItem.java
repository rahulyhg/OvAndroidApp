package ovenues.com.ovenue.modelpojo;

import com.google.gson.JsonObject;

import org.json.JSONObject;

public class HomeHoriRecyclerSingleItem {


    private String name;
    private String url,url2;
    private String id;
    private String type;
    private boolean is_selected;

    private String service_id,photourl;
    private JsonObject jsonObj;

    public HomeHoriRecyclerSingleItem() {
    }

    public HomeHoriRecyclerSingleItem(String name, String url, String id,String type) {
   // THIS IS USED FOR VENUE LIST PAGE SEVICE ICON DISPLAY
        this.name = name;
        this.url = url;
        this.id = id;
        this.type=type;
    }

    public HomeHoriRecyclerSingleItem(String name, String url, String url2, String id, String type, boolean is_selected) {
        this.name = name;
        this.url = url;
        this.url2 = url2;
        this.id = id;
        this.type = type;
        this.is_selected = is_selected;
    }

    public HomeHoriRecyclerSingleItem(String name, String url, String id, String type, String service_id, String photourl, JsonObject jsonObj) {
        // THIS IS FOR TOP-FIVE LIST OF HOME SCREEN VENDOR DETAILS json IS IMPORTANT HERE.
        this.name = name;
        this.url = url;
        this.id = id;
        this.type = type;
        this.service_id = service_id;
        this.photourl = photourl;
        this.jsonObj = jsonObj;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public JsonObject getJsonObj() {
        return jsonObj;
    }

    public void setJsonObj(JsonObject jsonObj) {
        this.jsonObj = jsonObj;
    }
}
