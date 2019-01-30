package com.kinfoitsolutions.ebooks.ui.model;

public class DownloadModelClass {

    Integer image;
    String title,author_name;


    public DownloadModelClass(Integer image, String title, String author_name) {
        this.image = image;
        this.title = title;
        this.author_name = author_name;
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

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
}
