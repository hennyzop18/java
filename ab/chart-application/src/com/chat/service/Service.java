package com.chat.service;

import com.chat.event.EventFileReceiver;
import com.chat.event.PublicEvent;
import com.chat.model.Model_File_Receiver;
import com.chat.model.Model_File_Sender;
import com.chat.model.Model_Group;
import com.chat.model.Model_Group_Member;
import com.chat.model.Model_Receive_Message;
import com.chat.model.Model_Send_Message;
import com.chat.model.Model_User_Account;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Service {

    private static Service instance;
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "localhost";
    private Model_User_Account user;
    private List<Model_File_Sender> fileSender;
    private List<Model_File_Receiver> fileReceiver;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private Service() {
        fileSender = new ArrayList<>();
        fileReceiver = new ArrayList<>();
    }

    public void startServer() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);
            
            client.on("new_group_created", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Group newGroup = new Model_Group(os[0]);
                    PublicEvent.getInstance().getEventGroup().newGroupCreated(newGroup);
                }
            });

            client.on("group_member_added", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Group_Member addedMember = new Model_Group_Member(os[0]);
                    PublicEvent.getInstance().getEventGroup().groupMemberAdded(addedMember);
                }
            });

            client.on("group_member_removed", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Group_Member removedMember = new Model_Group_Member(os[0]);
                    PublicEvent.getInstance().getEventGroup().groupMemberRemoved(removedMember);
                }
            });

            client.on("group_name_updated", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Group updatedGroup = new Model_Group(os[0]);
                    PublicEvent.getInstance().getEventGroup().groupNameUpdated(updatedGroup);
                }
            });

            client.on("group_deleted", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int groupId = (Integer) os[0];
                    PublicEvent.getInstance().getEventGroup().groupDeleted(groupId);
                }
            });
            
            client.on("list_user", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    //  list user
                    List<Model_User_Account> users = new ArrayList<>();
                    for (Object o : os) {
                        Model_User_Account u = new Model_User_Account(o);
                        if (u.getUserID() != user.getUserID()) {
                            users.add(u);
                        }
                    }
                    PublicEvent.getInstance().getEventMenuLeft().newUser(users);
                }
            });
            client.on("user_status", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    int userID = (Integer) os[0];
                    boolean status = (Boolean) os[1];
                    if (status) {
                        //  connect
                        PublicEvent.getInstance().getEventMenuLeft().userConnect(userID);
                    } else {
                        //  disconnect
                        PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userID);
                    }
                }
            });
            client.on("receive_ms", new Emitter.Listener() {
                @Override
                public void call(Object... os) {
                    Model_Receive_Message message = new Model_Receive_Message(os[0]);
                    PublicEvent.getInstance().getEventChat().receiveMessage(message);
                }
            });
            client.open();
        } catch (URISyntaxException e) {
            error(e);
        }
    }

    
    // Group interaction methods
    public void createGroup(String groupName) {
        client.emit("create_group", groupName);
    }

    public void addGroupMember(int groupId, int userId) {
        Model_Group_Member member = new Model_Group_Member(groupId, userId);
        client.emit("add_group_member", member.toJsonObject_group_member());
    }

    public void getGroupDetails(int groupId) {
        client.emit("get_group", groupId);
    }

    public void removeGroupMember(int groupId, int userId) {
        Model_Group_Member member = new Model_Group_Member(groupId, userId);
        client.emit("remove_group_member", member.toJsonObject_group_member());
    }

    public void updateGroupName(int groupId, String newName) {
        Model_Group group = new Model_Group(newName);
        group.setId(groupId);
        client.emit("update_group_name", group.toJsonObject_group());
    }

    public void deleteGroup(int groupId) {
        client.emit("delete_group", groupId);
    }
    
    
    
    
    public Model_File_Sender addFile(File file, Model_Send_Message message) throws IOException {
        Model_File_Sender data = new Model_File_Sender(file, client, message);
        message.setFile(data);
        fileSender.add(data);
        //  For send file one by one
        if (fileSender.size() == 1) {
            data.initSend();
        }
        return data;
    }

    public void fileSendFinish(Model_File_Sender data) throws IOException {
        fileSender.remove(data);
        if (!fileSender.isEmpty()) {
            //  Start send new file when old file sending finish
            fileSender.get(0).initSend();
        }
    }

    public void fileReceiveFinish(Model_File_Receiver data) throws IOException {
        fileReceiver.remove(data);
        if (!fileReceiver.isEmpty()) {
            fileReceiver.get(0).initReceive();
        }
    }

    public void addFileReceiver(int fileID, EventFileReceiver event) throws IOException {
        Model_File_Receiver data = new Model_File_Receiver(fileID, client, event);
        fileReceiver.add(data);
        if (fileReceiver.size() == 1) {
            data.initReceive();
        }
    }

    public Socket getClient() {
        return client;
    }

    public Model_User_Account getUser() {
        return user;
    }

    public void setUser(Model_User_Account user) {
        this.user = user;
    }

    private void error(Exception e) {
        System.err.println(e);
    }
}
