package com.chat.event;

import com.chat.model.Model_Group;
import com.chat.model.Model_Group_Member;

public interface EventGroup {
    void newGroupCreated(Model_Group group);
    void groupMemberAdded(Model_Group_Member member);
    void groupMemberRemoved(Model_Group_Member member);
    void groupNameUpdated(Model_Group group);
    void groupDeleted(int groupId);
}