package com.projects050414.myapplication3.app;

/**
 * Created by obelix on 02/05/2014.
 */
public class NavdrawerItem {

    String ItemName;
    int imgResID;

    public NavdrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public String getItemName() {
        return ItemName;
    }
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    public int getImgResID() {
        return imgResID;
    }
    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

}
