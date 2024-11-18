package com.chat.event;

import com.chat.model.Model_Message;

public interface EventMessage {

    public void callMessage(Model_Message message);
}
