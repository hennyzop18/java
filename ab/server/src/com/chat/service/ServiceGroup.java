package com.chat.service;

import com.chat.connection.DatabaseConnection;
import com.chat.model.Model_Group;
import com.chat.model.Model_Group_Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ServiceGroup {
    private Connection con;

    public ServiceGroup(Connection connection) {
        this.con = DatabaseConnection.getInstance().getConnection();;
    }

    // Create a new group
    public Model_Group createGroup(String groupName) throws SQLException {
        String sql = "INSERT INTO groups (name, created_at) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setString(1, groupName);
            stmt.setTimestamp(2, now);
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Model_Group group = new Model_Group(groupName);
                    group.setId(generatedKeys.getInt(1));
                    group.setCreatedAt(now);
                    return group;
                }
            }
        }
        throw new SQLException("Creating group failed, no ID obtained.");
    }

    // Add a member to a group
    public Model_Group_Member addGroupMember(int groupId, int userId) throws SQLException {
        String sql = "INSERT INTO group_members (group_id, user_id, joined_at) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setInt(1, groupId);
            stmt.setInt(2, userId);
            stmt.setTimestamp(3, now);
            
            stmt.executeUpdate();
            
            Model_Group_Member member = new Model_Group_Member(groupId, userId);
            member.setJoinedAt(now);
            return member;
        }
    }

    // Get group by ID
    public Model_Group getGroupById(int groupId) throws SQLException {
        String sql = "SELECT * FROM groups WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Model_Group group = new Model_Group();
                    group.setId(rs.getInt("id"));
                    group.setName(rs.getString("name"));
                    group.setCreatedAt(rs.getTimestamp("created_at"));
                    
                    // Fetch group members
                    group.setMembers(getGroupMembers(groupId));
                    
                    return group;
                }
            }
        }
        return null;
    }

    // Get all members of a group
    public List<Model_Group_Member> getGroupMembers(int groupId) throws SQLException {
        String sql = "SELECT * FROM group_members WHERE group_id = ?";
        List<Model_Group_Member> members = new ArrayList<>();
        
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Model_Group_Member member = new Model_Group_Member();
                    member.setGroupId(rs.getInt("group_id"));
                    member.setUserId(rs.getInt("user_id"));
                    member.setJoinedAt(rs.getTimestamp("joined_at"));
                    members.add(member);
                }
            }
        }
        return members;
    }

    // Remove a member from a group
    public boolean removeGroupMember(int groupId, int userId) throws SQLException {
        String sql = "DELETE FROM group_members WHERE group_id = ? AND user_id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, groupId);
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Update group name
    public boolean updateGroupName(int groupId, String newName) throws SQLException {
        String sql = "UPDATE groups SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, groupId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a group
    public boolean deleteGroup(int groupId) throws SQLException {
        // First, delete all members associated with the group
        String deleteMembersSQL = "DELETE FROM group_members WHERE group_id = ?";
        try (PreparedStatement membersStmt = con.prepareStatement(deleteMembersSQL)) {
            membersStmt.setInt(1, groupId);
            membersStmt.executeUpdate();
        }

        // Then delete the group itself
        String deleteGroupSQL = "DELETE FROM groups WHERE id = ?";
        try (PreparedStatement groupStmt = con.prepareStatement(deleteGroupSQL)) {
            groupStmt.setInt(1, groupId);
            int affectedRows = groupStmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}