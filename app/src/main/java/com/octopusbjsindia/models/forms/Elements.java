package com.octopusbjsindia.models.forms;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.LocaleData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Elements implements Serializable {
    @ColumnInfo(name = "choices_by_url_response_path")
    private String choicesByUrlResponsePath;

    private String mAnswer;
    private List<HashMap<String, String>> mAnswerArray;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private LocaleData title;
    @SerializedName("defaultValue")
    @Expose
    private String defaultValue;
    @SerializedName("inputType")
    @Expose
    private String inputType;
    @SerializedName("isRequired")
    @Expose
    private Boolean isRequired;
    @SerializedName("readOnly")
    @Expose
    private Boolean readOnly;
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
//    @SerializedName("rows")
//    @Expose
//    private Integer rows;
    @SerializedName("rows")
    @Expose
    private transient Integer rows;

    @SerializedName("rows")
    @Expose
    private List<Row> rowsList = null;

    @SerializedName("columns")
    @Expose
    private List<Column> columns;
    @SerializedName("keyName")
    @Expose
    private String keyName;
    @SerializedName("keyDuplicationError")
    @Expose
    private LocaleData keyDuplicationError;
    @SerializedName("hasOther")
    @Expose
    private Boolean hasOther;
    @SerializedName("hasNone")
    @Expose
    private Boolean hasNone;

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<HashMap<String, String>> getAnswerArray() {
        return mAnswerArray;
    }

    public void setAnswerArray(List<HashMap<String, String>> mAnswerArray) {
        this.mAnswerArray = mAnswerArray;
    }

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

//    public Integer getRows() {
//        return rows;
//    }
//
//    public void setRows(Integer rows) {
//        this.rows = rows;
//    }

//    public List<String> getRows() {
//        return rows;
//    }
//
//    public void setRows(List<String> rows) {
//        this.rows = rows;
//    }


    public List<Row> getRowsList() {
        return rowsList;
    }

    public void setRowsList(List<Row> rowsList) {
        this.rowsList = rowsList;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public LocaleData getKeyDuplicationError() {
        return keyDuplicationError;
    }

    public void setKeyDuplicationError(LocaleData keyDuplicationError) {
        this.keyDuplicationError = keyDuplicationError;
    }

    public Boolean getHasOther() {
        return hasOther;
    }

    public void setHasOther(Boolean hasOther) {
        this.hasOther = hasOther;
    }

    public Boolean getHasNone() {
        return hasNone;
    }

    public void setHasNone(Boolean hasNone) {
        this.hasNone = hasNone;
    }
}


