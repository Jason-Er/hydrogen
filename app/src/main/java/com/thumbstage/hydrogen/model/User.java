package com.thumbstage.hydrogen.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class User {

    String id;
    String name;
    String avatar;
    Set<Privilege> privileges;

    public User(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.privileges = new LinkedHashSet<Privilege>() {
            {
                add(Privilege.BROWSE_PUBLISHEDCLOSED);
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof User)) {
            return false;
        }
        User user = (User) obj;
        boolean status = name.equals(user.name)
                && id.equals(user.id)
                && avatar.equals(user.avatar)
                && privileges.equals(user.privileges);
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 53 * hash + this.name.hashCode();
        return hash;
    }

    // region getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    // endregion
}
