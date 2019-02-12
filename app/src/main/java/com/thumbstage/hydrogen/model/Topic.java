package com.thumbstage.hydrogen.model;

public class Topic {
    String name;
    String brief;
    String sponsor_name;
    String setting_url;
    String conversation_id;

    public static Topic Builder() {
        return new Topic();
    }

    public Topic setName(String name) {
        this.name = name;
        return this;
    }

    public Topic setBrief(String brief) {
        this.brief = brief;
        return this;
    }

    public Topic setSponsor_name(String sponsor_name) {
        this.sponsor_name = sponsor_name;
        return this;
    }

    public Topic setSetting_url(String setting_url) {
        this.setting_url = setting_url;
        return this;
    }

    public Topic setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
        return this;
    }

    // region getter
    public String getName() {
        return name;
    }

    public String getBrief() {
        return brief;
    }

    public String getSponsor_name() {
        return sponsor_name;
    }

    public String getSetting_url() {
        return setting_url;
    }

    public String getConversation_id() {
        return conversation_id;
    }
    // endregion
}
