package com.thumbstage.hydrogen.view.common;

public class HyMenuItem {
    int resId;
    CommandType commandName;

    public enum CommandType {
        SETTING, START, PUBLISH, CLOSE, MEMBERS
    }

    public HyMenuItem(int resId, CommandType commandName) {
        this.resId = resId;
        this.commandName = commandName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public CommandType getCommandName() {
        return commandName;
    }

    public void setCommandName(CommandType commandName) {
        this.commandName = commandName;
    }
}
