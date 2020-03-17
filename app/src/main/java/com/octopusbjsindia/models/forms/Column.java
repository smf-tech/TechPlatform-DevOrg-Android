package com.octopusbjsindia.models.forms;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.LocaleData;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class Column implements Serializable {
    @ColumnInfo(name = "choices_by_url_response_path")
    private String choicesByUrlResponsePath;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private LocaleData title;
    @SerializedName("cellType")
    @Expose
    private String cellType;
    @SerializedName("inputType")
    @Expose
    private String inputType;
    @SerializedName("enableIf")
    @Expose
    private String enableIf;
    @SerializedName("isRequired")
    @Expose
    private Boolean isRequired;
    @SerializedName("maxLength")
    @Expose
    private Integer maxLength;
    @SerializedName("validators")
    @Expose
    private List<Validator> validators = null;
    @SerializedName("choices")
    @Expose
    private List<Choice> choices = null;
    @SerializedName("choicesByUrl")
    @Expose
    private ChoicesByUrl choicesByUrl;

    public String getChoicesByUrlResponsePath() {
        return choicesByUrlResponsePath;
    }

    public void setChoicesByUrlResponsePath(String choicesByUrlResponsePath) {
        this.choicesByUrlResponsePath = choicesByUrlResponsePath;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public ChoicesByUrl getChoicesByUrl() {
        return choicesByUrl;
    }

    public void setChoicesByUrl(ChoicesByUrl choicesByUrl) {
        this.choicesByUrl = choicesByUrl;
    }

    public String getEnableIf() {
        return enableIf;
    }

    public void setEnableIf(String enableIf) {
        this.enableIf = enableIf;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public LocaleData getTitle() {
        return title;
    }

    public void setTitle(LocaleData title) {
        this.title = title;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}
