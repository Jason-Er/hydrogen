package com.thumbstage.hydrogen.repository;

public enum FieldName {
    FIELD_ID("objectId"),
    FIELD_NAME("name"),
    FIELD_USERNAME("username"),
    FIELD_AVATAR("avatar"),
    FIELD_BRIEF("brief"),
    FIELD_IS_FINISHED("is_finished"),
    FIELD_STARTED_BY("started_by"),
    FIELD_DERIVE_FROM("derive_from"),
    FIELD_SETTING("setting"),
    FIELD_MEMBERS("members"),
    FIELD_DIALOGUE("dialogue"),
    FIELD_TOPIC("topic"),
    FIELD_PRIVILEGE("privilege"),
    FIELD_TYPE("type");

    final public String name;
    FieldName(String name) {
        this.name = name;
    }
}
