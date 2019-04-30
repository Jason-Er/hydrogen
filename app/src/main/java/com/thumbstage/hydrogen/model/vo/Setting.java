package com.thumbstage.hydrogen.model.vo;

import com.thumbstage.hydrogen.model.bo.HyFile;

public class Setting extends HyFile {

    public Setting(String id, String url, Boolean isInCloud) {
        super("", id, url, isInCloud);
    }

}
