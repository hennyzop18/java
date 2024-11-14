/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chat.service;
import com.chat.connection.DatabaseConnection;
import com.chat.model.Model_Group;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceGroup {
    private final Connection con;

    public ServiceGroup() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public List<Model_Group> getGroups() throws SQLException {
        List<Model_Group> groups = new ArrayList<>();
        PreparedStatement stmt = con.prepareStatement("SELECT GroupID, GroupName, GroupMember, ImageString, Status FROM groups");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int groupId = rs.getInt("GroupID");
            String groupName = rs.getString("GroupName");
            int groupMember = rs.getInt("GroupMember");
            String image = rs.getString("ImageString");
            boolean status = rs.getBoolean("Status");

            Model_Group group = new Model_Group(groupId, groupName, groupMember, image, status);
            groups.add(group);
        }

        rs.close();
        stmt.close();
        return groups;
    }
}