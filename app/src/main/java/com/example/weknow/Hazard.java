package com.example.weknow;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hazard {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("hazard_desc")
    @Expose
    public String hazardDesc;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("lng")
    @Expose
    public String lng;

}

