package com.quyet.banhang.app_banhang.model;

public class TheLoai {
    private String title;
    private String image;

    public TheLoai(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public TheLoai() {
    }
}
