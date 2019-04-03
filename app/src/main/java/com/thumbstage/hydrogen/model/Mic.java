package com.thumbstage.hydrogen.model;

import java.util.ArrayList;
import java.util.List;

public class Mic implements Cloneable {
    String id; // conversation id
    Topic topic;

    List<Line> lineBuffer;

    public Mic() {
        topic = new Topic();
        lineBuffer = new ArrayList<>();
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

    public List<Line> getLineBuffer() {
        return lineBuffer;
    }

    public void setLineBuffer(List<Line> lineBuffer) {
        this.lineBuffer = lineBuffer;
    }
    // endregion

    public void speak(Line line) {
        lineBuffer.add(line);
    }

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
