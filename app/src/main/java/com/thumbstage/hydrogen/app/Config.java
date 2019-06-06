package com.thumbstage.hydrogen.app;

import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.StringUtil;

public class Config {
    public final static int PAGE_SIZE = 15;
    public final static int PREFETCH_DISTANCE = 30;
    public final static User defaultUser = new User(StringUtil.DEFAULT_USERID, StringUtil.DEFAULT_USERID, "");
}
