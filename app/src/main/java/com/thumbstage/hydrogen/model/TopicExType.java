package com.thumbstage.hydrogen.model;

import com.thumbstage.hydrogen.repository.TableName;

public enum TopicExType {
    PUBLISHED_OPENED(TableName.TABLE_PUBLISHED_OPENED.name),
    IPUBLISHED_OPENED(TableName.TABLE_PUBLISHED_OPENED.name),
    ISTARTED_OPENED(TableName.TABLE_STARTED_OPENED.name),
    IATTENDED_OPENED(TableName.TABLE_ATTENDED_OPENED.name);

    public final String name;
    TopicExType(String name) {
        this.name = name;
    }
}
