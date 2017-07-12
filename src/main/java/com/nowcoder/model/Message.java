package com.nowcoder.model;

import java.util.Date;

/**
 * Created by ruiwen on 2017/7/9.
 */
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private String coversationId;
    private Date createdDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoversationId() {
        return coversationId;
    }

    public void setCoversationId(String coversationId) {
        this.coversationId = coversationId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
