package com.chat.event;

import com.chat.model.Model_Group;
import com.chat.model.Model_User_Account;


public interface EventMain {

    public void showLoading(boolean show);

    public void initChat();

    public void selectUser(Model_User_Account user);
    
    public void updateUser(Model_User_Account user);
    
    public void selectGroup(Model_Group group);

    public void updateGroup(Model_Group group);

}
