package com.thumbstage.hydrogen.app;

import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.cache.LCIMProfileCache;

public class UserGlobal {

    final static String TAG = "UserGlobal";
    private AVUser avUser;

    private final Set<Privilege> privilegeSet;

    private volatile static UserGlobal userGlobal = null;

    private UserGlobal() {
        privilegeSet = new LinkedHashSet<>();
    }

    public static UserGlobal getInstance() {
        if (userGlobal == null) {
            synchronized (UserGlobal.class) {
                if (userGlobal == null) {
                    userGlobal = new UserGlobal();
                }
            }
        }
        return userGlobal;
    }

    public void setAvUser(AVUser avUser) {
        this.avUser = avUser;
        Set<Privilege> privileges = new LinkedHashSet<Privilege>() {
            {
                add(Privilege.BROWSE_PUBLISHEDCLOSED);
            }
        };
        if( avUser != null ) {
            // TODO: 2/12/2019 needing fetch privileges of this userGlobal
            privileges.add(Privilege.BROWSE_PUBLISHEDOPENED);
            privileges.add(Privilege.BROWSE_AT_ME);
            privileges.add(Privilege.BROWSE_ISTARTED);
            privileges.add(Privilege.BROWSE_IATTENDED);
            //
            userConnect(avUser);
        }
        updateUserPrivileges(privileges);
    }

    public void userConnect(final AVUser avUser) {
        AVIMClient client = AVIMClient.getInstance(avUser);
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if(e == null) {
                    Log.i(TAG, "userConnect ok");
                } else {
                    // TODO: 2/25/2019 show some message for user that he cannot use IM
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateUserPrivileges(Set<Privilege> privileges) {
        privilegeSet.clear();
        for (Privilege pr : privileges) {
            privilegeSet.add(pr);
        }
    }

    public AVIMClient getClient() {
        if (!TextUtils.isEmpty(avUser.getObjectId()) && avUser != null) {
            return AVIMClient.getInstance(avUser.getObjectId());
        }
        return null;
    }

    public Set<Privilege> getPrivilegeSet() {
        return privilegeSet;
    }

    public String getCurrentUserId() {
        if(avUser != null) {
            return avUser.getObjectId();
        }
        return null;
    }

    public AVIMConversation getConversation(final String conversationId) {
        return getClient().getConversation(conversationId);
    }

    public void signOut() {
        AVUser.logOut();
        getClient().close(null);
        setAvUser(AVUser.getCurrentUser());
        // LCIMConversationItemCache.getInstance().cleanup();
    }

}
