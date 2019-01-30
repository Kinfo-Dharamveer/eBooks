package com.kinfoitsolutions.ebooks.ui.model;

public class AuthorsModel {

    private int auhtorimage;
    private String authorName;


    public AuthorsModel(int auhtorimage, String authorName) {
        this.auhtorimage = auhtorimage;
        this.authorName = authorName;
    }

    public int getAuhtorimage() {
        return auhtorimage;
    }

    public void setAuhtorimage(int auhtorimage) {
        this.auhtorimage = auhtorimage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
