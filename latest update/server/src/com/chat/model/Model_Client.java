package com.chat.model;

import com.corundumstudio.socketio.SocketIOClient;

public class Model_Client {

    public SocketIOClient getClient() {
        return client;
    }

    public void setClient(SocketIOClient client) {
        this.client = client;
    }

    public Model_User_Account getUser() {
        return user;
    }

    public void setUser(Model_User_Account user) {
        this.user = user;
    }
    
    public Model_Group getGroup() {
        return group;
    }

    public void setGroup(Model_Group group) {
        this.group = group;
    }

    public Model_Client(SocketIOClient client, Model_User_Account user, Model_Group group) {
        this.client = client;
        this.user = user;
        this.group = group;
    }

    public Model_Client() {
    }

    private SocketIOClient client;
    private Model_User_Account user;
    private Model_Group group;
}
