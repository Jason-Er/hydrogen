package com.thumbstage.hydrogen.model;

public class TopicEx {
    Topic topic;
    Pipe pipe;

    public TopicEx(Topic topic, Pipe pipe) {
        this.topic = topic;
        this.pipe = pipe;
    }

    // region getter and setter
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Pipe getPipe() {
        return pipe;
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }
    // endregion
}
