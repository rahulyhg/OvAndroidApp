package ovenues.com.ovenue.modelpojo.Spiners;

/**
 * Created by Jay-Andriod on 07-Apr-17.
 */
public class SearchVenueSpiners {

    public String id , type;

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

    public SearchVenueSpiners(String id, String type) {
        this.id = id;
        this.type = type;
    }
}
