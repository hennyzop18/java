package com.chat.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.chat.app.MessageType;
import com.chat.model.Model_Client;
import com.chat.model.Model_File;
import com.chat.model.Model_Group;
import com.chat.model.Model_Group_Member;
import com.chat.model.Model_Login;
import com.chat.model.Model_Message;
import com.chat.model.Model_Package_Sender;
import com.chat.model.Model_Receive_Image;
import com.chat.model.Model_Receive_Message;
import com.chat.model.Model_Register;
import com.chat.model.Model_Reques_File;
import com.chat.model.Model_Send_Message;
import com.chat.model.Model_User_Account;
import com.mysql.jdbc.Connection;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

public class Service {
    
    private static Service instance;
    private SocketIOServer server;
    private ServiceUser serviceUser;
    private ServiceFIle serviceFile;
    private List<Model_Client> listClient;
    private JTextArea textArea;
    private final int PORT_NUMBER = 9999;
    
     private ServiceGroup groupService;
    private Connection con;
    
    public static Service getInstance(JTextArea textArea) {
        if (instance == null) {
            instance = new Service(textArea);
        }
        return instance;
    }
    
    private Service(JTextArea textArea) {
        this.textArea = textArea;
        serviceUser = new ServiceUser();
        serviceFile = new ServiceFIle();
        listClient = new ArrayList<>();
        
        this.groupService = new ServiceGroup(con);

    }
    
