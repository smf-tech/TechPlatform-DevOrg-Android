package com.platform.models.attendance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckIn {

@SerializedName("lat")
@Expose
private String lat;
@SerializedName("long")
@Expose
private String _long;
@SerializedName("time")
@Expose
private String time;
@SerializedName("address")
@Expose
private String address;

public String getLat() {
return lat;
}

public void setLat(String lat) {
this.lat = lat;
}

public String getLong() {
return _long;
}

public void setLong(String _long) {
this._long = _long;
}

public String getTime() {
return time;
}

public void setTime(String time) {
this.time = time;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

}