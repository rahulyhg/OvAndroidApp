package movile.com.creditcardguide.autocompleteSearchviewCity;

/**
 * Created by Jay-Andriod on 13-Apr-17.
 */

public class SearchCityModel {


    String id,name,county;

    public SearchCityModel(String id, String name, String county) {
        this.id = id;
        this.name = name;
        this.county = county;
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
}
