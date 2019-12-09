
package com.octopusbjsindia.models.content;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("subModule")
    @Expose
    private String subModule;
    @SerializedName("contentData")
    @Expose
    private List<ContentDatum> contentData = null;

    public String getSubModule() {
        return subModule;
    }

    public void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    public List<ContentDatum> getContentData() {
        return contentData;
    }

    public void setContentData(List<ContentDatum> contentData) {
        this.contentData = contentData;
    }

}
