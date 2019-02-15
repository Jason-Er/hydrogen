package com.thumbstage.hydrogen.utils;

import com.thumbstage.hydrogen.model.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataConvertUtil {

    public static List<Map> convert2AVObject(List<Line> lines) {
        List<Map> list = new ArrayList<>();
        for(Line line:lines) {
            Map map = new HashMap();
            map.put("who", line.getWho());
            map.put("when", line.getWhen());
            map.put("what", line.getWhat());
            map.put("type", line.getLineType().name());
            list.add(map);
        }
        return list;
    }

    public static Map convert2AVObject(Line line) {
        Map map = new HashMap();
        map.put("who", line.getWho());
        map.put("when", line.getWhen());
        map.put("what", line.getWhat());
        map.put("type", line.getLineType().name());
        return map;
    }
}
