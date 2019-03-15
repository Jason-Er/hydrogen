package com.thumbstage.hydrogen.repository;

public enum TableName {
    TABLE_USER("_User"),
    TABLE_MIC("_Conversation"),
    TABLE_FILE("_File"),
    TABLE_PUBLISHED_OPENED("PublishedOpened"),
    TABLE_STARTED_OPENED("StartedOpened"),
    TABLE_ATTENDED_OPENED("AttendedOpened");

    public final String name;
    TableName(String name) {
        this.name = name;
    }
}
