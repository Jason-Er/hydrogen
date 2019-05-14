package com.thumbstage.hydrogen.model.vo;

public class Mic implements Cloneable {
    String id; // conversation id
    Topic topic;
    boolean hasNewLine;

    public Mic() {
        topic = new Topic();
    }

    // region getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public boolean isHasNewLine() {
        return hasNewLine;
    }

    public void setHasNewLine(boolean hasNewLine) {
        this.hasNewLine = hasNewLine;
    }

    // endregion

    @Override
    public Object clone(){
        Mic mic = null;
        try{
            mic = (Mic) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mic.topic = (Topic) topic.clone();
        mic.setId("");
        return mic;
    }
}
