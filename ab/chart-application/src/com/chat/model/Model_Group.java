/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.model;

/**
 *
 * @author leyen
 */
import java.sql.Timestamp;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class Model_Group {
    private int id;
    private String name;
    private Timestamp createdAt;
    private List<Model_Group_Member> members; // Quan hệ 1-n với GroupMember
    
    // Constructor
    public Model_Group(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            id = obj.getInt("GroupID");
            name = obj.getString("GroupName");
        } catch (JSONException e) {
            System.err.println(e);
        }
    }
    
    public JSONObject toJsonObject_group() {
        try {
            JSONObject json = new JSONObject();
            json.put("GroupName", name);
            return json;
        } catch (JSONException e) {
            return null;
        }
    }
    

    public Model_Group(String name) {
        this.name = name;
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

  

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public List<Model_Group_Member> getMembers() {
        return members;
    }

    public void setMembers(List<Model_Group_Member> members) {
        this.members = members;
    }
}