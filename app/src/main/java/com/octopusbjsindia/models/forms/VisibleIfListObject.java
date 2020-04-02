package com.octopusbjsindia.models.forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VisibleIfListObject implements Serializable {

    @SerializedName("questionKey")
    @Expose
    private String questionKey;
    @SerializedName("answer")
    @Expose
    private String answer;

    public String getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(String questionKey) {
        this.questionKey = questionKey;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
