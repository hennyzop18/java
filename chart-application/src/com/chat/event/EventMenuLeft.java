package com.chat.event;

import com.chat.model.Model_Group;
import com.chat.model.Model_User_Account;
import java.util.List;

public interface EventMenuLeft {

    public void newUser(List<Model_User_Account> users);

    public void userConnect(int userID);

    public void userDisconnect(int userID);
    
    public void newGroup(List<Model_Group> groups);

    public void groupConnect(int groupID);

    public void groupDisconnect(int groupID);
}
