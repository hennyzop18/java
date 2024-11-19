package com.chat.model;

import java.sql.Timestamp;
import org.json.JSONException;
import org.json.JSONObject;

public class Model_Group_Member {
    

    private int groupId;
    private int userId;
    private Timestamp joinedAt;

    // Constructors
    public Model_Group_Member(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            groupId = obj.getInt("groupId");
            userId = obj.getInt("userId");     
        } catch (JSONException e) {
            System.err.println(e);
        }
        
    }
    
    
    public JSONObject toJsonObject_group_member() {
        try {
            JSONObject json = new JSONObject();
            json.put("GroupID", groupId);
            json.put("UserID", userId);
            return json;
        } catch (JSONException e) {
            return null;
        }
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