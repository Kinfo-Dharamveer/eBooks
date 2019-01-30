package com.kinfoitsolutions.ebooks.ui.model.Getprofile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProfileResponse {


    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("user")
    @Expose
    private User user;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
