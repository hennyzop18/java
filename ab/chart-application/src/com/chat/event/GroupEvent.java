package com.chat.event;

import com.chat.model.Model_Group;
import com.chat.model.Model_Group_Member;

public class GroupEvent implements EventGroup {
    private EventGroup event;

    public GroupEvent() {
    }

    public void addEvent(EventGroup event) {
        this.event = event;
    }

    @Override
    public void newGroupCreated(Model_Group group) {
        if (event != null) {
            event.newGroupCreated(group);
        }
    }

    @Override
    public void groupMemberAdded(Model_Group_Member member) {
        if (event != null) {
            event.groupMemberAdded(member);
        }
    }

    @Override
    public void groupMemberRemoved(Model_Group_Member member) {
        if (event != null) {
            event.groupMemberRemoved(member);
        }
    }

    @Override
    public void groupNameUpdated(Model_Group group) {
        if (event != null) {
            event.groupNameUpdated(group);
        }
    }

    @Override
    public void groupDeleted(int groupId) {
        if (event != null) {
            event.groupDeleted(groupId);
        }
    }
}