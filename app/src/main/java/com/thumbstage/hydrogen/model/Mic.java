package com.thumbstage.hydrogen.model;

public class Mic implements Cloneable {
    String id; // conversation id
    Topic topic;

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
        mic.topic.setDerive_from(topic.getId());
        mic.topic.setId("");
        mic.topic.setStarted_by(null);
        mic.setId("");
        return mic;
    }
}
