package com.chat.model;

import java.sql.Timestamp;

public class Model_Group_Message {
    public enum MessageType {
        TEXT,
        FILE,
        IMAGE
    }

    private int id;
    private int groupId;
    private int senderId;
    private String senderName;
    private MessageType messageType;
    private String content;
    private String fileName;
    private long fileSize;
    private Timestamp sentAt;

    // Constructors
    public Model_Group_Message() {
    }

    public Model_Group_Message(int groupId, int senderId, MessageType messageType, String content) {
        this.groupId = groupId;
        this.senderId = senderId;
        this.messageType = messageType;
        this.content = content;
        this.sentAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getSentAt() {
        return sentAt;
    }

    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
}