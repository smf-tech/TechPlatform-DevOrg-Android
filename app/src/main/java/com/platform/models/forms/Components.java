package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.LocaleData;

import java.util.List;

@SuppressWarnings("unused")
public class Components {

    @SerializedName("title")
    @Expose
    private LocaleData title;
    @SerializedName("pages")
    @Expose
    private List<Page> pages = null;

    public LocaleData getTitle() {
        return title;
    }

    public void setTitle(LocaleData title) {
        this.title = title;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
