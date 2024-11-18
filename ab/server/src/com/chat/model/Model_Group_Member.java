package com.chat.model;

import java.sql.Timestamp;

public class Model_Group_Member {
    public enum Role {
        ADMIN,
        MEMBER
    }

    private int groupId;
    private int userId;
    private Role role;
    private Timestamp joinedAt;
    private String userName; // Để hiển thị tên người dùng

    // Constructors
    public Model_Group_Member() {
    }

    public Model_Group_Member(int groupId, int userId, Role role) {
        this.groupId = groupId;
        this.userId = userId;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}