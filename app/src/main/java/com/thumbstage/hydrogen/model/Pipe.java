package com.thumbstage.hydrogen.model;

public class Pipe {
    String id; // conversation id

    public Pipe(String id) {
        this.id = id;
    }

    // region getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    // endregion
}
