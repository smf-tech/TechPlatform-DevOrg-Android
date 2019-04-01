package com.platform.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.platform.models.LocaleData;

import java.util.List;

import androidx.room.ColumnInfo;

@SuppressWarnings("unused")
public class Elements {
    @ColumnInfo(name = "choices_by_url_response_path")
    private String choicesByUrlResponsePath;

    private String mAnswer;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private LocaleData title;
    @SerializedName("inputType")
    @Expose
    private String inputType;
    @SerializedName("isRequired")
    @Expose
    private Boolean isRequired;
    @SerializedName("maxLength")
    @Expose
    private Integer maxLength;
    @SerializedName("enableIf")
    @Expose
    private String enableIf;
    @SerializedName("visibleIf")
    @Expose
    private String visibleIf;
    @SerializedName("choices")
    @Expose
    private List<Choice> choices = null;
    @SerializedName("choicesByUrl")
    @Expose
    private ChoicesByUrl choicesByUrl;
    @SerializedName("validators")
    @Expose
    private List<Validator> validators = null;
    @SerializedName("rows")
    @Expose
    private Integer rows;
    @SerializedName("columns")
    @Expose
    private List<Column> columns;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getChoicesByUrlResponsePath() {
        return choicesByUrlResponsePath;
    }

    public void setChoicesByUrlResponsePath(String choicesByUrlResponsePath) {
        this.choicesByUrlResponsePath = choicesByUrlResponsePath;
    }

    public String getEnableIf() {
        return enableIf;
    }

    public void setEnableIf(String enableIf) {
        this.enableIf = enableIf;
    }

    public String getVisibleIf() {
        return visibleIf;
    }

    public void setVisibleIf(String visibleIf) {
        this.visibleIf = visibleIf;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocaleData getTitle() {
        return title;
    }

    public void setTitle(LocaleData title) {
        this.title = title;
    }

    public Boolean isRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
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

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(final String answer) {
        this.mAnswer = answer;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}


