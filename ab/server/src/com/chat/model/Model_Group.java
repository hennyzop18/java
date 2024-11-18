package com.chat.model;

import java.sql.Timestamp;
import java.util.List;

public class Model_Group {
    private int id;
    private String name;
    private String avatar;
    private String description;
    private Timestamp createdAt;
    private int createdBy;
    private List<Model_Group_Member> members; // Quan hệ 1-n với GroupMember

    // Constructor
    public Model_Group() {
    }

    public Model_Group(String name, String description, int createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public List<Model_Group_Member> getMembers() {
        return members;
    }

    public void setMembers(List<Model_Group_Member> members) {
        this.members = members;
    }
}