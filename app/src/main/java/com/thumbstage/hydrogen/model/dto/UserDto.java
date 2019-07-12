package com.thumbstage.hydrogen.model.dto;

import com.thumbstage.hydrogen.model.bo.Privilege;

import java.util.LinkedHashSet;
import java.util.Set;

public class UserDto {

    String id;
    String name;
    String avatar;
    String badge;
    Set<Privilege> privileges;

    public UserDto() {

    }

    public UserDto(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.privileges = new LinkedHashSet<Privilege>() {
            {
                add(Privilege.BROWSE_COMMUNITY_SHOW);
            }
        };
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof UserDto)) {
            return false;
        }
        UserDto user = (UserDto) obj;
        boolean status = id.equals(user.id);
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

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    // endregion
}
