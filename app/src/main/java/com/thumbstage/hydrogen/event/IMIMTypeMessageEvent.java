package com.thumbstage.hydrogen.event;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

public class IMIMTypeMessageEvent {
    public AVIMTypedMessage message;
    public AVIMConversation conversation;
}
