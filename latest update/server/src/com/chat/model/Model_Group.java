/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.model;

/**
 *
 * @author leyen
 */

public class Model_Group {
    

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
      public int getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(int groupMember) {
        this.groupMember = groupMember;
    }
    

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Model_Group(int groupID, String groupName, int groupMember, String image, boolean status) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.groupMember = groupMember;
        this.image = image;
        this.status = status;
    }

 
    public Model_Group() {
    }
private int groupID;
    private String groupName;
    private int groupMember;
    private String image;
    private boolean status;
    
}
