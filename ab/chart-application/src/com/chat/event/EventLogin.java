package com.chat.event;

import com.chat.model.Model_Login;
import com.chat.model.Model_Register;

public interface EventLogin {

    public void login(Model_Login data);

    public void register(Model_Register data, EventMessage message);

    public void goRegister();

    public void goLogin();
}
