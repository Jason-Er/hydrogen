package com.thumbstage.hydrogen.utils;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.model.vo.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataConvertUtil {

    public static List<Map> convert2AVObject(List<Line> lines) {
        List<Map> list = new ArrayList<>();
        for(Line line:lines) {
            Map map = new HashMap();
            map.put("who", line.getWho().getId());
            map.put("when", StringUtil.date2String(line.getWhen()));
            map.put("what", line.getWhat());
            map.put("type", line.getLineType().name());
            list.add(map);
        }
        return list;
    }

    public static Map convert2AVObject(Line line) {
        Map map = new HashMap();
        map.put("who", line.getWho().getId());
        map.put("when", StringUtil.date2String(line.getWhen()));
        map.put("what", line.getWhat());
        map.put("type", line.getLineType().name());
        return map;
    }

    public static Line convert2Line(AVIMMessage message) {
        String who = message.getFrom();
        Date when = new Date(message.getTimestamp());
        String what = null;
        LineType type = LineType.LT_DIALOGUE;
        if(message instanceof AVIMTextMessage) {
            what = ((AVIMTextMessage) message).getText();
            type = LineType.valueOf((String) ((AVIMTextMessage) message).getAttrs().get("type"));
        } else if(message instanceof AVIMAudioMessage) {
            what = (String) ((AVIMAudioMessage) message).getAttrs().get("what");
            type = LineType.valueOf((String) ((AVIMAudioMessage) message).getAttrs().get("type"));
        }
        Line line = new Line(new User(who, "",""), when, what, type);
        return line;
    }

    public static List<String> user2StringId(List<User> users) {
        List<String> ids = new ArrayList<>();
        for(User user: users) {
            ids.add(user.getId());
        }
        return ids;
    }
}
