package com.example.sanskarmodi.uberarts.model;

public class DashboardItem {

    private String title;
    private String desc;
    private int image;

    public DashboardItem(String title, String desc, int image) {
        this.title = title;
        this.desc = desc;
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
