package com.kinfoitsolutions.ebooks.ui.responsemodel.SearchAuthors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchAuthorsSuccess {


    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private List<SearchAuthorsPayload> data = null;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<SearchAuthorsPayload> getData() {
        return data;
    }

    public void setData(List<SearchAuthorsPayload> data) {
        this.data = data;
    }
}
