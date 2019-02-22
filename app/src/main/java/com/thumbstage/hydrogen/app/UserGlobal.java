package com.thumbstage.hydrogen.app;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.thumbstage.hydrogen.view.browse.BrowseFragmentPagerAdapter;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IStartedFragment;
import com.thumbstage.hydrogen.view.browse.IPagerAdapterCustomize;
import com.thumbstage.hydrogen.view.browse.published.PublishedClosedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedOpenedFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.cache.LCIMProfileCache;

public class UserGlobal implements IPagerAdapterCustomize {

    final static String TAG = "UserGlobal";
    private AVUser avUser;
    private final Map<String, Fragment> fragmentMap;
    private final Set<Privilege> privilegeSet;

    private volatile static UserGlobal userGlobal = null;

    public interface ICallBack {
        void callBack(AVIMConversation conv);
    }

    private UserGlobal() {
        privilegeSet = new LinkedHashSet<>();
        fragmentMap = new HashMap<String, Fragment>() {
            {
                put(Privilege.BROWSE_PUBLISHEDCLOSED.name(), new PublishedClosedFragment());
                put(Privilege.BROWSE_PUBLISHEDOPENED.name(), new PublishedOpenedFragment());
                put(Privilege.BROWSE_ISTARTED.name(), new IStartedFragment());
                put(Privilege.BROWSE_IATTENDED.name(), new IAttendedFragment());
            }
        };
    }

    @Override
    public void customize(BrowseFragmentPagerAdapter adapter) {
        List<Fragment> fragmentList = new ArrayList<>();
        for(Privilege pr : privilegeSet) {
            if(fragmentMap.containsKey(pr.name())) {
                fragmentList.add(fragmentMap.get(pr.name()));
            }
        }
        adapter.setFragmentList(fragmentList);
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
            privileges.add(Privilege.BROWSE_ISTARTED);
            privileges.add(Privilege.BROWSE_IATTENDED);
        }
        updateUserPrivileges(privileges);
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

    public String getCurrentUserId() {
        if(avUser != null) {
            return avUser.getObjectId();
        }
        return null;
    }

    public void getConversation(final String conversationId, final ICallBack callBack) {
        getClient().open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if(e == null) {
                    callBack.callBack(getClient().getConversation(conversationId));
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
