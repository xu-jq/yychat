package com.yychat.model;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author changing
 * @create 2022-05-31 10:33
 */
public class OnlineUserListModel extends AbstractListModel<Object> {
    private static final long serialVersionUID = -3903760573171074301L;
    private List<User> onlineUsers;

    public OnlineUserListModel(List<User> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public void addElement(Object object) {
        if (onlineUsers.contains(object)) {
            return;
        }
        int index = onlineUsers.size();
        onlineUsers.add((User)object);
        fireIntervalAdded(this, index, index);
    }

    public boolean removeElement(Object object) {
        int index = onlineUsers.indexOf(object);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
            User boolean1 = onlineUsers.remove(index);
            return true;
        }
        System.out.println(onlineUsers);
        return false;
    }

    public int getSize() {
        return onlineUsers.size();
    }

    public Object getElementAt(int i) {
        return onlineUsers.get(i);
    }

    public List<User> getOnlineUsers() {
        return onlineUsers;
    }
}
