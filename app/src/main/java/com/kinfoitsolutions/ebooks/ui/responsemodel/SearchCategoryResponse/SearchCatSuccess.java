package com.kinfoitsolutions.ebooks.ui.responsemodel.SearchCategoryResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchCatSuccess {


    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private List<SearchCategoryPayload> data = null;

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

    public List<SearchCategoryPayload> getData() {
        return data;
    }

    public void setData(List<SearchCategoryPayload> data) {
        this.data = data;
    }
}