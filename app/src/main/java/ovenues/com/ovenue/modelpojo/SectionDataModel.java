package ovenues.com.ovenue.modelpojo;

import java.util.ArrayList;

public class SectionDataModel {



    private String headerTitle;
    private ArrayList<HomeHoriRecyclerSingleItem> allItemsInSection;


    public SectionDataModel() {

    }
    public SectionDataModel(String headerTitle, ArrayList<HomeHoriRecyclerSingleItem> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }



    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<HomeHoriRecyclerSingleItem> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<HomeHoriRecyclerSingleItem> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }


}