    public void startServer() {
        Configuration config = new Configuration();
        config.setPort(PORT_NUMBER);
        server = new SocketIOServer(config);
        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient sioc) {
                textArea.append("One client connected\n");
            }
        });
        
        server.addEventListener("create_group", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String groupName, AckRequest ar) throws Exception {
                try {
                    Model_Group newGroup = groupService.createGroup(groupName);
                    ar.sendAckData(true, newGroup);
                    // Broadcast new group creation if needed
                    server.getBroadcastOperations().sendEvent("new_group_created", newGroup);
                } catch (SQLException e) {
                    ar.sendAckData(false, "Group creation failed");
                }
            }
        });

        // Add member to a group
        server.addEventListener("add_group_member", Model_Group_Member.class, new DataListener<Model_Group_Member>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Group_Member groupMember, AckRequest ar) throws Exception {
                try {
                    Model_Group_Member addedMember = groupService.addGroupMember(groupMember.getGroupId(), groupMember.getUserId());
                    ar.sendAckData(true, addedMember);
                    // Notify group members about new member
                    server.getBroadcastOperations().sendEvent("group_member_added", addedMember);
                } catch (SQLException e) {
                    ar.sendAckData(false, "Adding member failed");
                }
            }
        });

        // Get group details
        server.addEventListener("get_group", Integer.class, new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer groupId, AckRequest ar) throws Exception {
                try {
                    Model_Group group = groupService.getGroupById(groupId);
                    if (group != null) {
                        ar.sendAckData(true, group);
                    } else {
                        ar.sendAckData(false, "Group not found");
                    }
                } catch (SQLException e) {
                    ar.sendAckData(false, "Error fetching group");
                }
            }
        });

        // Remove group member
        server.addEventListener("remove_group_member", Model_Group_Member.class, new DataListener<Model_Group_Member>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Group_Member groupMember, AckRequest ar) throws Exception {
                try {
                    boolean removed = groupService.removeGroupMember(groupMember.getGroupId(), groupMember.getUserId());
                    ar.sendAckData(removed);
                    if (removed) {
                        // Notify group members about member removal
                        server.getBroadcastOperations().sendEvent("group_member_removed", groupMember);
                    }
                } catch (SQLException e) {
                    ar.sendAckData(false, "Removing member failed");
                }
            }
        });

        // Update group name
        server.addEventListener("update_group_name", Model_Group.class, new DataListener<Model_Group>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Group group, AckRequest ar) throws Exception {
                try {
                    boolean updated = groupService.updateGroupName(group.getId(), group.getName());
                    ar.sendAckData(updated);
                    if (updated) {
                        // Broadcast group name update
                        server.getBroadcastOperations().sendEvent("group_name_updated", group);
                    }
                } catch (SQLException e) {
                    ar.sendAckData(false, "Updating group name failed");
                }
            }
        });

        // Delete group
        server.addEventListener("delete_group", Integer.class, new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer groupId, AckRequest ar) throws Exception {
                try {
                    boolean deleted = groupService.deleteGroup(groupId);
                    ar.sendAckData(deleted);
                    if (deleted) {
                        // Notify about group deletion
                        server.getBroadcastOperations().sendEvent("group_deleted", groupId);
                    }
                } catch (SQLException e) {
                    ar.sendAckData(false, "Deleting group failed");
                }
            }
        });
        
        server.addEventListener("register", Model_Register.class, new DataListener<Model_Register>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Register t, AckRequest ar) throws Exception {
                Model_Message message = serviceUser.register(t);
                ar.sendAckData(message.isAction(), message.getMessage(), message.getData());
                if (message.isAction()) {
                    textArea.append("User has Register :" + t.getUserName() + " Pass :" + t.getPassword() + "\n");
                    server.getBroadcastOperations().sendEvent("list_user", (Model_User_Account) message.getData());
                    addClient(sioc, (Model_User_Account) message.getData());
                }
            }
        });
        server.addEventListener("login", Model_Login.class, new DataListener<Model_Login>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Login t, AckRequest ar) throws Exception {
                Model_User_Account login = serviceUser.login(t);
                if (login != null) {
                    ar.sendAckData(true, login);
                    addClient(sioc, login);
                    userConnect(login.getUserID());
                } else {
                    ar.sendAckData(false);
                }
            }
        });
        server.addEventListener("list_user", Integer.class, new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer userID, AckRequest ar) throws Exception {
                try {
                    List<Model_User_Account> list = serviceUser.getUser(userID);
                    sioc.sendEvent("list_user", list.toArray());
                } catch (SQLException e) {
                    System.err.println(e);
                }
            }
        });
        server.addEventListener("send_to_user", Model_Send_Message.class, new DataListener<Model_Send_Message>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Send_Message t, AckRequest ar) throws Exception {
                sendToClient(t, ar);
            }
        });
        server.addEventListener("send_file", Model_Package_Sender.class, new DataListener<Model_Package_Sender>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Package_Sender t, AckRequest ar) throws Exception {
                try {
                    serviceFile.receiveFile(t);
                    if (t.isFinish()) {
                        ar.sendAckData(true);
                        Model_Receive_Image dataImage = new Model_Receive_Image();
                        dataImage.setFileID(t.getFileID());
                        Model_Send_Message message = serviceFile.closeFile(dataImage);
                        //  Send to client 'message'
                        sendTempFileToClient(message, dataImage);
                        
                    } else {
                        ar.sendAckData(true);
                    }
                } catch (IOException | SQLException e) {
                    ar.sendAckData(false);
                    e.printStackTrace();
                }
            }
        });
        server.addEventListener("get_file", Integer.class, new DataListener<Integer>() {
            @Override
            public void onData(SocketIOClient sioc, Integer t, AckRequest ar) throws Exception {
                Model_File file = serviceFile.initFile(t);
                long fileSize = serviceFile.getFileSize(t);
                ar.sendAckData(file.getFileExtension(), fileSize);
            }
        });
        server.addEventListener("reques_file", Model_Reques_File.class, new DataListener<Model_Reques_File>() {
            @Override
            public void onData(SocketIOClient sioc, Model_Reques_File t, AckRequest ar) throws Exception {
                byte[] data = serviceFile.getFileData(t.getCurrentLength(), t.getFileID());
                if (data != null) {
                    ar.sendAckData(data);
                } else {
                    ar.sendAckData();
                }
            }
        });
        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient sioc) {
                int userID = removeClient(sioc);
                if (userID != 0) {
                    //  removed
                    userDisconnect(userID);
                }
            }
        });
        server.start();
        textArea.append("Server has Start on port : " + PORT_NUMBER + "\n");
    }
    
    private void userConnect(int userID) {
        server.getBroadcastOperations().sendEvent("user_status", userID, true);
    }
    
    private void userDisconnect(int userID) {
        server.getBroadcastOperations().sendEvent("user_status", userID, false);
    }
    
    private void addClient(SocketIOClient client, Model_User_Account user) {
        listClient.add(new Model_Client(client, user));
    }
    
    private void sendToClient(Model_Send_Message data, AckRequest ar) {
        if (data.getMessageType() == MessageType.IMAGE.getValue() || data.getMessageType() == MessageType.FILE.getValue()) {
            try {
                Model_File file = serviceFile.addFileReceiver(data.getText());
                serviceFile.initFile(file, data);
                ar.sendAckData(file.getFileID());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            for (Model_Client c : listClient) {
                if (c.getUser().getUserID() == data.getToUserID()) {
                    c.getClient().sendEvent("receive_ms", new Model_Receive_Message(data.getMessageType(), data.getFromUserID(), data.getText(), null));
                    break;
                }
            }
        }
    }
    
    private void sendTempFileToClient(Model_Send_Message data, Model_Receive_Image dataImage) {
        for (Model_Client c : listClient) {
            if (c.getUser().getUserID() == data.getToUserID()) {
                c.getClient().sendEvent("receive_ms", new Model_Receive_Message(data.getMessageType(), data.getFromUserID(), data.getText(), dataImage));
                break;
            }
        }
    }
    
    public int removeClient(SocketIOClient client) {
        for (Model_Client d : listClient) {
            if (d.getClient() == client) {
                listClient.remove(d);
                return d.getUser().getUserID();
            }
        }
        return 0;
    }
    
    public List<Model_Client> getListClient() {
        return listClient;
    }
}
