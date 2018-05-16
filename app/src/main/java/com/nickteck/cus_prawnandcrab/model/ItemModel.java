package com.nickteck.cus_prawnandcrab.model;


import java.util.ArrayList;

/**
 * Created by admin on 4/5/2018.
 */

public class ItemModel {

    ArrayList<ItemListRequestAndResponseModel.item_list>listArrayList = new ArrayList<>();
    boolean alreadyPlace = false;

    private static ItemModel ourInstance = new ItemModel();
    public static ItemModel getInstance() {
        return ourInstance;
    }

    public ArrayList<ItemListRequestAndResponseModel.item_list> getListArrayList() {
        return listArrayList;
    }

    public void setListArrayList(ArrayList<ItemListRequestAndResponseModel.item_list> listArrayList) {
        this.listArrayList = listArrayList;
    }

    public boolean isAlreadyPlace() {
        return alreadyPlace;
    }

    public void setAlreadyPlace(boolean alreadyPlace) {
        this.alreadyPlace = alreadyPlace;
    }
}
