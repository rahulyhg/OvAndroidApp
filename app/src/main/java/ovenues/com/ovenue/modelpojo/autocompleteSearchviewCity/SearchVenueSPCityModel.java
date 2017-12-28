package ovenues.com.ovenue.modelpojo.autocompleteSearchviewCity;

/**
 * Created by Jay-Andriod on 13-Apr-17.
 */

public class SearchVenueSPCityModel {


    String id,name,county,group,serviceID,ServiceName,svg_icon_url;


    public SearchVenueSPCityModel(String id, String name, String county, String group,String serviceID,String ServiceName,String svg_icon_url) {
        this.id = id;
        this.name = name;
        this.county = county;
        this.group = group;
        this.serviceID =serviceID;
        this.ServiceName = ServiceName;
        this.svg_icon_url = svg_icon_url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getSvg_icon_url() {
        return svg_icon_url;
    }

    public void setSvg_icon_url(String svg_icon_url) {
        this.svg_icon_url = svg_icon_url;
    }
}
