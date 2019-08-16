package com.platform.models.content;

import java.util.List;

public class DownloadContent {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMr() {
        return mr;
    }

    public void setMr(String mr) {
        this.mr = mr;
    }

    public String getHi() {
        return hi;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    private String name,mr,hi,def;

    public List<DownloadContent> getListDownloadData() {
        return listDownloadData;
    }

    public void setListDownloadData(List<DownloadContent> listDownloadData) {
        this.listDownloadData = listDownloadData;
    }

    private List<DownloadContent> listDownloadData;

    public DownloadInfo getInfo() {
        return info;
    }

    public void setInfo(DownloadInfo info) {
        this.info = info;
    }

    private DownloadInfo info;


}
