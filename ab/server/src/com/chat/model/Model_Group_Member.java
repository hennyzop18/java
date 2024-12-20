package com.chat.model;

import java.sql.Timestamp;

public class Model_Group_Member {
    

    private int groupId;
    private int userId;
    private Timestamp joinedAt;

    // Constructors
    public Model_Group_Member() {
    }

    public Model_Group_Member(int groupId, int userId) {
        this.groupId = groupId;
        this.userId = userId;
        this.joinedAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }

}