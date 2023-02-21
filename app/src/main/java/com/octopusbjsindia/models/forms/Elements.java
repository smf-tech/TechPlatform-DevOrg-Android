package com.octopusbjsindia.models.forms;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.octopusbjsindia.models.LocaleData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import kotlin.jvm.JvmField;

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

    @SerializedName("description")
    @Expose
    private LocaleData description;

    @SerializedName("defaultValue")
    @Expose
    private String defaultValue;
    @SerializedName("inputType")
    @Expose
    private String inputType;
    @SerializedName("isRequired")
    @Expose
    private Boolean isRequired = false;
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
    private VisibleIf visibleIf;
    @SerializedName("choices")
    @Expose
    private List<Choice> choices = null;

    @SerializedName("items")
    @Expose
    private List<MultiTextItem> items = null;
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
    @SerializedName("cellType")
    @Expose
    private String cellType;
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
    @SerializedName("rateMax")
    @Expose
    private Integer rateMax;
    @SerializedName("minRateDescription")
    @Expose
    private MinRateDescription minRateDescription;
    @SerializedName("maxRateDescription")
    @Expose
    private MaxRateDescription maxRateDescription;

    @SerializedName("placeHolder")
    @Expose
    private LocaleData placeHolder;
    @SerializedName("requiredErrorText")
    @Expose
    private LocaleData requiredErrorText;
    @SerializedName("otherErrorText")
    @Expose
    private LocaleData otherErrorText;
    @SerializedName("hasNone")
    @Expose
    private Boolean hasNone;
    private String locationRequiredLevel;

    @SerializedName("minDays")
    @Expose
    private Integer pastAllowedDays;
    @SerializedName("maxDays")
    @Expose
    private Integer futureAllowedDays;

    public LocaleData getOtherErrorText() {
        return otherErrorText;
    }

    public void setOtherErrorText(LocaleData otherErrorText) {
        this.otherErrorText = otherErrorText;
    }

    @SerializedName("minDate")
    @Expose
    private Long minDate;
    @SerializedName("maxDate")
    @Expose
    private Long maxDate;


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

    public LocaleData getDescription() {
        return description;
    }

    public void setDescription(LocaleData description) {
        this.description = description;
    }

    public String getEnableIf() {
        return enableIf;
    }

    public void setEnableIf(String enableIf) {
        this.enableIf = enableIf;
    }

    public VisibleIf getVisibleIf() {
        return visibleIf;
    }

    public void setVisibleIf(VisibleIf visibleIf) {
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

    public List<MultiTextItem> getItems() {
        return items;
    }

    public void setItems(List<MultiTextItem> items) {
        this.items = items;
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



    public Integer getRateMax() {
        return rateMax;
    }

    public void setRateMax(Integer rateMax) {
        this.rateMax = rateMax;
    }

    public MinRateDescription getMinRateDescription() {
        return minRateDescription;
    }

    public void setMinRateDescription(MinRateDescription minRateDescription) {
        this.minRateDescription = minRateDescription;
    }

    public MaxRateDescription getMaxRateDescription() {
        return maxRateDescription;
    }

    public void setMaxRateDescription(MaxRateDescription maxRateDescription) {
        this.maxRateDescription = maxRateDescription;
    }

    public String getLocationRequiredLevel() {
        return locationRequiredLevel;
    }

    public void setLocationRequiredLevel(String locationRequiredLevel) {
        this.locationRequiredLevel = locationRequiredLevel;
    }

    public LocaleData getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(LocaleData placeHolder) {
        this.placeHolder = placeHolder;
    }

    public LocaleData getRequiredErrorText() {
        return requiredErrorText;
    }

    public void setRequiredErrorText(LocaleData requiredErrorText) {
        this.requiredErrorText = requiredErrorText;
    }

    public Long getMinDate() {
        return minDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public Long getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public Integer getPastAllowedDays() {
        return pastAllowedDays;
    }

    public void setPastAllowedDays(Integer pastAllowedDays) {
        this.pastAllowedDays = pastAllowedDays;
    }

    public Integer getFutureAllowedDays() {
        return futureAllowedDays;
    }

    public void setFutureAllowedDays(Integer futureAllowedDays) {
        this.futureAllowedDays = futureAllowedDays;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }
}


