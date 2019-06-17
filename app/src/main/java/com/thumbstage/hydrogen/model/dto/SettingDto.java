package com.thumbstage.hydrogen.model.dto;

import com.thumbstage.hydrogen.model.bo.HyFile;

public class SettingDto extends HyFile {

    public SettingDto(String id, String url, Boolean isInCloud) {
        super("", id, url, isInCloud);
    }

}
