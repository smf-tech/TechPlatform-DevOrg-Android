package com.octopusbjsindia.models.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketAssingnRequest {
    @SerializedName("ticket_id")
    @Expose
    private String ticketId;
    @SerializedName("ticket_type")
    @Expose
    private String ticketType;
    @SerializedName("ticket_title")
    @Expose
    private String ticketTitle;
    @SerializedName("ticket_desc")
    @Expose
    private String ticketDesc;
    @SerializedName("ticket_attachment")
    @Expose
    private String ticketAttachment;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("attachment")
    @Expose
    private String attachment;
    @SerializedName("role_name")
    @Expose
    private String roleName;
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("comment_type")
    @Expose
    private String commentType;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getTicketTitle() {
        return ticketTitle;
    }

    public void setTicketTitle(String ticketTitle) {
        this.ticketTitle = ticketTitle;
    }

    public String getTicketDesc() {
        return ticketDesc;
    }

    public void setTicketDesc(String ticketDesc) {
        this.ticketDesc = ticketDesc;
    }

    public String getTicketAttachment() {
        return ticketAttachment;
    }

    public void setTicketAttachment(String ticketAttachment) {
        this.ticketAttachment = ticketAttachment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

}
