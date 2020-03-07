package com.octopusbjsindia.models.form_component;

public class RadioButtonData {
    String value;
    String text;
    boolean isSelected;

    public RadioButtonData(String value, String text, boolean isSelected) {
        this.value = value;
        this.text = text;
        this.isSelected = isSelected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
