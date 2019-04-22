package com.thumbstage.hydrogen.utils;

import java.util.HashSet;
import java.util.List;

public class CollectionsUtil {

    public static List<? extends Object> removeDuplicate(List<? extends Object> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

}
