package com.octopusbjsindia.models.stories;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedData {
    @SerializedName("feedId")
    @Expose
    private String feedId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("isExlusive")
    @Expose
    private Integer isExlusive;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("mediaUrl")
    @Expose
    private List<String> mediaUrl = null;
    @SerializedName("externalUrl")
    @Expose
    private String externalUrl;
    @SerializedName("likeCount")
    @Expose
    private Integer likeCount;
    @SerializedName("commentCount")
    @Expose
    private Integer commentCount;
    @SerializedName("shareCount")
    @Expose
    private Integer shareCount;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userProfileImage")
    @Expose
    private String userProfileImage;
    @SerializedName("createdDateTime")
    @Expose
    private String createdDateTime;

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsExlusive() {
        return isExlusive;
    }

    public void setIsExlusive(Integer isExlusive) {
        this.isExlusive = isExlusive;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<String> getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(List<String> mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
