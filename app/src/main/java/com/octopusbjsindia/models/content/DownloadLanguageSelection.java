package com.octopusbjsindia.models.content;

public class DownloadLanguageSelection {

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private String language;
    private boolean isSelected;
}
