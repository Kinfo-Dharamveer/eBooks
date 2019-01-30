package com.kinfoitsolutions.ebooks.ui.model;

public class CategoryModel {

    Integer image;
    String title,qty;

    public CategoryModel(Integer image, String title, String qty) {
        this.image = image;
        this.title = title;
        this.qty = qty;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
